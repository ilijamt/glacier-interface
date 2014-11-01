package com.matoski.glacier.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Principal;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.Statement.Effect;
import com.amazonaws.auth.policy.actions.SQSActions;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.metrics.RequestMetricCollector;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.CreateVaultRequest;
import com.amazonaws.services.glacier.model.CreateVaultResult;
import com.amazonaws.services.glacier.model.DeleteArchiveRequest;
import com.amazonaws.services.glacier.model.DeleteVaultRequest;
import com.amazonaws.services.glacier.model.DescribeVaultOutput;
import com.amazonaws.services.glacier.model.DescribeVaultRequest;
import com.amazonaws.services.glacier.model.DescribeVaultResult;
import com.amazonaws.services.glacier.model.GetJobOutputRequest;
import com.amazonaws.services.glacier.model.GetJobOutputResult;
import com.amazonaws.services.glacier.model.GlacierJobDescription;
import com.amazonaws.services.glacier.model.InitiateJobRequest;
import com.amazonaws.services.glacier.model.InitiateJobResult;
import com.amazonaws.services.glacier.model.JobParameters;
import com.amazonaws.services.glacier.model.ListJobsRequest;
import com.amazonaws.services.glacier.model.ListJobsResult;
import com.amazonaws.services.glacier.model.ListMultipartUploadsRequest;
import com.amazonaws.services.glacier.model.ListMultipartUploadsResult;
import com.amazonaws.services.glacier.model.ListPartsRequest;
import com.amazonaws.services.glacier.model.ListPartsResult;
import com.amazonaws.services.glacier.model.ListVaultsRequest;
import com.amazonaws.services.glacier.model.ListVaultsResult;
import com.amazonaws.services.glacier.model.UploadListElement;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;
import com.matoski.glacier.errors.RegionNotSupportedException;

/**
 * A base glacier utility
 * 
 * @author ilijamt
 */
public abstract class AmazonGlacierBaseUtil {

    /**
     * Generic string format
     */
    final public static String FORMAT = "[#%1$05d/#%2$05d] %3$-15s | (%4$s) %5$s";

    /**
     * Can we split the file in the specified parts, without hitting the limit
     * of {@link AmazonGlacierBaseUtil#MAXIMUM_UPLOAD_PARTS}
     * 
     * @param file
     * @param partSize
     * @return
     */
    public static Boolean isValidMaxParts(final File file, int partSize) {
	if (!file.exists() || !file.isFile()) {
	    return false;
	}

	return (file.length() / partSize) < MAXIMUM_UPLOAD_PARTS;
    }

    /**
     * The maximum part size, in bytes, for a Glacier multipart upload.
     */
    public static final long MAXIMUM_UPLOAD_PARTS = 1000L * 10;

    /**
     * The maximum part size, in bytes, for a Glacier multipart upload.
     */
    public static final long MAXIMUM_UPLOAD_PART_SIZE = 1024L * 1024 * 1024 * 4;

    /**
     * The default chunk size, in bytes, when downloading in multiple chunks
     * using range retrieval.
     */
    public static final long DEFAULT_DOWNLOAD_CHUNK_SIZE = 1024L * 1024 * 128;

    /**
     * The minimum part size, in bytes, for a Glacier multipart upload.
     */
    public static final long MINIMUM_PART_SIZE = 1024L * 1024;

    /**
     * Threshold, in bytes, for when to use the multipart upload operations
     */
    public static final long MULTIPART_UPLOAD_SIZE_THRESHOLD = 1024L * 1024L * 100;

    /**
     * Service name
     */
    public static String SERVICE_NAME = "glacier";

    /**
     * Service name
     */
    public static String SERVICE_SNS_NAME = "sns";

    /**
     * Service name
     */
    public static String SERVICE_SQS_NAME = "sqs";

    /**
     * The credentials for Amazon Glacier
     */
    protected final BasicAWSCredentials credentials;

    /**
     * The client for Amazon Glacier
     */
    protected final AmazonGlacierClient client;

    /**
     * The region for Amazon Glacier
     */
    protected final Region region;

    /**
     * Do we have HTTP support?
     */
    protected final Boolean hasHttpEndpoint;

    /**
     * Do we have HTTPS support
     */
    protected final Boolean hasHttpsEndpoint;

    /**
     * The client Endpoint
     */
    protected final String clientEndpoint;

    /**
     * Amazon SQS Client
     */
    protected final AmazonSQSClient sqsClient;

    /**
     * Amazon SNS Client
     */
    protected final AmazonSNSClient snsClient;

    /**
     * The SNS topic used for the job
     */
    final private static String SNS_TOPIC_NAME = "gi-sns-topic-job";

    /**
     * The SNS queue used for the job
     */
    final private static String SQS_QUEUE_NAME = "gi-sqs-queue-job";

    /**
     * SNS Queue ARN
     */
    private String snsQueueArn;

    /**
     * SQS Queue ARN
     */
    private String sqsQueueArn;

    /**
     * SQS Queue URL
     */
    private String sqsQueueUrl;

    /**
     * SNS Topic ARN
     */
    private String snsTopicArn;

    /**
     * SNS Subscription ARN
     */
    private String snsSubscriptionArn;

    /**
     * Constructor
     * 
     * @param credentials
     * @param client
     * @param region
     */
    public AmazonGlacierBaseUtil(BasicAWSCredentials credentials, AmazonGlacierClient client, Region region) {
	super();
	this.credentials = credentials;
	this.client = client;
	this.region = region;
	this.hasHttpEndpoint = this.region.hasHttpEndpoint(SERVICE_NAME);
	this.hasHttpsEndpoint = this.region.hasHttpsEndpoint(SERVICE_NAME);
	this.clientEndpoint = this.region.getServiceEndpoint(SERVICE_NAME);

	this.sqsClient = new AmazonSQSClient(credentials);
	this.sqsClient.setRegion(region);
	this.sqsClient.setEndpoint(this.region.getServiceEndpoint(SERVICE_SQS_NAME));

	this.snsClient = new AmazonSNSClient(credentials);
	this.snsClient.setRegion(region);
	this.snsClient.setEndpoint(this.region.getServiceEndpoint(SERVICE_SNS_NAME));

    }

    /**
     * Constructor
     * 
     * @param accessKey
     * @param secretKey
     * @param region
     * 
     * @throws RegionNotSupportedException
     */
    public AmazonGlacierBaseUtil(String accessKey, String secretKey, String region) throws RegionNotSupportedException {

	ClientConfiguration clientConfiguration = new ClientConfiguration();
	clientConfiguration.setConnectionTimeout(70 * 1000);

	this.region = Region.getRegion(Regions.fromName(region));
	this.credentials = new BasicAWSCredentials(accessKey, secretKey);
	this.client = new AmazonGlacierClient(this.credentials, clientConfiguration);

	if (!this.region.isServiceSupported(SERVICE_NAME)) {
	    throw new RegionNotSupportedException();
	}

	this.hasHttpEndpoint = this.region.hasHttpEndpoint(SERVICE_NAME);
	this.hasHttpsEndpoint = this.region.hasHttpsEndpoint(SERVICE_NAME);
	this.clientEndpoint = this.region.getServiceEndpoint(SERVICE_NAME);

	this.client.setRegion(this.region);
	this.client.setEndpoint(this.clientEndpoint);

	this.sqsClient = new AmazonSQSClient(credentials);
	this.sqsClient.setRegion(this.region);
	this.sqsClient.setEndpoint(this.region.getServiceEndpoint(SERVICE_SQS_NAME));

	this.snsClient = new AmazonSNSClient(credentials);
	this.snsClient.setRegion(this.region);
	this.snsClient.setEndpoint(this.region.getServiceEndpoint(SERVICE_SNS_NAME));
    }

    /**
     * Cleanup
     */
    protected void cleanup() {
	snsClient.unsubscribe(snsSubscriptionArn);
	snsClient.deleteTopic(new DeleteTopicRequest(snsTopicArn));
	sqsClient.deleteQueue(new DeleteQueueRequest(sqsQueueUrl));
    }

    /**
     * Initialize SQS and SNS
     */
    protected void init() {

	initSQS();
	initSNS();

    }

    /**
     * Initialize SNS
     */
    final protected void initSNS() {

	CreateTopicRequest request = new CreateTopicRequest().withName(SNS_TOPIC_NAME);
	CreateTopicResult result = snsClient.createTopic(request);
	snsTopicArn = result.getTopicArn();

	SubscribeRequest subscribeRequest = new SubscribeRequest().withTopicArn(snsTopicArn).withEndpoint(sqsQueueArn).withProtocol("sqs");

	SubscribeResult subscribeResult = snsClient.subscribe(subscribeRequest);

	snsSubscriptionArn = subscribeResult.getSubscriptionArn();

    }

    /**
     * Initialize SQS
     */
    final protected void initSQS() {

	CreateQueueRequest request = new CreateQueueRequest().withQueueName(SQS_QUEUE_NAME);
	CreateQueueResult result = sqsClient.createQueue(request);
	sqsQueueUrl = result.getQueueUrl();

	GetQueueAttributesRequest qRequest = new GetQueueAttributesRequest().withQueueUrl(sqsQueueUrl).withAttributeNames("QueueArn");

	GetQueueAttributesResult qResult = sqsClient.getQueueAttributes(qRequest);
	sqsQueueArn = qResult.getAttributes().get("QueueArn");

	Policy sqsPolicy = new Policy().withStatements(new Statement(Effect.Allow).withPrincipals(Principal.AllUsers)
		.withActions(SQSActions.SendMessage).withResources(new Resource(sqsQueueArn)));
	Map<String, String> queueAttributes = new HashMap<String, String>();
	queueAttributes.put("Policy", sqsPolicy.toJson());
	sqsClient.setQueueAttributes(new SetQueueAttributesRequest(sqsQueueUrl, queueAttributes));

    }

    /**
     * Create a vault
     * 
     * @param vaultName
     * 
     * @return
     * 
     * @throws AmazonClientException
     * @throws AmazonServiceException
     */
    public CreateVaultResult CreateVault(String vaultName) throws AmazonServiceException, AmazonClientException {
	CreateVaultRequest createVaultRequest = new CreateVaultRequest().withVaultName(vaultName);
	return client.createVault(createVaultRequest);
    }

    /**
     * Delete an archive
     * 
     * @param vaultName
     * @param archiveId
     */
    public void DeleteArchive(String vaultName, String archiveId) {
	DeleteArchive(vaultName, archiveId, null, null);
    }

    /**
     * Delete an archive
     * 
     * @param vaultName
     * @param archiveId
     * @param listener
     * @param collector
     */
    public void DeleteArchive(String vaultName, String archiveId, ProgressListener listener, RequestMetricCollector collector) {

	DeleteArchiveRequest request = new DeleteArchiveRequest().withVaultName(vaultName).withArchiveId(archiveId);

	if (null != listener) {
	    request.withGeneralProgressListener(listener);
	}

	if (null != collector) {
	    request.withRequestMetricCollector(collector);
	}

	client.deleteArchive(request);

    }

    /**
     * Delete a vault
     * 
     * @param vaultName
     * 
     * @throws AmazonClientException
     * @throws AmazonServiceException
     */
    public void DeleteVault(String vaultName) throws AmazonServiceException, AmazonClientException {
	DeleteVaultRequest request = new DeleteVaultRequest().withVaultName(vaultName);
	client.deleteVault(request);
    }

    /**
     * Describe the vault
     * 
     * @param vaultName
     * 
     * @return
     * 
     * @throws AmazonClientException
     * @throws AmazonServiceException
     */
    public DescribeVaultResult DescribeVault(String vaultName) throws AmazonServiceException, AmazonClientException {
	DescribeVaultRequest describeVaultRequest = new DescribeVaultRequest().withVaultName(vaultName);
	return client.describeVault(describeVaultRequest);
    }

    /**
     * Get a list of the parts in the specified multipart upload id
     * 
     * @param vaultName
     * @param uploadId
     * @return
     */
    public ListPartsResult GetMultipartUploadInfo(String vaultName, String uploadId) {

	String marker = null;
	ListPartsResult response = new ListPartsResult();

	do {

	    ListPartsRequest request = new ListPartsRequest().withVaultName(vaultName).withUploadId(uploadId).withMarker(marker);

	    ListPartsResult result = this.client.listParts(request);

	    if (null == marker) {
		response.setArchiveDescription(result.getArchiveDescription());
		response.setCreationDate(result.getCreationDate());
		response.setMultipartUploadId(result.getMultipartUploadId());
		response.setVaultARN(result.getVaultARN());
		response.setPartSizeInBytes(result.getPartSizeInBytes());
		response.setParts(result.getParts());
	    } else {
		response.getParts().addAll(result.getParts());
	    }

	    marker = result.getMarker();

	} while (marker != null);

	return response;

    }

    /**
     * Download the inventory
     * 
     * @param vaultName
     * @param jobId
     * 
     * @return
     */
    public GetJobOutputResult InventoryDownload(String vaultName, String jobId) {
	return InventoryDownload(vaultName, jobId, null, null);
    }

    /**
     * Download the inventory
     * 
     * @param vaultName
     * @param jobId
     * @param listener
     * @param collector
     * @return
     */
    public GetJobOutputResult InventoryDownload(String vaultName, String jobId, ProgressListener listener, RequestMetricCollector collector) {

	GetJobOutputRequest request = new GetJobOutputRequest().withVaultName(vaultName).withJobId(jobId);

	if (null != listener) {
	    request.withGeneralProgressListener(listener);
	}

	if (null != collector) {
	    request.withRequestMetricCollector(collector);
	}

	return client.getJobOutput(request);

    }

    /**
     * Initiate an inventory retrieval job
     * 
     * @param vaultName
     * @return
     */
    public InitiateJobResult InventoryRetrieval(String vaultName) {
	return InventoryRetrieval(vaultName, null, null, null);
    }

    /**
     * Initiate an inventory retrieval job with an SNS topic
     * 
     * @param vaultName
     * @param topicSNS
     * @param listener
     * @param collector
     * 
     * @return
     */
    public InitiateJobResult InventoryRetrieval(String vaultName, String topicSNS, ProgressListener listener,
	    RequestMetricCollector collector) {

	JobParameters jobParameters = new JobParameters().withType("inventory-retrieval");

	if (null != topicSNS) {
	    jobParameters.withSNSTopic(topicSNS);
	}

	InitiateJobRequest request = new InitiateJobRequest().withVaultName(vaultName).withJobParameters(jobParameters);

	if (null != listener) {
	    request.withGeneralProgressListener(listener);
	}

	if (null != collector) {
	    request.withRequestMetricCollector(collector);
	}

	InitiateJobResult response = this.client.initiateJob(request);

	return response;

    }

    /**
     * List all multipart uploads for a vault
     * 
     * @param vaultName
     * @return
     */
    public List<UploadListElement> ListMultipartUploads(String vaultName) {

	String marker = null;
	List<UploadListElement> list = new ArrayList<UploadListElement>();

	do {

	    ListMultipartUploadsRequest request = new ListMultipartUploadsRequest().withVaultName(vaultName).withUploadIdMarker(marker);

	    ListMultipartUploadsResult result = this.client.listMultipartUploads(request);

	    list.addAll(result.getUploadsList());

	    marker = result.getMarker();

	} while (marker != null);

	return list;

    }

    /**
     * Get's a list of vault jobs
     * 
     * @param vaultName
     * @return
     */
    public List<GlacierJobDescription> ListVaultJobs(String vaultName) {

	String marker = null;
	List<GlacierJobDescription> list = new ArrayList<GlacierJobDescription>();

	do {

	    ListJobsRequest request = new ListJobsRequest().withVaultName(vaultName).withMarker(marker);

	    ListJobsResult result = this.client.listJobs(request);

	    list.addAll(result.getJobList());

	    marker = result.getMarker();

	} while (marker != null);

	return list;

    }

    /**
     * Get a list of vaults available in the region
     * 
     * @return
     */
    public List<DescribeVaultOutput> ListVaults() {

	String marker = null;
	List<DescribeVaultOutput> list = new ArrayList<DescribeVaultOutput>();

	do {

	    ListVaultsRequest request = new ListVaultsRequest().withMarker(marker);

	    ListVaultsResult listVaultsResult = client.listVaults(request);

	    list.addAll(listVaultsResult.getVaultList());

	    marker = listVaultsResult.getMarker();

	} while (marker != null);

	return list;

    }
}

package com.matoski.glacier.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Principal;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.Statement.Effect;
import com.amazonaws.auth.policy.actions.SQSActions;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.glacier.AmazonGlacierClient;
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
    public AmazonGlacierBaseUtil(BasicAWSCredentials credentials,
	    AmazonGlacierClient client, Region region) {
	super();
	this.credentials = credentials;
	this.client = client;
	this.region = region;
	this.hasHttpEndpoint = this.region.hasHttpEndpoint(SERVICE_NAME);
	this.hasHttpsEndpoint = this.region.hasHttpsEndpoint(SERVICE_NAME);
	this.clientEndpoint = this.region.getServiceEndpoint(SERVICE_NAME);

	this.sqsClient = new AmazonSQSClient(credentials);
	this.sqsClient.setRegion(region);
	this.sqsClient.setEndpoint(this.region
		.getServiceEndpoint(SERVICE_SQS_NAME));

	this.snsClient = new AmazonSNSClient(credentials);
	this.snsClient.setRegion(region);
	this.snsClient.setEndpoint(this.region
		.getServiceEndpoint(SERVICE_SNS_NAME));

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
    public AmazonGlacierBaseUtil(String accessKey, String secretKey,
	    String region) throws RegionNotSupportedException {

	ClientConfiguration clientConfiguration = new ClientConfiguration();
	clientConfiguration.setConnectionTimeout(70 * 1000);

	this.region = Region.getRegion(Regions.fromName(region));
	this.credentials = new BasicAWSCredentials(accessKey, secretKey);
	this.client = new AmazonGlacierClient(this.credentials,
		clientConfiguration);

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
	this.sqsClient.setEndpoint(this.region
		.getServiceEndpoint(SERVICE_SQS_NAME));

	this.snsClient = new AmazonSNSClient(credentials);
	this.snsClient.setRegion(this.region);
	this.snsClient.setEndpoint(this.region
		.getServiceEndpoint(SERVICE_SNS_NAME));
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

	CreateTopicRequest request = new CreateTopicRequest()
		.withName(SNS_TOPIC_NAME);
	CreateTopicResult result = snsClient.createTopic(request);
	snsTopicArn = result.getTopicArn();

	SubscribeRequest subscribeRequest = new SubscribeRequest()
		.withTopicArn(snsTopicArn).withEndpoint(sqsQueueArn)
		.withProtocol("sqs");

	SubscribeResult subscribeResult = snsClient.subscribe(subscribeRequest);

	snsSubscriptionArn = subscribeResult.getSubscriptionArn();

    }

    /**
     * Initialize SQS
     */
    final protected void initSQS() {

	CreateQueueRequest request = new CreateQueueRequest()
		.withQueueName(SQS_QUEUE_NAME);
	CreateQueueResult result = sqsClient.createQueue(request);
	sqsQueueUrl = result.getQueueUrl();

	GetQueueAttributesRequest qRequest = new GetQueueAttributesRequest()
		.withQueueUrl(sqsQueueUrl).withAttributeNames("QueueArn");

	GetQueueAttributesResult qResult = sqsClient
		.getQueueAttributes(qRequest);
	sqsQueueArn = qResult.getAttributes().get("QueueArn");

	Policy sqsPolicy = new Policy().withStatements(new Statement(
		Effect.Allow).withPrincipals(Principal.AllUsers)
		.withActions(SQSActions.SendMessage)
		.withResources(new Resource(sqsQueueArn)));
	Map<String, String> queueAttributes = new HashMap<String, String>();
	queueAttributes.put("Policy", sqsPolicy.toJson());
	sqsClient.setQueueAttributes(new SetQueueAttributesRequest(sqsQueueUrl,
		queueAttributes));

    }

}

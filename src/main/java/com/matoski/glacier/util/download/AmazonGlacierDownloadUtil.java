package com.matoski.glacier.util.download;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.InitiateJobRequest;
import com.amazonaws.services.glacier.model.InitiateJobResult;
import com.amazonaws.services.glacier.model.JobParameters;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.util.AmazonGlacierBaseUtil;

/**
 * Amazon Glacier helper utilities
 * 
 * Contains utilities for download archives, initiation of multipart download,
 * canceling of multipart downloads
 * 
 * @author ilijamt
 */
public class AmazonGlacierDownloadUtil extends AmazonGlacierBaseUtil {

    /**
     * Constructor
     * 
     * @param credentials
     * @param client
     * @param region
     */
    public AmazonGlacierDownloadUtil(BasicAWSCredentials credentials, AmazonGlacierClient client, Region region) {
	super(credentials, client, region);
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
    public AmazonGlacierDownloadUtil(String accessKey, String secretKey, String region) throws RegionNotSupportedException {
	super(accessKey, secretKey, region);
    }

    /**
     * Initiate a download request for an archive
     * 
     * @param vaultName
     * @param archiveId
     * @return
     */
    public InitiateJobResult InitiateDownloadRequest(String vaultName, String archiveId) {
	return InitiateDownloadRequest(vaultName, archiveId, null);
    }

    /**
     * Initiate a download request for an archive
     * 
     * @param vaultName
     * @param archiveId
     * @param snsTopicARN
     * @return
     */
    public InitiateJobResult InitiateDownloadRequest(String vaultName, String archiveId, String snsTopicARN) {

	JobParameters job = new JobParameters().withType("archive-retrieval").withArchiveId(archiveId);

	if (null != snsTopicARN) {
	    job.withSNSTopic(snsTopicARN);
	}

	InitiateJobRequest request = new InitiateJobRequest().withVaultName(vaultName).withJobParameters(job);

	return client.initiateJob(request);

    }
}

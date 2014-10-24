package com.matoski.glacier.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.metrics.RequestMetricCollector;
import com.amazonaws.regions.Region;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.TreeHashGenerator;
import com.amazonaws.services.glacier.model.AbortMultipartUploadRequest;
import com.amazonaws.services.glacier.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.glacier.model.CompleteMultipartUploadResult;
import com.amazonaws.services.glacier.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.glacier.model.InitiateMultipartUploadResult;
import com.amazonaws.services.glacier.model.UploadArchiveRequest;
import com.amazonaws.services.glacier.model.UploadArchiveResult;

/**
 * Amazon Glacier helper utilities
 * 
 * Contains utilities for uploading archives, initiation of multipart uploads,
 * canceling of multipart uploads,
 * 
 * @author ilijamt
 */
public class AmazonGlacierUploadUtil extends AmazonGlacierBaseUtil {

    /**
     * Constructor
     * 
     * @param credentials
     * @param client
     * @param region
     */
    public AmazonGlacierUploadUtil(BasicAWSCredentials credentials,
	    AmazonGlacierClient client, Region region) {
	super(credentials, client, region);
    }

    /**
     * Upload an archive in a single request
     * 
     * @param file
     * @param vaultName
     * @param archiveDescription
     * @param listener
     * @param collector
     * 
     * @return
     * 
     * @throws FileNotFoundException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public UploadArchiveResult SingleUpload(File file, String vaultName,
	    String archiveDescription, ProgressListener listener,
	    RequestMetricCollector collector) throws FileNotFoundException,
	    AmazonServiceException, AmazonClientException {

	InputStream body = new FileInputStream(file);
	String checksum = TreeHashGenerator.calculateTreeHash(file);
	long fileSize = file.length();

	UploadArchiveRequest request = new UploadArchiveRequest()
		.withVaultName(vaultName)
		.withArchiveDescription(archiveDescription)
		.withChecksum(checksum).withBody(body)
		.withContentLength(fileSize);

	if (null != listener) {
	    request.withGeneralProgressListener(listener);
	}

	if (null != collector) {
	    request.withRequestMetricCollector(collector);
	}

	return this.client.uploadArchive(request);
    }

    /**
     * Initiate a multipart upload
     * 
     * @param vaultName
     * @param archiveDescription
     * @param partSize
     * 
     * @return
     * 
     * @throws AmazonClientException
     * @throws AmazonServiceException
     */
    public InitiateMultipartUploadResult InitiateMultipartUpload(
	    String vaultName, String archiveDescription, String partSize)
	    throws AmazonClientException, AmazonServiceException {
	return this.InitiateMultipartUpload(vaultName, archiveDescription,
		partSize, null, null);
    }

    /**
     * Initiate a multipart upload
     * 
     * @param vaultName
     * @param archiveDescription
     * @param partSize
     * @param listener
     * @param collector
     * 
     * @return
     * 
     * @throws AmazonClientException
     * @throws AmazonServiceException
     */
    public InitiateMultipartUploadResult InitiateMultipartUpload(
	    String vaultName, String archiveDescription, String partSize,
	    ProgressListener listener, RequestMetricCollector collector)
	    throws AmazonClientException, AmazonServiceException {

	InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest()
		.withVaultName(vaultName)
		.withArchiveDescription(archiveDescription)
		.withPartSize(partSize);

	if (null != listener) {
	    request.withGeneralProgressListener(listener);
	}

	if (null != collector) {
	    request.withRequestMetricCollector(collector);
	}

	return this.client.initiateMultipartUpload(request);
    }

    /**
     * Complete a multipart upload
     * 
     * @param fileSize
     * @param uploadId
     * @param vaultName
     * @param checksum
     * 
     * @return
     * 
     * @throws AmazonClientException
     * @throws AmazonServiceException
     */
    public CompleteMultipartUploadResult CompleteMultipartUpload(long fileSize,
	    String uploadId, String vaultName, String checksum)
	    throws AmazonClientException, AmazonServiceException {
	return this.CompleteMultipartUpload(fileSize, uploadId, vaultName,
		checksum, null, null);
    }

    /**
     * Complete a multipart upload
     * 
     * @param fileSize
     * @param uploadId
     * @param vaultName
     * @param checksum
     * @param listener
     * 
     * @return
     * 
     * @throws AmazonClientException
     * @throws AmazonServiceException
     */
    public CompleteMultipartUploadResult CompleteMultipartUpload(long fileSize,
	    String uploadId, String vaultName, String checksum,
	    ProgressListener listener, RequestMetricCollector collector)
	    throws AmazonClientException, AmazonServiceException {

	CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest()
		.withVaultName(vaultName).withUploadId(uploadId)
		.withChecksum(checksum)
		.withArchiveSize(String.valueOf(fileSize));

	if (null != listener) {
	    request.withGeneralProgressListener(listener);
	}

	if (null != collector) {
	    request.withRequestMetricCollector(collector);
	}

	return this.client.completeMultipartUpload(request);

    }

    /**
     * Cancel the multipart upload
     * 
     * @param uploadId
     * @param vaultName
     * 
     * @return
     */
    public Boolean CancelMultipartUpload(String uploadId, String vaultName) {
	return this.CancelMultipartUpload(uploadId, vaultName, null, null);
    }

    /**
     * Cancel the multipart upload
     * 
     * @param uploadId
     * @param vaultName
     * @param listener
     * @param collector
     * 
     * @return
     */
    public Boolean CancelMultipartUpload(String uploadId, String vaultName,
	    ProgressListener listener, RequestMetricCollector collector) {

	Boolean valid = false;

	AbortMultipartUploadRequest request = new AbortMultipartUploadRequest()
		.withUploadId(uploadId).withVaultName(vaultName);

	if (null != listener) {
	    request.withGeneralProgressListener(listener);
	}

	if (null != collector) {
	    request.withRequestMetricCollector(collector);
	}

	try {
	    this.client.abortMultipartUpload(request);
	    valid = true;
	} catch (AmazonServiceException e) {
	    valid = false;
	}

	return valid;

    }

}

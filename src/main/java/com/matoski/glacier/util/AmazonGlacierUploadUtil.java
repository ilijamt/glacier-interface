package com.matoski.glacier.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
import com.amazonaws.services.glacier.model.UploadMultipartPartRequest;
import com.amazonaws.services.glacier.model.UploadMultipartPartResult;
import com.amazonaws.util.BinaryUtils;
import com.matoski.glacier.enums.Metadata;
import com.matoski.glacier.enums.UploadMultipartStatus;
import com.matoski.glacier.errors.InvalidUploadedChecksumException;
import com.matoski.glacier.errors.UploadTooManyPartsException;
import com.matoski.glacier.pojo.Archive;
import com.matoski.glacier.pojo.UploadPiece;

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

	InputStream stream = new FileInputStream(file);
	String checksum = TreeHashGenerator.calculateTreeHash(file);
	long fileSize = file.length();

	UploadArchiveRequest request = new UploadArchiveRequest()
		.withVaultName(vaultName)
		.withArchiveDescription(archiveDescription)
		.withChecksum(checksum).withBody(stream)
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
	    String vaultName, String archiveDescription, long partSize)
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
	    String vaultName, String archiveDescription, long partSize,
	    ProgressListener listener, RequestMetricCollector collector)
	    throws AmazonClientException, AmazonServiceException {

	InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest()
		.withVaultName(vaultName)
		.withArchiveDescription(archiveDescription)
		.withPartSize(String.valueOf(partSize));

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

    public Archive UploadMultipartFile(File file, int partSize,
	    String vaultName, Metadata metadata)
	    throws UploadTooManyPartsException {

	Archive archive = new Archive();
	long fileSize = file.length();
	UploadPiece piece = null;
	int pieces = (int) Math.ceil(fileSize / (double) partSize);
	List<byte[]> checksums = new LinkedList<byte[]>();

	if (!isValidMaxParts(file, partSize)) {
	    throw new UploadTooManyPartsException();
	}

	final AmazonGlacierProgressBar bar = new AmazonGlacierProgressBar();
	bar.setTotal(fileSize);

	// 1. Initiate MultiPart Upload

	this.InitiateMultipartUpload(vaultName, Parser.getParser(metadata)
		.encode(archive), partSize);

	// 2. Upload Pieces

	// 3. Finish MultiPart Upload

	// UploadMultipartStatus.COMPLETE;

	return archive;
    }

    /**
     * Upload a piece of the file to amazon glacier
     * 
     * @param file
     * @param part
     * @param partSize
     * @param vaultName
     * @param uploadId
     * 
     * @return
     * 
     * @throws AmazonServiceException
     * @throws NoSuchAlgorithmException
     * @throws AmazonClientException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public UploadPiece UploadMultipartPiece(File file, int part, int partSize,
	    String vaultName, String uploadId) throws AmazonServiceException,
	    NoSuchAlgorithmException, AmazonClientException,
	    FileNotFoundException, IOException,
	    InvalidUploadedChecksumException {
	return UploadMultipartPiece(file, part, partSize, vaultName, uploadId,
		null, null);
    }

    /**
     * Upload a piece of the file to amazon glacier
     * 
     * @param file
     * @param part
     * @param partSize
     * @param vaultName
     * @param uploadId
     * @param listener
     * @param collector
     * 
     * @return
     * 
     * @throws AmazonServiceException
     * @throws NoSuchAlgorithmException
     * @throws AmazonClientException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public UploadPiece UploadMultipartPiece(File file, int part, int partSize,
	    String vaultName, String uploadId, ProgressListener listener,
	    RequestMetricCollector collector) throws AmazonServiceException,
	    NoSuchAlgorithmException, AmazonClientException,
	    FileNotFoundException, IOException {

	UploadPiece ret = new UploadPiece();

	ret.setPart(part);
	ret.setStatus(UploadMultipartStatus.PIECE_START);

	byte[] buffer = new byte[partSize];
	FileInputStream stream = new FileInputStream(file);

	int position = part * (int) partSize;
	stream.skip(position);
	int read = stream.read(buffer);
	stream.close();

	if (read == -1) {
	    ret.setStatus(UploadMultipartStatus.PIECE_INVALID_PART);
	    return ret;
	}

	String contentRange = String.format("bytes %s-%s/*", position, position
		+ read - 1);
	String checksum = TreeHashGenerator
		.calculateTreeHash(new ByteArrayInputStream(buffer));

	ret.setCalculatedChecksum(checksum);

	UploadMultipartPartRequest request = new UploadMultipartPartRequest()
		.withVaultName(vaultName)
		.withBody(new ByteArrayInputStream(buffer))
		.withChecksum(checksum).withRange(contentRange)
		.withUploadId(uploadId);

	if (null != listener) {
	    request.withGeneralProgressListener(listener);
	}

	if (null != collector) {
	    request.withRequestMetricCollector(collector);
	}

	UploadMultipartPartResult result = client.uploadMultipartPart(request);

	ret.setUploadedChecksum(result.getChecksum());

	if (ret.getCalculatedChecksum().equals(ret.getUploadedChecksum())) {
	    ret.setStatus(UploadMultipartStatus.PIECE_COMPLETE);
	} else {
	    ret.setStatus(UploadMultipartStatus.PIECE_CHECKSUM_MISMATCH);
	}

	return ret;
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

package com.matoski.glacier.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.UploadTooManyPartsException;
import com.matoski.glacier.pojo.Archive;
import com.matoski.glacier.pojo.MultipartUploadStatus;
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
     * Constructor
     * 
     * @param accessKey
     * @param secretKey
     * @param region
     * 
     * @throws RegionNotSupportedException
     */
    public AmazonGlacierUploadUtil(String accessKey, String secretKey,
	    String region) throws RegionNotSupportedException {
	super(accessKey, secretKey, region);
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

    public Archive UploadMultipartFile(File file, int threads, int retry,
	    int partSize, String vaultName, Metadata metadata,
	    boolean doNotComplete) throws UploadTooManyPartsException {

	ExecutorService pool = Executors.newFixedThreadPool(threads);
	HashMap<Integer, Future<UploadPiece>> map = new HashMap<Integer, Future<UploadPiece>>();

	Archive archive = new Archive();
	long fileSize = file.length();
	int pieces = (int) Math.ceil(fileSize / (double) partSize);

	if (!isValidMaxParts(file, partSize)) {
	    throw new UploadTooManyPartsException();
	}

	archive.setCreatedDate(new Date());

	// 0. Get the upload state file
	final MultipartUploadStatus uploadStatus = new MultipartUploadStatus(
		pieces);
	uploadStatus.setPartSize(partSize);
	uploadStatus.setParts(pieces);

	// 1. Initiate MultiPart Upload
	InitiateMultipartUploadResult initiate = this
		.InitiateMultipartUpload(vaultName, Parser.getParser(metadata)
			.encode(archive), partSize);

	uploadStatus.setFile(file);
	uploadStatus.setId(initiate.getUploadId());

	try {
	    uploadStatus.write();
	} catch (NullPointerException | IOException e) {
	    System.err.println(String.format("ERROR: %s", e.getMessage()));
	}

	// 2. Upload Pieces
	for (int i = 0; i < pieces; i++) {
	    try {

		map.put(i, pool.submit(new ThreadAmazonGlacierUploadUtil(retry,
			file, pieces, i, partSize, vaultName, initiate
				.getUploadId(),
			credentials.getAWSAccessKeyId(), credentials
				.getAWSSecretKey(), region.getName())));

	    } catch (AmazonClientException | RegionNotSupportedException e) {
		System.err.println(String.format("ERROR: %s", e.getMessage()));
		map.put(i, null);
	    }

	}
	pool.shutdown();

	try {
	    while (!pool.awaitTermination(24L, TimeUnit.HOURS)) {
		System.out.println("Still waiting for the executor to finish");
	    }
	} catch (InterruptedException e) {
	    System.err.println(String.format("ERROR: %s", e.getMessage()));
	}

	// contains a list of the parts, with the required checksum for each
	// part, we use this to calculate the whole checksum
	HashMap<Integer, String> state = new HashMap<>();
	UploadPiece piece = null;
	Future<UploadPiece> future;

	// Iterate through all of them
	for (Entry<Integer, Future<UploadPiece>> entry : map.entrySet()) {
	    future = entry.getValue();
	    try {
		if (future.isDone() && !future.isCancelled()) {
		    piece = entry.getValue().get();
		    uploadStatus.addPiece(piece);
		    state.put(piece.getPart(), piece.getCalculatedChecksum());
		}
	    } catch (InterruptedException | ExecutionException | IOException e) {
		System.err.println(String.format("ERROR: %s", e.getMessage()));
	    }
	}

	// 3. Go through the future to check the data and calculate the output
	// all the items are finished now, let's calculate the checksum
	List<byte[]> checksums = new LinkedList<byte[]>();
	for (Entry<Integer, String> entry : state.entrySet()) {
	    checksums.add(BinaryUtils.fromHex(entry.getValue()));
	}

	// process the upload state
	uploadStatus.isFinished();

	if (doNotComplete) {
	    this.CancelMultipartUpload(initiate.getUploadId(), vaultName);
	} else {
	    // 3. Finish MultiPart Upload
	    CompleteMultipartUploadResult complete = this
		    .CompleteMultipartUpload(fileSize, initiate.getUploadId(),
			    vaultName,
			    TreeHashGenerator.calculateTreeHash(checksums));

	    archive.setHash(complete.getChecksum());
	    archive.setId(complete.getArchiveId());
	    archive.setUri(complete.getLocation());

	}

	archive.setSize(fileSize);
	archive.setName(file.toString());
	archive.setModifiedDate(file.lastModified());

	return archive;
    }

    /**
     * Upload a piece of the file to amazon glacier
     * 
     * @param file
     * @param pieces
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
    public UploadPiece UploadMultipartPiece(File file, int pieces, int part,
	    int partSize, String vaultName, String uploadId)
	    throws AmazonServiceException, NoSuchAlgorithmException,
	    AmazonClientException, FileNotFoundException, IOException,
	    InvalidUploadedChecksumException {
	return UploadMultipartPiece(file, pieces, part, partSize, vaultName,
		uploadId, null, null);
    }

    /**
     * Upload a piece of the file to amazon glacier
     * 
     * @param file
     * @param pieces
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
    public UploadPiece UploadMultipartPiece(File file, int pieces, int part,
	    int partSize, String vaultName, String uploadId,
	    ProgressListener listener, RequestMetricCollector collector)
	    throws AmazonServiceException, NoSuchAlgorithmException,
	    AmazonClientException, FileNotFoundException, IOException {

	UploadPiece ret = new UploadPiece();

	ret.setPart(part);
	ret.setId(uploadId);
	ret.setStatus(UploadMultipartStatus.PIECE_START);

	int bufferSize = partSize;

	FileInputStream stream = new FileInputStream(file);

	int position = part * (int) partSize;
	stream.skip(position);

	if (part > pieces) {
	    ret.setStatus(UploadMultipartStatus.PIECE_INVALID_PART);
	    stream.close();
	    return ret;
	} else if (part == (pieces - 1)) {
	    bufferSize = (int) (file.length() - position);
	}

	byte[] buffer = new byte[bufferSize];
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

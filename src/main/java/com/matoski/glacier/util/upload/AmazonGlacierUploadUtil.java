package com.matoski.glacier.util.upload;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.FileUtils;

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
import com.amazonaws.services.glacier.model.DescribeVaultOutput;
import com.amazonaws.services.glacier.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.glacier.model.InitiateMultipartUploadResult;
import com.amazonaws.services.glacier.model.ListMultipartUploadsRequest;
import com.amazonaws.services.glacier.model.ListMultipartUploadsResult;
import com.amazonaws.services.glacier.model.ListPartsRequest;
import com.amazonaws.services.glacier.model.ListPartsResult;
import com.amazonaws.services.glacier.model.ListVaultsRequest;
import com.amazonaws.services.glacier.model.ListVaultsResult;
import com.amazonaws.services.glacier.model.PartListElement;
import com.amazonaws.services.glacier.model.RequestTimeoutException;
import com.amazonaws.services.glacier.model.UploadArchiveRequest;
import com.amazonaws.services.glacier.model.UploadArchiveResult;
import com.amazonaws.services.glacier.model.UploadListElement;
import com.amazonaws.services.glacier.model.UploadMultipartPartRequest;
import com.amazonaws.services.glacier.model.UploadMultipartPartResult;
import com.matoski.glacier.Constants;
import com.matoski.glacier.enums.Metadata;
import com.matoski.glacier.enums.UploadMultipartStatus;
import com.matoski.glacier.errors.InvalidUploadedChecksumException;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.UploadTooManyPartsException;
import com.matoski.glacier.pojo.Archive;
import com.matoski.glacier.pojo.MultipartUploadStatus;
import com.matoski.glacier.pojo.UploadPiece;
import com.matoski.glacier.util.AmazonGlacierBaseUtil;
import com.matoski.glacier.util.Parser;

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
     * Generic string format
     */
    final public static String FORMAT = "[#%1$05d/#%2$05d] %3$-15s | (%4$s) %5$s";

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
	    int partSize, String vaultName, Metadata metadata)
	    throws UploadTooManyPartsException, IOException,
	    RegionNotSupportedException {

	ExecutorService pool = Executors.newFixedThreadPool(threads);
	HashMap<Integer, Future<UploadPiece>> map = new HashMap<Integer, Future<UploadPiece>>();

	Archive archive = new Archive();
	long fileSize = file.length();
	int pieces = (int) Math.ceil(fileSize / (double) partSize);

	if (!isValidMaxParts(file, partSize)) {
	    throw new UploadTooManyPartsException();
	}

	archive.setCreatedDate(new Date());

	// 0. Get the upload state file, check if we have a state already
	final MultipartUploadStatus uploadStatus;

	if (MultipartUploadStatus.has(file)) {
	    System.out.println(String.format(
		    "Upload state found for %s, loading", file.getName()));
	    uploadStatus = MultipartUploadStatus.load(file);
	} else {
	    uploadStatus = new MultipartUploadStatus();
	}

	String location = null;
	String uploadId = null;

	// 1. Initiate MultiPart Upload
	if (uploadStatus.isInitiated()) {
	    location = uploadStatus.getLocation();
	    uploadId = uploadStatus.getId();
	    System.out.println(String.format(
		    "Upload already initiated with location: %s and id: %s",
		    location, uploadId));

	    if (partSize != uploadStatus.getPartSize()
		    || pieces != uploadStatus.getParts()) {
		pieces = uploadStatus.getParts();
		partSize = uploadStatus.getPartSize();
		System.out.println(String.format(
			"[Overriding] Parts: %s and piece size: %s bytes",
			pieces, partSize));
	    }
	    System.out.println();
	} else {
	    InitiateMultipartUploadResult initiate = this
		    .InitiateMultipartUpload(vaultName,
			    Parser.getParser(metadata).encode(archive),
			    partSize);

	    location = initiate.getLocation();
	    uploadId = initiate.getUploadId();

	    uploadStatus.setStatus(UploadMultipartStatus.START);
	    uploadStatus.setPartSize(partSize);
	    uploadStatus.setParts(pieces);
	    uploadStatus.setInitiated(true);
	    uploadStatus.setFile(file);
	    uploadStatus.setId(uploadId);
	    uploadStatus.setLocation(location);
	    uploadStatus.setStarted(new Date());

	    try {
		uploadStatus.write();
	    } catch (NullPointerException | IOException e) {
		System.err.println(String.format("ERROR: %s", e.getMessage()));
	    }

	    uploadStatus.setStatus(UploadMultipartStatus.IN_PROGRESS);
	}

	Callable<UploadPiece> thread;

	// 2. Upload Pieces
	for (int i = 0; i < pieces; i++) {

	    if (uploadStatus.isPieceCompleted(i)) {

		thread = new ThreadAmazonGlacierUploadDummy(pieces, file,
			uploadStatus.getPiece(i));

	    } else {

		thread = new ThreadAmazonGlacierUploadUtil(retry, file, pieces,
			i, partSize, vaultName, uploadId,
			credentials.getAWSAccessKeyId(),
			credentials.getAWSSecretKey(), region.getName());

	    }

	    map.put(i, pool.submit(thread));

	}

	pool.shutdown();

	UploadPiece piece = null;
	Future<UploadPiece> future;
	Boolean pieceExists = false;

	try {
	    while (!pool.awaitTermination(Constants.WAIT_TIME_THREAD_CHECK,
		    TimeUnit.SECONDS)) {

		for (Entry<Integer, Future<UploadPiece>> entry : map.entrySet()) {

		    future = entry.getValue();

		    try {

			piece = future.get(
				Constants.WAIT_FETCH_OBJECT_FROM_POOL,
				TimeUnit.MILLISECONDS);

			pieceExists = uploadStatus.exists(piece);

			if (!pieceExists && future.isDone()
				&& !future.isCancelled()) {
			    uploadStatus.addPiece(piece);
			}
		    } catch (InterruptedException e) {

		    } catch (ExecutionException e) {

		    } catch (IOException e) {
			System.err.println(String.format("ERROR: %s",
				e.getMessage()));
		    } catch (TimeoutException e) {
			// we skip this item as it has not finished yet we don't
			// do anything here
		    }

		}
	    }
	} catch (InterruptedException e) {
	    System.err.println(String.format("ERROR: %s", e.getMessage()));
	}

	// go through them again, in case we missed them , can happen if it
	// finishes very shortly after completion
	for (Entry<Integer, Future<UploadPiece>> entry : map.entrySet()) {

	    future = entry.getValue();

	    try {

		piece = future.get();

		pieceExists = uploadStatus.exists(piece);

		if (!pieceExists && future.isDone() && !future.isCancelled()) {
		    uploadStatus.addPiece(piece);
		}

	    } catch (InterruptedException | ExecutionException e) {
		System.err.println(String.format("ERROR: %s", e.getMessage()));
	    }

	}

	// process the upload state
	uploadStatus.isFinished();

	// 3. Finish MultiPart Upload
	CompleteMultipartUploadResult complete = this.CompleteMultipartUpload(
		fileSize, uploadId, vaultName, uploadStatus.getFinalChecksum());

	archive.setHash(complete.getChecksum());
	archive.setId(complete.getArchiveId());
	archive.setUri(complete.getLocation());

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
     * @throws RequestTimeoutException
     */
    public UploadPiece UploadMultipartPiece(File file, int pieces, int part,
	    int partSize, String vaultName, String uploadId)
	    throws AmazonServiceException, NoSuchAlgorithmException,
	    AmazonClientException, FileNotFoundException, IOException,
	    InvalidUploadedChecksumException, RequestTimeoutException {
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
     * @throws RequestTimeoutException
     */
    public UploadPiece UploadMultipartPiece(File file, int pieces, int part,
	    int partSize, String vaultName, String uploadId,
	    ProgressListener listener, RequestMetricCollector collector)
	    throws AmazonServiceException, NoSuchAlgorithmException,
	    AmazonClientException, FileNotFoundException, IOException,
	    RequestTimeoutException {

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

	    ListMultipartUploadsRequest request = new ListMultipartUploadsRequest()
		    .withVaultName(vaultName).withUploadIdMarker(marker);

	    ListMultipartUploadsResult result = this.client
		    .listMultipartUploads(request);

	    list.addAll(result.getUploadsList());

	    marker = result.getMarker();

	} while (marker != null);

	return list;

    }

    /**
     * Get a list of the parts in the specified multipart upload id
     * 
     * @param vaultName
     * @param uploadId
     * @return
     */
    public ListPartsResult GetMultipartUploadInfo(String vaultName,
	    String uploadId) {

	String marker = null;
	ListPartsResult response = new ListPartsResult();

	do {

	    ListPartsRequest request = new ListPartsRequest()
		    .withVaultName(vaultName).withUploadId(uploadId)
		    .withMarker(marker);

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

    public List<DescribeVaultOutput> ListVaults() {

	String marker = null;
	List<DescribeVaultOutput> list = new ArrayList<DescribeVaultOutput>();

	do {

	    ListVaultsRequest request = new ListVaultsRequest()
		    .withMarker(marker);

	    ListVaultsResult listVaultsResult = client.listVaults(request);

	    list.addAll(listVaultsResult.getVaultList());

	    marker = listVaultsResult.getMarker();

	} while (marker != null);

	return list;

    }
}

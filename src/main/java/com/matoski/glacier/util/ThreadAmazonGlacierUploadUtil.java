package com.matoski.glacier.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.metrics.RequestMetricCollector;
import com.matoski.glacier.enums.UploadMultipartStatus;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.pojo.UploadPiece;

/**
 * A threaded upload manager
 * 
 * @author ilijamt
 */
public class ThreadAmazonGlacierUploadUtil extends AmazonGlacierUploadUtil
	implements Callable<UploadPiece> {

    /**
     * The file for the upload
     */
    private final File requestFile;

    /**
     * Total pieces
     */
    private final int requestPieces;

    /**
     * The part to upload
     */
    private final int requestPart;

    /**
     * The partition size
     */
    private final int requestPartSize;

    /**
     * The vault name
     */
    private final String requestVaultName;

    /**
     * The upload ID for the request
     */
    private final String requestUploadId;

    /**
     * The progress listener
     */
    private final ProgressListener requestListener;

    /**
     * The request metric collector
     */
    private final RequestMetricCollector requestCollector;

    /**
     * How many times to retry uploading a failed upload, either it is an error,
     * or a {@link UploadMultipartStatus#PIECE_CHECKSUM_MISMATCH} is present in
     * the result
     */
    private final int requestRetryFailedUploads;

    /**
     * Constructor
     * 
     * @param retryFailedUploads
     * @param file
     * @param pieces
     * @param part
     * @param partSize
     * @param vaultName
     * @param uploadId
     * @param accessKey
     * @param secretKey
     * @param region
     * @param listener
     * @param collector
     * 
     * @throws RegionNotSupportedException
     */
    public ThreadAmazonGlacierUploadUtil(int retryFailedUploads, File file,
	    int pieces, int part, int partSize, String vaultName,
	    String uploadId, String accessKey, String secretKey, String region,
	    ProgressListener listener, RequestMetricCollector collector)
	    throws RegionNotSupportedException {
	super(accessKey, secretKey, region);
	this.requestRetryFailedUploads = retryFailedUploads;
	this.requestFile = file;
	this.requestPieces = pieces;
	this.requestPart = part;
	this.requestPartSize = partSize;
	this.requestVaultName = vaultName;
	this.requestUploadId = uploadId;
	this.requestListener = listener;
	this.requestCollector = collector;
    }

    /**
     * Constructor
     * 
     * @param retryFailedUploads
     * @param file
     * @param pieces
     * @param part
     * @param partSize
     * @param vaultName
     * @param uploadId
     * @param accessKey
     * @param secretKey
     * @param region
     * 
     * @throws RegionNotSupportedException
     */
    public ThreadAmazonGlacierUploadUtil(int retryFailedUploads, File file,
	    int pieces, int part, int partSize, String vaultName,
	    String uploadId, String accessKey, String secretKey, String region)
	    throws RegionNotSupportedException {
	super(accessKey, secretKey, region);
	this.requestRetryFailedUploads = retryFailedUploads;
	this.requestFile = file;
	this.requestPieces = pieces;
	this.requestPart = part;
	this.requestPartSize = partSize;
	this.requestVaultName = vaultName;
	this.requestUploadId = uploadId;
	this.requestCollector = null;
	this.requestListener = null;
    }

    /**
     * Upload
     * 
     * @param time
     * 
     * @throws AmazonServiceException
     * @throws NoSuchAlgorithmException
     * @throws AmazonClientException
     * @throws FileNotFoundException
     * @throws IOException
     * 
     * @return
     */
    private UploadPiece upload(int time) throws AmazonServiceException,
	    NoSuchAlgorithmException, AmazonClientException,
	    FileNotFoundException, IOException {

	return this.UploadMultipartPiece(requestFile, requestPieces,
		requestPart, requestPartSize, requestVaultName,
		requestUploadId, requestListener, requestCollector);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UploadPiece call() throws Exception {
	UploadPiece piece = null;
	int count = 0;

	for (int i = 0; i < requestRetryFailedUploads; i++) {

	    try {

		piece = this.upload(count);

		if (piece.getStatus() == UploadMultipartStatus.PIECE_COMPLETE) {
		    // we have successfully uploaded the file, so we break now,
		    // no need to continue trying to re-upload the part again
		    break;
		}

	    } catch (NoSuchAlgorithmException | AmazonClientException
		    | IOException e) {
		throw e;
	    }

	}

	System.out.println(String.format("Uploaded piece: %s/%s", requestPart,
		requestPieces));

	return piece;
    }

}

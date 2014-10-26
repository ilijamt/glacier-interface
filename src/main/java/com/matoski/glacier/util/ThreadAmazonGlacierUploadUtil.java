package com.matoski.glacier.util;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.metrics.RequestMetricCollector;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.interfaces.IUploadPieceHandler;
import com.matoski.glacier.pojo.UploadPiece;

/**
 * A threaded upload manager
 * 
 * @author ilijamt
 */
public class ThreadAmazonGlacierUploadUtil extends AmazonGlacierUploadUtil implements
	Runnable {

    /**
     * The file for the upload
     */
    private File requestFile;

    /**
     * Total pieces
     */
    private int requestPieces;

    /**
     * The part to upload
     */
    private int requestPart;

    /**
     * The partition size
     */
    private int requestPartSize;

    /**
     * The vault name
     */
    private String requestVaultName;

    /**
     * The upload ID for the request
     */
    private String requestUploadId;

    /**
     * The progress listener
     */
    private ProgressListener requestListener;

    /**
     * The request metric collector
     */
    private RequestMetricCollector requestCollector;

    /**
     * Constructor
     * 
     * @param accessKey
     * @param secretKey
     * @param region
     * 
     * @throws RegionNotSupportedException
     */
    public ThreadAmazonGlacierUploadUtil(File file, int pieces, int part, int partSize,
	    String vaultName, String uploadId, String accessKey,
	    String secretKey, String region, ProgressListener listener,
	    RequestMetricCollector collector)
	    throws RegionNotSupportedException {
	super(accessKey, secretKey, region);
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
     * @param accessKey
     * @param secretKey
     * @param region
     * 
     * @throws RegionNotSupportedException
     */
    public ThreadAmazonGlacierUploadUtil(File file, int pieces, int part, int partSize,
	    String vaultName, String uploadId, String accessKey,
	    String secretKey, String region) throws RegionNotSupportedException {
	super(accessKey, secretKey, region);
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
     * Set a handler
     * 
     * @param handler
     */
    public void setHandler(IUploadPieceHandler handler) {
	this.handler = handler;
    }

    /**
     * A generic handler, used to notify it's progress
     */
    private IUploadPieceHandler handler;

    /**
     * Has handler
     * 
     * @return
     */
    public Boolean hasHandler() {
	return null != handler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

	UploadPiece piece = null;

	if (hasHandler()) {
	    handler.start();
	}

	try {
	    piece = this.UploadMultipartPiece(requestFile, requestPieces,
		    requestPart, requestPartSize, requestVaultName,
		    requestUploadId, requestListener, requestCollector);
	} catch (NoSuchAlgorithmException | AmazonClientException | IOException e) {
	    if (hasHandler()) {
		handler.exception(e);
	    } else {
		e.printStackTrace();
	    }
	}

	if (hasHandler()) {
	    handler.end(requestPart, piece);
	}

    }

}

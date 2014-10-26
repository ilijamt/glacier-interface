package com.matoski.glacier.util;

import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.interfaces.IUploadPieceHandler;

/**
 * A threaded upload manager
 * 
 * @author ilijamt
 */
public class ThreadUploadPiece extends AmazonGlacierBaseUtil implements
	Runnable {

    /**
     * Constructor
     * 
     * @param accessKey
     * @param secretKey
     * @param region
     * @throws RegionNotSupportedException
     */
    public ThreadUploadPiece(String accessKey, String secretKey, String region)
	    throws RegionNotSupportedException {
	super(accessKey, secretKey, region);
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

	if (hasHandler()) {
	    handler.start();
	}

	if (hasHandler()) {
	    handler.end(0, null);
	}

    }

}

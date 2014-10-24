package com.matoski.glacier.util;

import java.io.File;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.glacier.AmazonGlacierClient;

public abstract class AmazonGlacierBaseUtil {

    /**
     * The maximum part size, in bytes, for a Glacier multipart upload.
     */
    private static final long MAXIMUM_UPLOAD_PARTS = 1000L * 10;

    /**
     * The maximum part size, in bytes, for a Glacier multipart upload.
     */
    private static final long MAXIMUM_UPLOAD_PART_SIZE = 1024L * 1024 * 1024 * 4;

    /**
     * The default chunk size, in bytes, when downloading in multiple chunks
     * using range retrieval.
     */
    private static final long DEFAULT_DOWNLOAD_CHUNK_SIZE = 1024L * 1024 * 128;

    /**
     * The minimum part size, in bytes, for a Glacier multipart upload.
     */
    private static final long MINIMUM_PART_SIZE = 1024L * 1024;

    /**
     * Threshold, in bytes, for when to use the multipart upload operations
     */
    private static final long MULTIPART_UPLOAD_SIZE_THRESHOLD = 1024L * 1024L * 100;

    /**
     * The credentials for Amazon Glacier
     */
    protected BasicAWSCredentials credentials = null;

    /**
     * The client for Amazon Glacier
     */
    protected AmazonGlacierClient client = null;

    /**
     * The region for Amazon Glacier
     */
    protected Region region = null;

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
    }

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
}

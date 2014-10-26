package com.matoski.glacier.util;

import java.io.File;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.matoski.glacier.commands.AbstractCommand;
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
	this.hasHttpEndpoint = this.region
		.hasHttpEndpoint(AbstractCommand.SERVICE_NAME);
	this.hasHttpsEndpoint = this.region
		.hasHttpsEndpoint(AbstractCommand.SERVICE_NAME);
	this.clientEndpoint = this.region
		.getServiceEndpoint(AbstractCommand.SERVICE_NAME);
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

	this.region = Region.getRegion(Regions.fromName(region));
	this.credentials = new BasicAWSCredentials(accessKey, secretKey);
	this.client = new AmazonGlacierClient(this.credentials);

	if (!this.region.isServiceSupported(AbstractCommand.SERVICE_NAME)) {
	    throw new RegionNotSupportedException();
	}

	this.hasHttpEndpoint = this.region
		.hasHttpEndpoint(AbstractCommand.SERVICE_NAME);
	this.hasHttpsEndpoint = this.region
		.hasHttpsEndpoint(AbstractCommand.SERVICE_NAME);
	this.clientEndpoint = this.region
		.getServiceEndpoint(AbstractCommand.SERVICE_NAME);

	this.client.setRegion(this.region);
	this.client.setEndpoint(this.clientEndpoint);

    }
}

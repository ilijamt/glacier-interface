package com.matoski.glacier.commands;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.interfaces.ICommand;
import com.matoski.glacier.pojo.Config;

/**
 * A generic abstract command used as a base for all the commands related to
 * Amazon Glacier.
 * 
 * @author ilijamt
 * @param <T>
 */
public abstract class AbstractCommand<T> implements ICommand, Runnable {

    /**
     * Basic AWS credentials
     */
    protected BasicAWSCredentials credentials = null;

    /**
     * The client used to connect to amazon glacier
     */
    protected AmazonGlacierClient client = null;

    /**
     * The configuration
     */
    protected Config config = null;

    /**
     * The region used for all operations with Amazon Glacier
     */
    protected Region region = null;

    /**
     * The specific command for the command in question
     */
    protected T command;

    /**
     * Service name
     */
    public static String SERVICE_NAME = "glacier";

    /**
     * Constructor
     * 
     * @param config
     * 
     * @throws VaultNameNotPresentException
     * @throws RegionNotSupportedException
     */
    @SuppressWarnings("unused")
    public AbstractCommand(Config config, T command)
	    throws VaultNameNotPresentException, RegionNotSupportedException {

	this.command = command;
	this.config = config;
	this.region = Region.getRegion(Regions.fromName(config.getRegion()));
	this.credentials = new BasicAWSCredentials(config.getKey(),
		config.getSecretKey());
	ClientConfiguration configuration = new ClientConfiguration();
	configuration.setConnectionTimeout(70 * 1000);
	this.client = new AmazonGlacierClient(this.credentials, configuration);

	if (!this.region.isServiceSupported(AbstractCommand.SERVICE_NAME)) {
	    throw new RegionNotSupportedException();
	}

	Boolean hasHttpEndpoint = this.region.hasHttpEndpoint(SERVICE_NAME);
	Boolean hasHttpsEndpoint = this.region.hasHttpsEndpoint(SERVICE_NAME);
	String endpoint = this.region.getServiceEndpoint(SERVICE_NAME);

	this.client.setRegion(region);
	this.client.setEndpoint(endpoint);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean valid() {
	return true;
    }
}

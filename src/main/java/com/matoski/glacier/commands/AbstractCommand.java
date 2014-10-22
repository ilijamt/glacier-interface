package com.matoski.glacier.commands;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.glacier.AmazonGlacierAsyncClient;
import com.matoski.glacier.interfaces.ICommand;
import com.matoski.glacier.pojo.Config;

public abstract class AbstractCommand implements ICommand, Runnable {

	protected BasicAWSCredentials credentials = null;
	protected AmazonGlacierAsyncClient client = null;
	protected Config config = null;
	protected Region region = null;

	public AbstractCommand(Config config) {

		this.config = config;
		this.region = Region.getRegion(Regions.fromName(config.getRegion()));
		this.credentials = new BasicAWSCredentials(config.getKey(),
				config.getSecretKey());
		this.client = new AmazonGlacierAsyncClient(this.credentials);
		this.client.setEndpoint(this.region.getServiceEndpoint("glacier"));

	}

	@Override
	public boolean valid() {
		return true;
	}
}

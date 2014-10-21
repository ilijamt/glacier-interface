package com.matoski.glacier.cli;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.beust.jcommander.Parameter;
import com.matoski.glacier.pojo.Config;

public class Arguments {

	@Parameter(names = "--aws-region", description = "Sets the amazon region, if specified it will overide the data loaded from the configuration file")
	public String amazonRegion = "eu-west-1";

	@Parameter(names = "--aws-key", description = "Sets the amazon key, if specified it will overide the data loaded from the configuration file")
	public String amazonKey;

	@Parameter(names = "--aws-secret-key", description = "Sets the amazon secret key, if specified it will overide the data loaded from the configuration file")
	public String amazonSecretKey;

	@Parameter(names = "--aws-vault", description = "Sets the amazon vault, if specified it will overide the data loaded from the configuration file")
	public String amazonVault;

	@Parameter(names = "--config", description = "Location to the configuration file to load")
	public String config;

	public Config generate() throws IOException {

		Config config = new Config();
		BufferedReader bufferedReader = null;

		if (!this.config.isEmpty()) {
			bufferedReader = new BufferedReader(new FileReader(this.config));
			bufferedReader.close();
		}

		config.setKey(this.amazonKey);
		config.setSecretKey(this.amazonSecretKey);
		config.setVault(this.amazonVault);
		config.setRegion(this.amazonRegion);

		return config;
	}

}

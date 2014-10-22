package com.matoski.glacier.commands;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.glacier.model.CreateVaultRequest;
import com.amazonaws.services.glacier.model.CreateVaultResult;
import com.amazonaws.services.glacier.model.DescribeVaultRequest;
import com.amazonaws.services.glacier.model.DescribeVaultResult;
import com.matoski.glacier.Output;
import com.matoski.glacier.cli.CommandCreateVault;
import com.matoski.glacier.pojo.Config;

public class CreateVaultCommand extends AbstractCommand {

	protected CommandCreateVault command;

	public CreateVaultCommand(Config config, CommandCreateVault command) {

		super(config);
		this.command = command;
	}

	public void run() {

		System.out.println("COMMAND: create-vault");

		try {

			CreateVaultRequest createVaultRequest = new CreateVaultRequest()
					.withVaultName(command.vaultName);
			DescribeVaultRequest describeVaultRequest = new DescribeVaultRequest()
					.withVaultName(command.vaultName);

			CreateVaultResult createVaultResult = client
					.createVault(createVaultRequest);
			DescribeVaultResult describeVaultResult = client
					.describeVault(describeVaultRequest);

			Output.process(createVaultResult,
					this.region.getServiceEndpoint("glacier"));
			Output.process(describeVaultResult);

		} catch (AmazonServiceException e) {
			switch (e.getErrorCode()) {
			case "InvalidSignatureException":
				Output.process(String.format("ERROR: Invalid credentials, check you key and secret key."));
				break;
			default:
				Output.process(String.format(
						"ERROR: Failed to create a vault: %s\n\t%s",
						command.vaultName, e.getMessage()));				
				break;
			}
		} catch (AmazonClientException e) {
			Output.process(String.format(
					"ERROR: Cannot connect to the amazon web services.\n\t%s",
					e.getMessage()));
		}

	}

	public boolean valid() {
		return false;
	}

}

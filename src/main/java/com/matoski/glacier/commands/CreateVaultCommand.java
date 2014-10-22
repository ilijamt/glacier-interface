package com.matoski.glacier.commands;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.glacier.model.CreateVaultRequest;
import com.amazonaws.services.glacier.model.CreateVaultResult;
import com.amazonaws.services.glacier.model.DescribeVaultRequest;
import com.amazonaws.services.glacier.model.DescribeVaultResult;
import com.matoski.glacier.cli.CommandCreateVault;
import com.matoski.glacier.pojo.Config;

public class CreateVaultCommand extends AbstractCommand {

	protected CommandCreateVault command;

	public CreateVaultCommand(Config config, CommandCreateVault command) {

		super(config);
		this.command = command;
	}

	public void run() {

		System.out.println("START: create-vault\n");

		try {

			CreateVaultRequest createVaultRequest = new CreateVaultRequest()
					.withVaultName(command.vaultName);
			DescribeVaultRequest describeVaultRequest = new DescribeVaultRequest()
					.withVaultName(command.vaultName);

			CreateVaultResult createVaultResult = client
					.createVault(createVaultRequest);
			DescribeVaultResult describeVaultResult = client
					.describeVault(describeVaultRequest);

			System.out.println(String.format(
					"Vault created succesfully location: %s%s",
					this.region.getServiceEndpoint("glacier"),
					createVaultResult.getLocation()));

			System.out.println(String.format("ARN: %s\nName: %s\nCreated: %s",
					describeVaultResult.getVaultARN(),
					describeVaultResult.getVaultName(),
					describeVaultResult.getCreationDate()));

		} catch (AmazonServiceException e) {
			switch (e.getErrorCode()) {
			case "InvalidSignatureException":
				System.out
						.println(String
								.format("ERROR: Invalid credentials, check you key and secret key."));
				break;
			default:
				System.out.println(String.format(
						"ERROR: Failed to create a vault: %s\n\t%s",
						command.vaultName, e.getMessage()));
				break;
			}
		} catch (AmazonClientException e) {
			System.out.println(String.format(
					"ERROR: Cannot connect to the amazon web services.\n\t%s",
					e.getMessage()));
		}

		System.out.println("\nEND: create-vault");

	}

	public boolean valid() {
		return false;
	}

}

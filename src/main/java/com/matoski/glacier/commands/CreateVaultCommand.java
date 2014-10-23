package com.matoski.glacier.commands;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.glacier.model.CreateVaultRequest;
import com.amazonaws.services.glacier.model.CreateVaultResult;
import com.amazonaws.services.glacier.model.DescribeVaultRequest;
import com.amazonaws.services.glacier.model.DescribeVaultResult;
import com.matoski.glacier.cli.CommandCreateVault;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;

public class CreateVaultCommand extends AbstractCommand {

    protected CommandCreateVault command;

    public CreateVaultCommand(Config config, CommandCreateVault command)
	    throws VaultNameNotPresentException {

	super(config);
	this.command = command;

	if ((null == command.vaultName || command.vaultName.isEmpty())
		&& (null == config.getVault() || config.getVault().isEmpty())) {
	    throw new VaultNameNotPresentException();
	}

	if ((null == command.vaultName) || command.vaultName.isEmpty()) {
	    command.vaultName = config.getVault();
	}
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

	    System.out.println(String.format("%1$12s: %2$s%3$s", "Location",
		    this.region.getServiceEndpoint("glacier"),
		    createVaultResult.getLocation()));
	    System.out.println(String.format("%1$12s: %2$s", "ARN",
		    describeVaultResult.getVaultARN()));
	    System.out.println(String.format("%1$12s: %2$s", "Vault Name",
		    describeVaultResult.getVaultName()));
	    System.out.println(String.format("%1$12s: %2$s", "Created",
		    describeVaultResult.getCreationDate()));

	} catch (AmazonServiceException e) {
	    switch (e.getErrorCode()) {
	    case "InvalidSignatureException":
		System.err
			.println(String
				.format("ERROR: Invalid credentials, check you key and secret key."));
		break;
	    default:
		System.err.println(String.format(
			"ERROR: Failed to create a vault: %s\n\t%s",
			command.vaultName, e.getMessage()));
		break;
	    }
	} catch (AmazonClientException e) {
	    System.err.println(String.format(
		    "ERROR: Cannot connect to the amazon web services.\n\t%s",
		    e.getMessage()));
	}

	System.out.println("\nEND: create-vault");

    }

}

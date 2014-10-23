package com.matoski.glacier.commands;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.glacier.model.DeleteVaultRequest;
import com.matoski.glacier.cli.CommandDeleteVault;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;

public class DeleteVaultCommand extends AbstractCommand {

    protected CommandDeleteVault command;

    public DeleteVaultCommand(Config config, CommandDeleteVault command)
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

	System.out.println("START: delete-vault\n");

	try {

	    DeleteVaultRequest request = new DeleteVaultRequest()
		    .withVaultName(command.vaultName);
	    client.deleteVault(request);

	    System.out
		    .println(String
			    .format("%s deleted. (Currently Amazon Glacier does not return error if vault does not exists)",
				    command.vaultName));

	} catch (AmazonServiceException e) {
	    switch (e.getErrorCode()) {
	    case "InvalidSignatureException":
		System.err
			.println(String
				.format("ERROR: Invalid credentials, check you key and secret key."));
		break;
	    default:
		System.err.println(String.format(
			"ERROR: Failed to delete a vault: %s\n\t%s",
			command.vaultName, e.getMessage()));
		break;
	    }
	} catch (AmazonClientException e) {
	    System.err.println(String.format(
		    "ERROR: Cannot connect to the amazon web services.\n\t%s",
		    e.getMessage()));
	}

	System.out.println("\nEND: delete-vault");

    }

}

package com.matoski.glacier.commands;

import com.amazonaws.services.glacier.model.InitiateJobRequest;
import com.amazonaws.services.glacier.model.InitiateJobResult;
import com.amazonaws.services.glacier.model.JobParameters;
import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandInventoryRetrieval;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;

public class InventoryRetrievalCommand extends
	AbstractCommand<CommandInventoryRetrieval> {

    public InventoryRetrievalCommand(Config config,
	    CommandInventoryRetrieval command)
	    throws VaultNameNotPresentException, RegionNotSupportedException {
	super(config, command);

	if ((null == command.vaultName || command.vaultName.isEmpty())
		&& (null == config.getVault() || config.getVault().isEmpty())) {
	    throw new VaultNameNotPresentException();
	}

	if ((null == command.vaultName) || command.vaultName.isEmpty()) {
	    command.vaultName = config.getVault();
	}

    }

    @Override
    public void run() {

	System.out.println("START: inventory-retrieve\n");

	InitiateJobRequest initJobRequest = new InitiateJobRequest()
		.withVaultName(command.vaultName).withJobParameters(
			new JobParameters().withType("inventory-retrieval"));

	InitiateJobResult initJobResult = this.client
		.initiateJob(initJobRequest);
	String jobId = initJobResult.getJobId();

	System.out.println("Inventory retrieved.\n");

	System.out.println(String.format("%1$10s: %2$s", "Job ID", jobId));
	System.out.println(String.format("%1$10s: %2$s", "Vault",
		command.vaultName));

	System.out.println("\nEND: inventory-retrieve");
    }
}

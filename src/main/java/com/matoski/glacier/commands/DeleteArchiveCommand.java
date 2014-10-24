package com.matoski.glacier.commands;

import com.amazonaws.services.glacier.model.DeleteArchiveRequest;
import com.matoski.glacier.cli.CommandDeleteArchive;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;

public class DeleteArchiveCommand extends AbstractCommand<CommandDeleteArchive> {

    public DeleteArchiveCommand(Config config, CommandDeleteArchive command)
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

	System.out.println("START: delete-archive\n");

	try {

	    client.deleteArchive(new DeleteArchiveRequest().withVaultName(
		    command.vaultName).withArchiveId(command.id));

	    System.out.println("Archive deleted.\n");

	    System.out.println(String.format("%1$10s: %2$s", "Vault",
		    command.vaultName));
	    System.out.println(String.format("%1$10s: %2$s", "Archive ID",
		    command.id));

	} catch (Exception e) {
	    System.err.println("Failed to delete the archive");
	}

	System.out.println("\nEND: delete-archive");
    }
}

package com.matoski.glacier.commands;

import com.matoski.glacier.cli.CommandUploadArchive;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;

public class UploadArchiveCommand extends AbstractCommand {

    protected CommandUploadArchive command;

    public UploadArchiveCommand(Config config, CommandUploadArchive command)
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

	System.out.println("START: upload-archive\n");

	System.out.println("\nEND: upload-archive");
    }
}

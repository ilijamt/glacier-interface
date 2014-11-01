package com.matoski.glacier.commands;

import java.io.IOException;

import com.amazonaws.services.glacier.model.InitiateJobResult;
import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandInitDownload;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Archive;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.pojo.journal.State;
import com.matoski.glacier.util.download.AmazonGlacierDownloadUtil;

public class InitDownloadCommand extends AbstractCommand<CommandInitDownload> {

    protected String archiveId;
    private State journal;

    public InitDownloadCommand(Config config, CommandInitDownload command) throws VaultNameNotPresentException, RegionNotSupportedException {
	super(config, command);
	archiveId = command.id;

	Boolean validVaultName = null != command.vaultName;
	Boolean validVaultNameConfig = null != config.getVault();
	Boolean validId = (null != command.id);
	Boolean validName = (null != command.name);

	if (command.ignoreJournal) {
	    try {
		this.journal = State.load(command.journal);
	    } catch (IOException e) {
		throw new RuntimeException("Journal doesn't exist");
	    }
	}

	if (!validVaultName && !validVaultNameConfig) {
	    throw new VaultNameNotPresentException();
	}

	if (validVaultNameConfig) {
	    command.vaultName = config.getVault();
	}

	if (!validId && !validName) {
	    throw new IllegalArgumentException("ID or NAME are required");
	}

	if (command.ignoreJournal && !validId) {
	    throw new IllegalArgumentException("ID is required");
	}

	String archiveName = "N/A";

	if (validId && !validName) {
	    archiveId = command.id;
	} else {
	    Archive archive = journal.getByName(command.name);
	    if (null == archive) {
		throw new IllegalArgumentException("The archive is not present in the journal");
	    }
	    archiveName = archive.getName();
	    archiveId = archive.getId();
	}
	System.out.println(String.format("Initiating download for %s[%s]", archiveName, archiveId));

    }

    @Override
    public void run() {

	System.out.println("START: init-download\n");

	AmazonGlacierDownloadUtil download = new AmazonGlacierDownloadUtil(credentials, client, region);

	InitiateJobResult request = download.InitiateDownloadRequest(command.vaultName, archiveId);

	System.out.println("\nEND: init-download");
    }
}

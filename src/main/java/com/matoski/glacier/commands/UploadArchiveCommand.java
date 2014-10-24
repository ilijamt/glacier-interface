package com.matoski.glacier.commands;

import com.matoski.glacier.cli.CommandUploadArchive;
import com.matoski.glacier.enums.Metadata;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.util.AmazonGlacierUploadUtil;

/**
 * Upload archive
 * 
 * @author ilijamt
 */
public class UploadArchiveCommand extends AbstractCommand<CommandUploadArchive> {

    /**
     * Metadata
     */
    protected Metadata metadata;

    /**
     * The helper utility for uploading
     */
    protected AmazonGlacierUploadUtil upload;

    /**
     * Constructor
     * 
     * @param config
     * @param command
     * 
     * @throws VaultNameNotPresentException
     * @throws RegionNotSupportedException
     */
    public UploadArchiveCommand(Config config, CommandUploadArchive command)
	    throws VaultNameNotPresentException, RegionNotSupportedException {
	super(config, command);

	if ((null == command.vaultName || command.vaultName.isEmpty())
		&& (null == config.getVault() || config.getVault().isEmpty())) {
	    throw new VaultNameNotPresentException();
	}

	if ((null == command.vaultName) || command.vaultName.isEmpty()) {
	    command.vaultName = config.getVault();
	}

	this.metadata = Metadata.from(command.metadata);
	this.upload = new AmazonGlacierUploadUtil(credentials, client, region);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

	System.out.println("START: upload-archive\n");

	if (command.files.isEmpty()) {
	    System.out.println("ERROR: No files specified");
	} else {

	    for (String fileName : command.files) {
		System.out.println(String.format("Processing: %s", fileName));
	    }

	}

	System.out.println("\nEND: upload-archive");
    }
}

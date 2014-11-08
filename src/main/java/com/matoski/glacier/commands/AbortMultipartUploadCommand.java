package com.matoski.glacier.commands;

import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandAbortMultipartUpload;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.util.upload.AmazonGlacierUploadUtil;

/**
 * Aborts a multipart upload 
 * 
 * @author ilijamt
 */
public class AbortMultipartUploadCommand extends AbstractCommand<CommandAbortMultipartUpload> {

    /**
     * Constructor 
     * 
     * @param config
     * @param command
     * @throws VaultNameNotPresentException
     * @throws RegionNotSupportedException
     */
    public AbortMultipartUploadCommand(Config config, CommandAbortMultipartUpload command) throws VaultNameNotPresentException,
	    RegionNotSupportedException {
	super(config, command);

	if ((null == command.vaultName || command.vaultName.isEmpty()) && (null == config.getVault() || config.getVault().isEmpty())) {
	    throw new VaultNameNotPresentException();
	}

	if ((null == command.vaultName) || command.vaultName.isEmpty()) {
	    command.vaultName = config.getVault();
	}

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

	System.out.println("START: abort-multipart-upload\n");

	AmazonGlacierUploadUtil upload = new AmazonGlacierUploadUtil(credentials, client, region);

	Boolean canceled = upload.CancelMultipartUpload(command.multipartId, command.vaultName);
	System.out.println(String.format("Multipart upload canceled: %s\n", canceled));

	System.out.println("END: abort-multipart-upload");
    }
}

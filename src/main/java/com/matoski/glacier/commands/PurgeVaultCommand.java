package com.matoski.glacier.commands;

import java.io.IOException;
import java.util.Map.Entry;

import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandPurgeVault;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.pojo.archive.Archive;
import com.matoski.glacier.pojo.journal.State;
import com.matoski.glacier.util.upload.AmazonGlacierUploadUtil;

public class PurgeVaultCommand extends AbstractCommand<CommandPurgeVault> {

    /**
     * The journal, we use this for storing the data
     */
    protected State journal;

    public PurgeVaultCommand(Config config, CommandPurgeVault command) throws VaultNameNotPresentException, RegionNotSupportedException {
	super(config, command);

	Boolean validVaultName = null != command.vaultName;
	Boolean validVaultNameConfig = null != config.getVault();

	try {
	    this.journal = State.load(command.journal);
	} catch (IOException e) {
	    throw new RuntimeException("Journal doesn't exist");
	}

	if (!validVaultName && !validVaultNameConfig) {
	    throw new VaultNameNotPresentException();
	}

	if (validVaultNameConfig) {
	    command.vaultName = config.getVault();
	}

    }

    @Override
    public void run() {

	System.out.println("START: purge-vault\n");

	if (journal.size() > 0) {
	    Archive archive = null;
	    AmazonGlacierUploadUtil upload = new AmazonGlacierUploadUtil(credentials, client, region);

	    for (Entry<String, Archive> entry : journal.getArchives().entrySet()) {

		try {
		    archive = entry.getValue();

		    upload.DeleteArchive(command.vaultName, archive.getId());
		    System.out.println(String.format("DELETED [%s] %s", archive.getId(), archive.getName()));

		    this.journal.deleteArchive(archive.getId());
		    this.journal.save();

		} catch (Exception e) {
		    System.err.println("Failed to delete the archive");
		}

	    }
	} else {
	    System.out.println("No items available in the journal");
	}

	System.out.println("\nEND: purge-vault");
    }
}

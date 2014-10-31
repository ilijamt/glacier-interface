package com.matoski.glacier.commands;

import java.io.IOException;

import com.amazonaws.services.glacier.model.DeleteArchiveRequest;
import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandDeleteArchive;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Archive;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.pojo.journal.State;

public class DeleteArchiveCommand extends AbstractCommand<CommandDeleteArchive> {

    /**
     * The journal, we use this for storing the data
     */
    protected State journal;

    /**
     * The archive ID we need to delete
     */
    protected String archiveId;

    /**
     * Constructor
     * 
     * @param config
     * @param command
     * 
     * @throws VaultNameNotPresentException
     * @throws RegionNotSupportedException
     * @throws IllegalArgumentException
     */
    public DeleteArchiveCommand(Config config, CommandDeleteArchive command) throws VaultNameNotPresentException,
	    RegionNotSupportedException, IllegalArgumentException {
	super(config, command);

	Boolean validVaultName = null != command.vaultName;
	Boolean validVaultNameConfig = null != config.getVault();
	Boolean validId = (null != command.id);
	Boolean validName = (null != command.name);

	try {
	    this.journal = State.load(command.journal);
	} catch (IOException e) {
	    System.out.println(String.format("Creating a new journal: %s", command.journal));
	    this.journal = new State();
	    this.journal.setFile(command.journal);
	    throw new RuntimeException("Journal doesn't exist");
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

	if (validId && !validName) {
	    archiveId = command.id;
	} else {
	    Archive archive = journal.getByName(command.name);
	    if (null == archive) {
		throw new IllegalArgumentException("The archive is not present in the journal");
	    }
	    archiveId = archive.getId();
	}

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

	System.out.println("START: delete-archive\n");

	try {

	    client.deleteArchive(new DeleteArchiveRequest().withVaultName(command.vaultName).withArchiveId(archiveId));

	    System.out.println("Archive deleted.\n");

	    this.journal.deleteArchive(archiveId);

	    System.out.println(String.format("%1$10s: %2$s", "Vault", command.vaultName));
	    System.out.println(String.format("%1$10s: %2$s", "Archive ID", command.id));

	} catch (Exception e) {
	    System.err.println("Failed to delete the archive");
	}

	System.out.println("\nEND: delete-archive");
    }
}

package com.matoski.glacier.commands;

import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandDeleteArchive;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.pojo.archive.Archive;
import com.matoski.glacier.pojo.journal.State;
import com.matoski.glacier.util.upload.AmazonGlacierUploadUtil;

import java.io.IOException;

/**
 * Delete an archive
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class DeleteArchiveCommand extends AbstractCommand<CommandDeleteArchive> {

    /**
     * The journal, we use this for storing the data.
     */
    protected State journal;

    /**
     * The archive ID we need to delete.
     */
    protected String archiveId;

    /**
     * Constructor.
     *
     * @param config  Application config
     * @param command The command configuration
     * @throws VaultNameNotPresentException Vault not present in config
     * @throws RegionNotSupportedException  Region not supported
     * @throws IllegalArgumentException     Missing arguments
     */
    public DeleteArchiveCommand(Config config, CommandDeleteArchive command)
            throws VaultNameNotPresentException, RegionNotSupportedException, IllegalArgumentException {
        super(config, command);

        final Boolean validVaultName = null != command.vaultName;
        final Boolean validVaultNameConfig = null != config.getVault();
        final Boolean validId = null != command.id;
        final Boolean validName = null != command.name;

        if (!command.ignoreJournal) {
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

        if (command.ignoreJournal && !validId) {
            throw new IllegalArgumentException("ID is required, when --ignore-journal is supplied");
        }

        if (!validId && !validName) {
            throw new IllegalArgumentException("ID or NAME are required");
        }

        if (command.ignoreJournal && !validId) {
            throw new IllegalArgumentException("ID is required");
        }

        if (validId && !validName) {
            archiveId = command.id;
        } else if (!command.ignoreJournal) {
            Archive archive = journal.getByName(command.name);
            if (null == archive) {
                throw new IllegalArgumentException("The archive is not present in the journal");
            }
            archiveId = archive.getId();
        }

    }

    @Override
    public void run() {

        System.out.println("START: delete-archive\n");

        AmazonGlacierUploadUtil upload = new AmazonGlacierUploadUtil(credentials, client, region);

        try {

            upload.deleteArchive(command.vaultName, archiveId);

            System.out.println("Archive deleted.\n");

            this.journal.deleteArchive(archiveId);

            System.out.println(String.format("%1$10s: %2$s", "Vault", command.vaultName));
            System.out.println(String.format("%1$10s: %2$s", "Archive ID", archiveId));

            this.journal.save();

        } catch (Exception e) {
            System.err.println("Failed to delete the archive");
        }

        System.out.println("\nEND: delete-archive");
    }
}

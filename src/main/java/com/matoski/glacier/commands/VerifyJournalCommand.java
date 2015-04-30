package com.matoski.glacier.commands;

import com.matoski.glacier.base.AbstractEmptyCommand;
import com.matoski.glacier.cli.CommandVerifyJournal;
import com.matoski.glacier.enums.GenericValidateEnum;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.pojo.archive.Archive;
import com.matoski.glacier.pojo.journal.State;
import com.matoski.glacier.util.FileUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Map.Entry;

/**
 * Verify the data from the journal
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class VerifyJournalCommand extends AbstractEmptyCommand<CommandVerifyJournal> {

    /**
     * The journal.
     */
    private State journal;

    /**
     * Constructor.
     *
     * @param config  Application config
     * @param command The command configuration
     * @throws VaultNameNotPresentException Vault not present in config
     * @throws RegionNotSupportedException  Region not supported
     */
    public VerifyJournalCommand(Config config, CommandVerifyJournal command)
            throws VaultNameNotPresentException, RegionNotSupportedException {
        super(config, command);

        try {
            this.journal = State.load(command.journal);
        } catch (IOException e) {
            throw new RuntimeException("Journal doesn't exist");
        }

    }

    @Override
    public void run() {

        System.out.println("START: verify-journal\n");

        Archive archive = null;

        long validSize = 0;
        long size = 0;
        Boolean valid = false;

        GenericValidateEnum validateSize;
        GenericValidateEnum validateModified;
        GenericValidateEnum validateHash;

        System.out.println(String.format("There are %s files in the journal", this.journal.size()));
        System.out.println();

        for (Entry<String, Archive> entry : this.journal.getArchives().entrySet()) {
            valid = false;
            archive = entry.getValue();

            validateSize = State.archiveValidateFileSize(archive);
            validateModified = State.archiveValidateLastModified(archive);
            System.out.println(String.format("%1$17s : %2$s", "Archive ID", archive.getId()));
            System.out.println(String.format("%1$17s : %2$s", "Name", archive.getName()));
            System.out.println(String.format("%1$17s : %2$s (%3$s, %4$s bytes)", "Size", validateSize,
                    FileUtils.humanReadableByteCount(archive.getSize()), archive.getSize()));
            Date modifiedDate = new Date(archive.getModifiedDate());
            System.out.println(String.format("%1$17s : %2$s (%3$s)", "Modified", validateModified,
                    modifiedDate));

            if (command.skipHash) {
                validateHash = GenericValidateEnum.SKIP;
            } else {
                validateHash = State.archiveValidateTreeHash(archive);
            }

            System.out.println(String.format("%1$17s : %2$s (%3$s)", "SHA256 TreeHash", validateHash,
                    archive.getHash()));
            boolean validHash = validateHash == GenericValidateEnum.VALID
                    || validateHash == GenericValidateEnum.SKIP;
            valid = validateSize == GenericValidateEnum.VALID
                    && validateModified == GenericValidateEnum.VALID && validHash;

            System.out.println(String.format("%1$17s : %2$s", "Valid", valid));
            System.out.println();

            size += archive.getSize();
            if (valid) {
                validSize += archive.getSize();
            }

        }

        System.out.println(String.format("Invalid: %s (%s bytes)",
                FileUtils.humanReadableByteCount(size - validSize), size - validSize));
        System.out.println(String.format("Valid: %s (%s bytes)",
                FileUtils.humanReadableByteCount(validSize), validSize));
        System.out.println(String.format("Total size: %s (%s bytes)",
                FileUtils.humanReadableByteCount(size), size));

        System.out.println("\nEND: verify-journal");

    }
}

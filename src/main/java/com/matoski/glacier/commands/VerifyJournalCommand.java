package com.matoski.glacier.commands;

import java.io.IOException;
import java.util.Date;
import java.util.Map.Entry;

import com.matoski.glacier.base.AbstractEmptyCommand;
import com.matoski.glacier.cli.CommandVerifyJournal;
import com.matoski.glacier.enums.GenericValidateEnum;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.pojo.archive.Archive;
import com.matoski.glacier.pojo.journal.State;
import com.matoski.glacier.util.FileUtils;

/**
 * Verify the data from the journal
 * 
 * @author ilijamt
 *
 */
public class VerifyJournalCommand extends AbstractEmptyCommand<CommandVerifyJournal> {

    /**
     * The journal
     */
    private State journal;

    /**
     * Constructor
     * 
     * @param config
     * @param command
     * @throws VaultNameNotPresentException
     * @throws RegionNotSupportedException
     */
    public VerifyJournalCommand(Config config, CommandVerifyJournal command) throws VaultNameNotPresentException,
	    RegionNotSupportedException {
	super(config, command);

	try {
	    this.journal = State.load(command.journal);
	} catch (IOException e) {
	    throw new RuntimeException("Journal doesn't exist");
	}

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

	System.out.println("START: verify-journal\n");

	Archive archive = null;

	long validSize = 0;
	long size = 0;
	Boolean valid = false;

	GenericValidateEnum vSize;
	GenericValidateEnum vModified;
	GenericValidateEnum vHash;

	System.out.println(String.format("There are %s files in the journal", this.journal.size()));
	System.out.println();

	for (Entry<String, Archive> entry : this.journal.getArchives().entrySet()) {
	    valid = false;
	    archive = entry.getValue();

	    vSize = State.archiveValidateFileSize(archive);
	    vModified = State.archiveValidateLastModified(archive);
	    System.out.println(String.format("%1$17s : %2$s", "Archive ID", archive.getId()));
	    System.out.println(String.format("%1$17s : %2$s", "Name", archive.getName()));
	    System.out.println(String.format("%1$17s : %2$s (%3$s, %4$s bytes)", "Size", vSize,
		    FileUtils.humanReadableByteCount(archive.getSize()), archive.getSize()));
	    System.out.println(String.format("%1$17s : %2$s (%3$s)", "Modified", vModified, new Date(archive.getModifiedDate())));

	    if (command.skipHash) {
		vHash = GenericValidateEnum.SKIP;
	    } else {
		vHash = State.archiveValidateTreeHash(archive);
	    }

	    System.out.println(String.format("%1$17s : %2$s (%3$s)", "SHA256 TreeHash", vHash, archive.getHash()));
	    valid = vSize == GenericValidateEnum.VALID && vModified == GenericValidateEnum.VALID
		    && (vHash == GenericValidateEnum.VALID || vHash == GenericValidateEnum.SKIP);

	    System.out.println(String.format("%1$17s : %2$s", "Valid", valid));
	    System.out.println();

	    size += archive.getSize();
	    if (valid) {
		validSize += archive.getSize();
	    }

	}

	System.out.println(String.format("Invalid: %s", FileUtils.humanReadableByteCount(size - validSize, false)));
	System.out.println(String.format("Valid: %s", FileUtils.humanReadableByteCount(validSize, false)));
	System.out.println(String.format("Total size: %s", FileUtils.humanReadableByteCount(size, false)));

	System.out.println("\nEND: verify-journal");

    }
}

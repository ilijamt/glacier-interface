package com.matoski.glacier.commands;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import com.matoski.glacier.cli.CommandUploadArchive;
import com.matoski.glacier.enums.GenericValidateEnum;
import com.matoski.glacier.enums.Metadata;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.UploadTooManyPartsException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Archive;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.pojo.Journal;
import com.matoski.glacier.util.AmazonGlacierBaseUtil;
import com.matoski.glacier.util.upload.AmazonGlacierUploadUtil;

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
     * The journal, we use this for storing the data
     */
    protected Journal journal;

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

	command.partSize = command.partSize
		* (int) AmazonGlacierBaseUtil.MINIMUM_PART_SIZE;

	try {
	    this.journal = Journal.load(command.journal);
	} catch (IOException e) {
	    System.out.println(String.format("Creating a new journal: %s",
		    command.journal));
	    this.journal = new Journal();
	    this.journal.setFile(command.journal);
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

	    Archive archive = null;

	    for (String fileName : command.files) {

		// check if this file exists in the journal
		if (journal.isFileInArchive(fileName)) {

		    System.out.println(String.format(
			    "%s is already present in the journal", fileName));
		    System.out.println(String.format("Verifying ..."));

		    Archive testArchive = journal.getByName(fileName);

		    GenericValidateEnum validSize = Journal
			    .archiveValidateFileSize(testArchive);
		    GenericValidateEnum validModifiedDate = Journal
			    .archiveValidateLastModified(testArchive);
		    GenericValidateEnum validTreeHash = GenericValidateEnum.INVALID;

		    System.out.println(String.format("%s size is %s", fileName,
			    validSize));
		    System.out.println(String.format("%s modified date is %s",
			    fileName, validModifiedDate));

		    if (validSize == GenericValidateEnum.VALID
			    && validModifiedDate == GenericValidateEnum.VALID) {
			Scanner in = new Scanner(System.in);
			System.out
				.print("Do you want to check tree hash (yes/no/quit?)");
			String response = in.nextLine();
			if (response.trim().equalsIgnoreCase("yes")) {
			    validTreeHash = Journal
				    .archiveValidateTreeHash(testArchive);
			    if (validTreeHash == GenericValidateEnum.VALID) {
				System.out.println(String.format(
					"%s hash is %s, skipping..", fileName,
					validTreeHash));
				continue;
			    }
			}

		    }

		}

		System.out.println(String.format("Processing: %s (size: %s)",
			fileName, new File(Config.getInstance().getDirectory(),
				fileName).length()));

		try {
		    archive = this.upload.UploadMultipartFile(new File(Config
			    .getInstance().getDirectory(), fileName),
			    command.concurrent, command.retryFailedUpload,
			    command.partSize, command.vaultName, metadata);

		    this.journal.addArchive(archive);
		    this.journal.save();

		} catch (UploadTooManyPartsException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (RegionNotSupportedException e) {
		    e.printStackTrace();
		}

	    }

	}

	System.out.println("\nEND: upload-archive");
    }
}

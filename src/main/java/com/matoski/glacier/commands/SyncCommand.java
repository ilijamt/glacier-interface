package com.matoski.glacier.commands;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;

import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandSync;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.pojo.journal.State;
import com.matoski.glacier.util.upload.AmazonGlacierUploadUtil;

public class SyncCommand extends AbstractCommand<CommandSync> {

    protected Collection<String> files = new LinkedList<String>();
    private State journal;

    public SyncCommand(Config config, CommandSync command) throws VaultNameNotPresentException, RegionNotSupportedException {
	super(config, command);

	try {
	    this.journal = State.load(command.journal);
	} catch (IOException e) {
	    System.out.println(String.format("Creating a new journal: %s", command.journal));
	    this.journal = new State();
	    this.journal.setMetadata(command.metadata);
	    this.journal.setName(command.vaultName);
	    this.journal.setDate(new Date());
	    this.journal.setFile(command.journal);
	}

    }

    @Override
    public void run() {

	System.out.println("START: sync\n");

	AmazonGlacierUploadUtil upload = new AmazonGlacierUploadUtil(credentials, client, region);

	Collection<File> result = FileUtils.listFiles(new File(config.getDirectory()), null, true);

	for (File file : result) {
	    files.add(file.getAbsolutePath().replace(config.getDirectory() + "/", ""));
	}

	System.out.println(String.format("%s files found", files.size()));

	for (String fileName : files) {
	    upload.UploadArchive(journal, command.vaultName, fileName, false, command.concurrent, command.retryFailedUpload,
		    command.partSize);
	}

	System.out.println("\nEND: sync");
    }
}

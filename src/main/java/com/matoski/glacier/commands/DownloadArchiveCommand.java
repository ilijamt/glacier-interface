package com.matoski.glacier.commands;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandDownloadArchive;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.pojo.job.DownloadJobInfo;
import com.matoski.glacier.util.download.AmazonGlacierDownloadUtil;

public class DownloadArchiveCommand extends AbstractCommand<CommandDownloadArchive> {

    DownloadJobInfo job = null;
    AmazonGlacierDownloadUtil download = null;

    public DownloadArchiveCommand(Config config, CommandDownloadArchive command) throws VaultNameNotPresentException,
	    RegionNotSupportedException, FileNotFoundException {
	super(config, command);

	try {
	    job = DownloadJobInfo.load(command.jobFile, DownloadJobInfo.class);
	} catch (InstantiationException | IllegalAccessException | IOException e) {
	    e.printStackTrace();
	}

	if (null == job) {
	    throw new FileNotFoundException(command.jobFile);
	}

	download = new AmazonGlacierDownloadUtil(credentials, client, region);

    }

    @Override
    public void run() {

	System.out.println("START: download-archive\n");

	System.out.println("\nEND: download-archive");
    }
}

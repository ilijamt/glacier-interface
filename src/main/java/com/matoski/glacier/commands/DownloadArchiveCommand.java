package com.matoski.glacier.commands;

import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandDownloadArchive;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;

public class DownloadArchiveCommand extends
	AbstractCommand<CommandDownloadArchive> {

    public DownloadArchiveCommand(Config config, CommandDownloadArchive command)
	    throws VaultNameNotPresentException, RegionNotSupportedException {
	super(config, command);
    }

    @Override
    public void run() {

	System.out.println("START: download-archive\n");

	System.out.println("\nEND: download-archive");
    }
}

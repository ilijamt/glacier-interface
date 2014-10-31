package com.matoski.glacier.commands;

import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandInitDownload;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;

public class InitDownloadCommand extends AbstractCommand<CommandInitDownload> {

    public InitDownloadCommand(Config config, CommandInitDownload command)
	    throws VaultNameNotPresentException, RegionNotSupportedException {
	super(config, command);
    }

    @Override
    public void run() {

	System.out.println("START: init-download\n");

	System.out.println("\nEND: init-download");
    }
}

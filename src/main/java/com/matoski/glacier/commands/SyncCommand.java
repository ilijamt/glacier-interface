package com.matoski.glacier.commands;

import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandSync;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;

public class SyncCommand extends AbstractCommand<CommandSync> {

    public SyncCommand(Config config, CommandSync command) throws VaultNameNotPresentException, RegionNotSupportedException {
	super(config, command);
    }

    @Override
    public void run() {

	System.out.println("START: sync\n");

	System.out.println("\nEND: sync");
    }
}

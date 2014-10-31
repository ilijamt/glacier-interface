package com.matoski.glacier.commands;

import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandPurge;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;

public class PurgeCommand extends AbstractCommand<CommandPurge> {

    public PurgeCommand(Config config, CommandPurge command) throws VaultNameNotPresentException, RegionNotSupportedException {
	super(config, command);
    }

    @Override
    public void run() {

	System.out.println("START: purge\n");

	System.out.println("\nEND: purge");
    }
}

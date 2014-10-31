package com.matoski.glacier.cli;

import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "sync", commandDescription = "Sync a folder contents to amazon glacier")
public class CommandSync extends GenericCommand {

    public CommandSync() {
	super(CliCommands.Sync);
    }

}
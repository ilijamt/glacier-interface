package com.matoski.glacier.cli;

import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "purge", commandDescription = "Purge the data from the vault that is missing in the journal")
public class CommandPurge extends GenericCommand {

    public CommandPurge() {
	super(CliCommands.Purge);
    }

}
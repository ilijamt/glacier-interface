package com.matoski.glacier.cli;

import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "help", commandDescription = "Show the help page")
public class CommandHelp extends GenericCommand {

    public CommandHelp() {
	super(CliCommands.Help);
    }

}

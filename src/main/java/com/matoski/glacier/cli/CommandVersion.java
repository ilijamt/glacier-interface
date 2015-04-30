package com.matoski.glacier.cli;

import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "version", commandDescription = "Show the version of the application")
public class CommandVersion extends GenericCommand {

    public CommandVersion() {
        super(CliCommands.Version);
    }

}

package com.matoski.glacier.cli;

import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "init-download", commandDescription = "Initiate a download job")
public class CommandInitDownload extends GenericCommand {

    public CommandInitDownload() {
	super(CliCommands.InitDownload);
    }

}
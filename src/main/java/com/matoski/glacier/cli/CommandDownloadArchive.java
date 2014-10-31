package com.matoski.glacier.cli;

import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "download-archive", commandDescription = "Download an archive")
public class CommandDownloadArchive extends GenericCommand {

    public CommandDownloadArchive() {
	super(CliCommands.DownloadArchive);
    }

}
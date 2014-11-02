package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "download-job", commandDescription = "Download the job")
public class CommandDownloadArchive extends GenericCommand {

    @Parameter(required = true, names = "--job-file", description = "Uses this file to download all the archives in the file")
    public String jobFile;

    public CommandDownloadArchive() {
	super(CliCommands.DownloadArchive);
    }

}
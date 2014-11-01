package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "init-download", commandDescription = "Initiate a download job")
public class CommandInitDownload extends GenericCommand {

    @Parameter(names = "--vault", description = "The name of the vault for whom the inventory needs to be retrieved, will be overwritten by --aws-vault if not specified")
    public String vaultName;

    @Parameter(names = "--id", description = "The id of the archive")
    public String id;

    @Parameter(names = "--name", description = "The name of the archive")
    public String name;

    @Parameter(required = true, names = "--journal", description = "Journal location")
    public String journal;

    @Parameter(names = "--ignore-journal", description = "Ignore the journal when downloading, you should only use this with ID")
    public Boolean ignoreJournal = false;

    public CommandInitDownload() {
	super(CliCommands.InitDownload);
    }

}
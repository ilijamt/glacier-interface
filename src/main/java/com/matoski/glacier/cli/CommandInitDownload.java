package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

import java.util.ArrayList;
import java.util.List;

@Parameters(commandNames = "init-download", commandDescription = "Initiate a download job")
public class CommandInitDownload extends GenericCommand {

    @Parameter(
            names = "--vault",
            description = "The name of the vault for whom the inventory needs to be retrieved, will be overwritten by --aws-vault if not specified")
    public String vaultName;

    @Parameter(names = "--id", description = "The id(s) of the archive, you can supply multiple ids")
    public List<String> id = new ArrayList<String>();

    @Parameter(names = "--name",
            description = "The name of the archive, you can supply multiple names")
    public List<String> name = new ArrayList<String>();

    @Parameter(required = true, names = "--journal", description = "Journal location")
    public String journal;

    @Parameter(
            names = "--ignore-journal",
            description = "Ignore the journal when downloading, you should only use this with ID, the journal will still be loaded to get the names of the archives in the journal, the other names will be named as the archive id")
    public Boolean ignoreJournal = false;

    @Parameter(required = true, names = "--job-file",
            description = "Where to store the job, this is used for downloading the archives")
    public String jobFile;

    @Parameter(names = "--wait", description = "Should we wait to process the download job, or not")
    public Boolean wait = false;

    public CommandInitDownload() {
        super(CliCommands.InitDownload);
    }

}
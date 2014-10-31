package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "delete-archive", commandDescription = "Initiate a deletion of archive")
public class CommandDeleteArchive extends GenericCommand {

    public CommandDeleteArchive() {
	super(CliCommands.DeleteArchive);
    }

    @Parameter(names = "--vault", description = "The name of the vault from where the archive will be deleted, will be overwriten by --aws-vault if not specified")
    public String vaultName;

    @Parameter(names = "--metadata", description = "Available: mt2, fgv2")
    public String metadata = "mt2";

    @Parameter(required = true, names = "--journal", description = "Journal")
    public String journal;

    @Parameter(names = "--id", description = "The id of the archive")
    public String id;

    @Parameter(names = "--name", description = "The name of the archive")
    public String name;

}

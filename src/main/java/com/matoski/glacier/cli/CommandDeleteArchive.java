package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "delete-archive", commandDescription = "Initiate a deletion of archive")
public class CommandDeleteArchive {

    @Parameter(names = "--vault", description = "The name of the vault from where the archive will be deleted, will be overwriten by --aws-vault if not specified")
    public String vaultName;

    @Parameter(required = true, names = "--id", description = "The ID of the archive")
    public String id;

}

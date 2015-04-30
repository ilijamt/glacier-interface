package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "purge-vault", commandDescription = "Purge the data from the vault")
public class CommandPurgeVault extends GenericCommand {

    @Parameter(
            names = "--vault",
            description = "The name of the vault from where the archive will be purged, will be overwritten by --aws-vault if not specified")
    public String vaultName;

    @Parameter(required = true, names = "--journal", description = "Journal")
    public String journal;

    public CommandPurgeVault() {
        super(CliCommands.PurgeVault);
    }

}
package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "create-vault",
        commandDescription = "Creates a new vault on Amazon Glacier")
public class CommandCreateVault extends GenericCommand {

    @Parameter(
            names = "--vault",
            description = "The name of the vault to be created, will be overwritten by --aws-vault if not specified")
    public String vaultName;

    public CommandCreateVault() {
        super(CliCommands.CreateVault);
    }

}

package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(
        commandNames = "delete-vault",
        commandDescription = "Deletes the vault from Amazon Glacier, this will not work if the vault is not empty, so you will have to delete everything in the vault before you can delete the vault")
public class CommandDeleteVault extends GenericCommand {

    @Parameter(
            names = "--vault",
            description = "The name of the vault to be deleted, will be overwritten by --aws-vault if not specified")
    public String vaultName;

    public CommandDeleteVault() {
        super(CliCommands.DeleteVault);
    }

}

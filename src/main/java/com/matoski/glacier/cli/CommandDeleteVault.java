package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "delete-vault", commandDescription = "Deletes the vault from Amazon Glacier, this will not work if the vault is not empty, so you will have to delete everything in the vault before you can delete the vault")
public class CommandDeleteVault {

    @Parameter(names = "--vault", description = "The name of the vault to be deleted, will be overwriten by --aws-vault if not specified")
    public String vaultName;

}

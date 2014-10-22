package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "create-vault", commandDescription = "Creates a new vault on Amazon Glacier")
public class CommandCreateVault {

    @Parameter(names = "--vault", description = "The name of the vault to be created, will be overwriten by --aws-vault if not specified")
    public String vaultName;

}

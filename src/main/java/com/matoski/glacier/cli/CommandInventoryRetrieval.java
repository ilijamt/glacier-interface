package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "inventory-retrieve", commandDescription = "Initiate an inventory retrieval")
public class CommandInventoryRetrieval {

    @Parameter(names = "--vault", description = "The name of the vault for whom the inventory needs to be retrieved, will be overwriten by --aws-vault if not specified")
    public String vaultName;

}

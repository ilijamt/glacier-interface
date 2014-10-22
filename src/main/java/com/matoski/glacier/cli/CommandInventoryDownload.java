package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "inventory-download", commandDescription = "Initiate an inventory download, and it will download the last succesfull InventoryRetrieval")
public class CommandInventoryDownload {

    @Parameter(names = "--vault", description = "The name of the vault for whom the inventory needs to be retrieved, will be overwriten by --aws-vault if not specified")
    public String vaultName;

    @Parameter(names = "--id", description = "The ID of the Job with InventoryRetrieval type")
    public String id;

    @Parameter(required = true, names = "--journal", description = "Where to save the data retrieved from the inventory")
    public String journal;

}

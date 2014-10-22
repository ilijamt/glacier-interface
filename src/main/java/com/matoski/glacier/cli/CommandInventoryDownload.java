package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "inventory-download", commandDescription = "Initiate an inventory download")
public class CommandInventoryDownload {

    @Parameter(names = "--vault", description = "The name of the vault for whom the inventory needs to be retrieved, will be overwriten by --aws-vault if not specified")
    public String vaultName;

    @Parameter(names = "--id", description = "The ID of the Job with InventoryRetrival type")
    public String id;

    @Parameter(names = "--last", description = "Retrieve the last available inventory job, if ID is supply it will fetch the job with the ID")
    public Boolean last = true;

    @Parameter(names = "--journal", description = "Where to save the data retrieved from the inventory")
    public String journal;
}

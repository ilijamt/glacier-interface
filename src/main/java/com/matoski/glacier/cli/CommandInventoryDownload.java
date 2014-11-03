package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.matoski.glacier.Constants;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "inventory-download", commandDescription = "Initiate an inventory download, and it will download the last successful InventoryRetrieval")
public class CommandInventoryDownload extends GenericCommand {

    @Parameter(names = "--vault", description = "The name of the vault for whom the inventory needs to be retrieved, will be overwritten by --aws-vault if not specified")
    public String vaultName;

    @Parameter(names = "--id", description = "The ID of the Job with InventoryRetrieval type")
    public String id;

    @Parameter(required = true, names = "--journal", description = "Where to save the data retrieved from the inventory")
    public String journal;

    @Parameter(names = "--metadata", description = "Available: mt2, fgv2")
    public String metadata = Constants.DEFAULT_PARSER_METADATA;

    @Parameter(names = "--raw", description = "Store the glacier full data instead of the parsed one, useful for creating new metadata parsers")
    public Boolean raw = false;

    public CommandInventoryDownload() {
	super(CliCommands.InventoryDownload);
    }

}

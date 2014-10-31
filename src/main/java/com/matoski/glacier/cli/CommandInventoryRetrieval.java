package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "inventory-retrieve", commandDescription = "Initiate an inventory retrieval")
public class CommandInventoryRetrieval extends GenericCommand {

    @Parameter(names = "--vault", description = "The name of the vault for whom the inventory needs to be retrieved, will be overwriten by --aws-vault if not specified")
    public String vaultName;

    @Parameter(names = "--wait", description = "Wait for completion of the command, this sets up a SQS/SNS so it can be told when to job is finished")
    public Boolean wait = false;

    public CommandInventoryRetrieval() {
	super(CliCommands.InventoryRetrieve);
    }

}

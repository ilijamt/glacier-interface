package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "list-multipart-uploads", commandDescription = "List all the present multipart uploads for the vault")
public class CommandListMultipartUploads extends GenericCommand {

    @Parameter(names = "--vault", description = "The name of the vault from which the multipart uploads will be retrieved, will be overwriten by --aws-vault if not specified")
    public String vaultName;

    @Parameter(names = "--cancel", description = "Cancels all the multipart uploads")
    public Boolean cancel = false;

    public CommandListMultipartUploads() {
	super(CliCommands.ListMultipartUploads);
    }

}

package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "vault-job-info",
        commandDescription = "Gets the information about the job in question")
public class CommandVaultJobInfo extends GenericCommand {

    @Parameter(
            names = "--vault",
            description = "The name of the vault from which the jobs will be retrieved, will be overwritten by --aws-vault if not specified")
    public String vaultName;

    @Parameter(required = true, names = "--id", description = "The ID of the Job")
    public String id;

    public CommandVaultJobInfo() {
        super(CliCommands.VaultJobInfo);
    }

}

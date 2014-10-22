package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "list-vault-jobs", commandDescription = "List all the present jobs in the system")
public class CommandListVaultJobs {

    @Parameter(names = "--vault", description = "The name of the vault from which the jobs will be retrieved, will be overwriten by --aws-vault if not specified")
    public String vaultName;

}

package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "list-vault-jobs", commandDescription = "List all the present jobs in the system")
public class CommandListVaultJobs extends GenericCommand {

  @Parameter(names = "--vault", description = "The name of the vault from which the jobs will be retrieved, will be overwritten by --aws-vault if not specified")
  public String vaultName;

  @Parameter(names = "--full", description = "Display full details for the jobs")
  public Boolean fullDetails = false;

  public CommandListVaultJobs() {
    super(CliCommands.ListVaultJobs);
  }

}

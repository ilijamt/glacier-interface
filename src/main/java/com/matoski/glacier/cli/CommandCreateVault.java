package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "create-vault", commandDescription = "Creates a new vault on Amazon Glacier")
public class CommandCreateVault {

	@Parameter(names = "--name", description = "The name of the vault to be created", required = true)
	public String vaultName;

}

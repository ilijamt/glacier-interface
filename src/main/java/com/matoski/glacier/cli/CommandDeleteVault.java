package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "delete-vault", commandDescription = "Deletes the vault from Amazon Glacier, this will not work if the vault is not empty, so you will have to delete everything in the vault before you can delete the vault")
public class CommandDeleteVault {

	@Parameter(names = "--name", description = "The name of the vault to be deleted", required = true)
	public String vaultName;

}

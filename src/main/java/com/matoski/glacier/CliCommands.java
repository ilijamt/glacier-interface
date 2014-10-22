package com.matoski.glacier;

public enum CliCommands {

    Help("help"), ListVaults("list-vaults"), CreateVault("create-vault"), DeleteVault(
	    "delete-vault"), ListVaultJobs("list-vault-jobs");

    private String propertyName;

    CliCommands(String propName) {
	this.propertyName = propName;
    }

    public String getPropertyName() {
	return propertyName;
    }

    static CliCommands from(String x) {
	for (CliCommands currentType : CliCommands.values()) {
	    if (x.equals(currentType.getPropertyName())) {
		return currentType;
	    }
	}
	// default command
	return CliCommands.Help;
    }

}

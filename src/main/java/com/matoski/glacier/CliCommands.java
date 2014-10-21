package com.matoski.glacier;

public enum CliCommands {

	Help("help"), ListVaults("list-vaults"), CreateVault("create-vault"), DeleteVault(
			"delete-vault");

	private String propertyName;

	CliCommands(String propName) {
		this.propertyName = propName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	static CliCommands from(String x) throws Exception {
		for (CliCommands currentType : CliCommands.values()) {
			if (x.equals(currentType.getPropertyName())) {
				return currentType;
			}
		}
		throw new Exception("Unmatched Type: " + x);
	}

}

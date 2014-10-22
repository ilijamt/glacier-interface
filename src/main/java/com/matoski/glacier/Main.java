package com.matoski.glacier;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.beust.jcommander.JCommander;
import com.google.gson.JsonSyntaxException;
import com.matoski.glacier.cli.Arguments;
import com.matoski.glacier.cli.CommandCreateVault;
import com.matoski.glacier.cli.CommandDeleteVault;
import com.matoski.glacier.cli.CommandHelp;
import com.matoski.glacier.cli.CommandListVaults;
import com.matoski.glacier.commands.CreateVaultCommand;
import com.matoski.glacier.commands.ListVaultsCommand;
import com.matoski.glacier.pojo.Config;

public class Main {

	public static void main(String[] args) {

		System.out.println("Glacier Interface, Copyright 2014, Ilija Matoski");
		System.out.println();

		JCommander commander = null;
		Arguments arguments = new Arguments();
		CommandHelp commandHelp = new CommandHelp();
		CommandListVaults commandListVaults = new CommandListVaults();
		CommandCreateVault commandCreateVault = new CommandCreateVault();
		CommandDeleteVault commandDeleteVault = new CommandDeleteVault();

		try {
			commander = new JCommander(arguments);
			commander.addCommand(commandHelp);
			commander.addCommand(commandListVaults);
			commander.addCommand(commandCreateVault);
			commander.addCommand(commandDeleteVault);
			commander.parse(args);
		} catch (Exception e) {
			commander.usage();
			System.out.print("ERROR: ");
			System.out.println(e.getMessage());
			System.exit(1);
		}

		Config config = null;
		Boolean hasConfig = !(null == arguments.config);
		String command = commander.getParsedCommand();

		if (hasConfig) {
			try {
				config = Config.fromFile(arguments.config);
				config.merge(arguments);
			} catch (JsonSyntaxException e) {
				System.out
						.println("ERROR: Invalid format in the configuration file");
				System.exit(1);
			} catch (FileNotFoundException e) {
				System.out.println("ERROR: Config file cannot be found");
				System.exit(1);
			} catch (IOException e) {
				System.out.println("ERROR: Config file cannot be found");
				System.exit(1);
			}
		} else {
			config = Config.fromArguments(arguments);
		}

		Boolean validCommand = (null != command);
		Boolean validConfig = false;
		Boolean isVaultRequired = false;
		CliCommands cliCommand = null;

		if (null != arguments.createConfig) {
			try {
				config.createConfigurationFile(arguments.createConfig);
			} catch (IOException e) {
				System.out.println("ERROR: Failed to write the configuration");
				e.printStackTrace();
				System.exit(1);
			}
		}

		if (validCommand) {
			cliCommand = CliCommands.from(command);
			validConfig = config.valid(Main.isVaultRequired(cliCommand));
		}

		if (!validCommand && !validConfig) {

			commander.usage();

			if (!validConfig) {

				System.out
						.println("ERROR: Missing one or more required parameters");
				if (null == config.getKey()) {
					System.out.println("\t--aws-key");
				}

				if (null == config.getSecretKey()) {
					System.out.println("\t--aws-secret-key");
				}

				if (null == config.getRegion()) {
					System.out.println("\t--aws-region");
				}

				if (isVaultRequired && (null == config.getVault())) {
					System.out.println("\t--aws-vault");
				}
			}

		} else {

			try {

				System.out.println("Starting to process command");

				switch (cliCommand) {
				case Help:
					commander.usage();
					break;

				case ListVaults:
					new ListVaultsCommand(config, commandListVaults).run();
					break;

				case CreateVault:
					new CreateVaultCommand(config, commandCreateVault).run();
					break;

				case DeleteVault:
					break;
				}

			} catch (Exception e) {
				System.out.println(e);
				System.exit(1);
			}
		}

		System.out.println();
		System.out.println("Finished");

	}

	/**
	 * Is a vault parameter required ?
	 * 
	 * @param command
	 * @return
	 */
	public static boolean isVaultRequired(CliCommands command) {

		switch (command) {
		default:
			break;
		}

		return false;

	}

}

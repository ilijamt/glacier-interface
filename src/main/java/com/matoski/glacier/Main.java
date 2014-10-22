package com.matoski.glacier;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.matoski.glacier.cli.Arguments;
import com.matoski.glacier.cli.CommandCreateVault;
import com.matoski.glacier.cli.CommandDeleteVault;
import com.matoski.glacier.cli.CommandHelp;
import com.matoski.glacier.cli.CommandListVaults;
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

		Boolean validConfig = config.valid();
		if (null == command || !validConfig) {

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
				if (null == config.getVault()) {
					System.out.println("\t--aws-vault");
				}
			}

		} else {

			try {

				switch (CliCommands.from(command)) {
				case Help:
					commander.usage();
					break;

				case ListVaults:
					new ListVaultsCommand(config, commandListVaults).run();
					break;

				case CreateVault:
					break;

				case DeleteVault:
					break;

				default:
					commander.usage();
					break;
				}
			} catch (Exception e) {
				System.out.println(e);
				System.exit(1);
			}
		}

	}

}

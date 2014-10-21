package com.matoski.glacier;

import com.beust.jcommander.JCommander;
import com.matoski.glacier.cli.Arguments;
import com.matoski.glacier.cli.CommandCreateVault;
import com.matoski.glacier.cli.CommandDeleteVault;
import com.matoski.glacier.cli.CommandHelp;
import com.matoski.glacier.cli.CommandListVaults;
import com.matoski.glacier.commands.ListVaultsCommand;
import com.matoski.glacier.pojo.Config;

public class Main {

	public static void main(String[] args) {

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

		final Config config = new Config();

		try {
			switch (CliCommands.from(commander.getParsedCommand())) {
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

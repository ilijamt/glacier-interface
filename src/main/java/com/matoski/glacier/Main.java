package com.matoski.glacier;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.beust.jcommander.JCommander;
import com.google.gson.JsonSyntaxException;
import com.matoski.glacier.cli.Arguments;
import com.matoski.glacier.cli.CommandCreateVault;
import com.matoski.glacier.cli.CommandDeleteVault;
import com.matoski.glacier.cli.CommandHelp;
import com.matoski.glacier.cli.CommandInventoryDownload;
import com.matoski.glacier.cli.CommandInventoryRetrieval;
import com.matoski.glacier.cli.CommandListVaultJobs;
import com.matoski.glacier.cli.CommandListVaults;
import com.matoski.glacier.cli.CommandVaultJobInfo;
import com.matoski.glacier.commands.CreateVaultCommand;
import com.matoski.glacier.commands.DeleteVaultCommand;
import com.matoski.glacier.commands.InventoryDownloadCommand;
import com.matoski.glacier.commands.InventoryRetrievalCommand;
import com.matoski.glacier.commands.ListVaultJobsCommand;
import com.matoski.glacier.commands.ListVaultsCommand;
import com.matoski.glacier.commands.VaultJobInfoCommand;
import com.matoski.glacier.errors.VaultNameNotPresentException;
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
	CommandListVaultJobs commandListVaultJobs = new CommandListVaultJobs();
	CommandVaultJobInfo commandVaultJobInfo = new CommandVaultJobInfo();
	CommandInventoryRetrieval commandInventoryRetrieval = new CommandInventoryRetrieval();
	CommandInventoryDownload commandInventoryDownload = new CommandInventoryDownload();

	try {

	    commander = new JCommander(arguments);
	    commander.addCommand(commandHelp);
	    commander.addCommand(commandListVaults);
	    commander.addCommand(commandCreateVault);
	    commander.addCommand(commandDeleteVault);
	    commander.addCommand(commandListVaultJobs);
	    commander.addCommand(commandVaultJobInfo);
	    commander.addCommand(commandInventoryRetrieval);
	    commander.addCommand(commandInventoryDownload);

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
	    validConfig = config.valid(false);
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
		    new DeleteVaultCommand(config, commandDeleteVault).run();
		    break;

		case ListVaultJobs:
		    new ListVaultJobsCommand(config, commandListVaultJobs)
			    .run();
		    break;

		case VaultJobInfo:
		    new VaultJobInfoCommand(config, commandVaultJobInfo).run();
		    break;

		case InventoryRetrieve:
		    new InventoryRetrievalCommand(config,
			    commandInventoryRetrieval).run();
		    break;

		case InventoryDownload:
		    new InventoryDownloadCommand(config,
			    commandInventoryDownload).run();
		    break;
		}

	    } catch (VaultNameNotPresentException e) {
		System.out
			.println("ERROR: Missing one or more required parameters");
		System.out.println("\t--aws-vault");
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
	case ListVaultJobs:
	case CreateVault:
	case DeleteVault:
	case VaultJobInfo:
	case InventoryRetrieve:
	    return true;
	default:
	    break;
	}

	return false;

    }

}

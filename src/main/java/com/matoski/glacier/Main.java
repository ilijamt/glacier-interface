package com.matoski.glacier;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.beust.jcommander.JCommander;
import com.google.gson.JsonSyntaxException;
import com.matoski.glacier.cli.Arguments;
import com.matoski.glacier.cli.CommandCreateVault;
import com.matoski.glacier.cli.CommandDeleteArchive;
import com.matoski.glacier.cli.CommandDeleteVault;
import com.matoski.glacier.cli.CommandHelp;
import com.matoski.glacier.cli.CommandInventoryDownload;
import com.matoski.glacier.cli.CommandInventoryRetrieval;
import com.matoski.glacier.cli.CommandListVaultJobs;
import com.matoski.glacier.cli.CommandListVaults;
import com.matoski.glacier.cli.CommandUploadArchive;
import com.matoski.glacier.cli.CommandVaultJobInfo;
import com.matoski.glacier.commands.CreateVaultCommand;
import com.matoski.glacier.commands.DeleteArchiveCommand;
import com.matoski.glacier.commands.DeleteVaultCommand;
import com.matoski.glacier.commands.InventoryDownloadCommand;
import com.matoski.glacier.commands.InventoryRetrievalCommand;
import com.matoski.glacier.commands.ListVaultJobsCommand;
import com.matoski.glacier.commands.ListVaultsCommand;
import com.matoski.glacier.commands.UploadArchiveCommand;
import com.matoski.glacier.commands.VaultJobInfoCommand;
import com.matoski.glacier.enums.CliCommands;
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
	CommandDeleteArchive commandDeleteArchive = new CommandDeleteArchive();
	CommandUploadArchive commandUploadArchive = new CommandUploadArchive();

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
	    commander.addCommand(commandDeleteArchive);
	    commander.addCommand(commandUploadArchive);

	    commander.parse(args);

	} catch (Exception e) {
	    commander.usage();
	    System.err.print("ERROR: ");
	    System.err.println(e.getMessage());
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
		System.err
			.println("ERROR: Invalid format in the configuration file");
		System.exit(1);
	    } catch (FileNotFoundException e) {
		System.err.println("ERROR: Config file cannot be found");
		System.exit(1);
	    } catch (IOException e) {
		System.err.println("ERROR: Config file cannot be found");
		System.exit(1);
	    }
	} else {
	    config = Config.fromArguments(arguments);
	}

	Boolean validCommand = (null != command);
	Boolean validConfig = false;
	CliCommands cliCommand = CliCommands.Help;

	if (null != arguments.createConfig) {
	    try {
		config.createConfigurationFile(arguments.createConfig);
	    } catch (IOException e) {
		System.err.println("ERROR: Failed to write the configuration");
		e.printStackTrace();
		System.exit(1);
	    }
	}

	if (validCommand) {
	    cliCommand = CliCommands.from(command);
	    validConfig = config.valid(false);
	}

	if (!validCommand || !validConfig) {

	    commander.usage();

	    if (cliCommand != CliCommands.Help && !validConfig) {

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

		case DeleteArchive:
		    new DeleteArchiveCommand(config, commandDeleteArchive)
			    .run();
		    break;

		case UploadArchive:
		    new UploadArchiveCommand(config, commandUploadArchive);
		    break;

		default:
		    break;
		}

	    } catch (VaultNameNotPresentException e) {
		System.err
			.println("ERROR: Missing one or more required parameters");
		System.err.println("\t--aws-vault or --vault");
	    } catch (Exception e) {
		System.err.println(e);
		System.exit(1);
	    }
	}

	System.out.println();
	System.out.println("Finished");

    }

    /**
     * Is a vault parameter required ?
     * 
     * @deprecated Handling is used in each job separately for vault name
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
	case DeleteArchive:
	case UploadArchive:
	    return true;
	default:
	    break;
	}

	return false;

    }

}

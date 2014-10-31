package com.matoski.glacier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import com.beust.jcommander.JCommander;
import com.google.gson.JsonSyntaxException;
import com.matoski.glacier.cli.Arguments;
import com.matoski.glacier.cli.CommandAbortMultipartUpload;
import com.matoski.glacier.cli.CommandCreateVault;
import com.matoski.glacier.cli.CommandDeleteArchive;
import com.matoski.glacier.cli.CommandDeleteVault;
import com.matoski.glacier.cli.CommandHelp;
import com.matoski.glacier.cli.CommandInventoryDownload;
import com.matoski.glacier.cli.CommandInventoryRetrieval;
import com.matoski.glacier.cli.CommandListMultipartUploads;
import com.matoski.glacier.cli.CommandListVaultJobs;
import com.matoski.glacier.cli.CommandListVaults;
import com.matoski.glacier.cli.CommandMultipartUploadInfo;
import com.matoski.glacier.cli.CommandUploadArchive;
import com.matoski.glacier.cli.CommandVaultJobInfo;
import com.matoski.glacier.commands.AbortMultipartUploadCommand;
import com.matoski.glacier.commands.CreateVaultCommand;
import com.matoski.glacier.commands.DeleteArchiveCommand;
import com.matoski.glacier.commands.DeleteVaultCommand;
import com.matoski.glacier.commands.InventoryDownloadCommand;
import com.matoski.glacier.commands.InventoryRetrievalCommand;
import com.matoski.glacier.commands.ListMultipartUploadsCommand;
import com.matoski.glacier.commands.ListVaultJobsCommand;
import com.matoski.glacier.commands.ListVaultsCommand;
import com.matoski.glacier.commands.MultipartUploadInfoCommand;
import com.matoski.glacier.commands.UploadArchiveCommand;
import com.matoski.glacier.commands.VaultJobInfoCommand;
import com.matoski.glacier.enums.CliCommands;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;

public class Main {

    final private static HashMap<Integer, Object> commands = new HashMap<Integer, Object>();

    public static void init() {

	commands.put(CliCommands.Help.ordinal(), new CommandHelp());
	commands.put(CliCommands.ListVaults.ordinal(), new CommandListVaults());
	commands.put(CliCommands.CreateVault.ordinal(),
		new CommandCreateVault());
	commands.put(CliCommands.DeleteVault.ordinal(),
		new CommandDeleteVault());
	commands.put(CliCommands.ListVaultJobs.ordinal(),
		new CommandListVaultJobs());
	commands.put(CliCommands.VaultJobInfo.ordinal(),
		new CommandVaultJobInfo());
	commands.put(CliCommands.InventoryRetrieve.ordinal(),
		new CommandInventoryRetrieval());
	commands.put(CliCommands.InventoryDownload.ordinal(),
		new CommandInventoryDownload());
	commands.put(CliCommands.UploadArchive.ordinal(),
		new CommandUploadArchive());
	commands.put(CliCommands.DeleteArchive.ordinal(),
		new CommandDeleteArchive());
	commands.put(CliCommands.ListMultipartUploads.ordinal(),
		new CommandListMultipartUploads());
	commands.put(CliCommands.MultipartUploadInfo.ordinal(),
		new CommandMultipartUploadInfo());
	commands.put(CliCommands.AbortMultipartUpload.ordinal(),
		new CommandAbortMultipartUpload());
    }

    public static void main(String[] args) {

	System.out.println("Glacier Interface, Copyright 2014, Ilija Matoski");
	System.out.println();

	JCommander commander = null;
	Arguments arguments = new Arguments();

	init();

	try {

	    commander = new JCommander(arguments);
	    for (Entry<Integer, Object> entry : commands.entrySet()) {
		commander.addCommand(entry.getValue());
	    }

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

	if (validCommand) {
	    cliCommand = CliCommands.from(command);
	    validConfig = config.valid(false);
	}

	System.out.println(String.format("Current working dir: %s",
		config.getDirectory()));
	System.out.println(String.format("Command: %s", cliCommand));
	System.out.println();

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
		    new ListVaultsCommand(config,
			    (CommandListVaults) commands.get(cliCommand
				    .ordinal())).run();
		    break;

		case CreateVault:
		    new CreateVaultCommand(config,
			    (CommandCreateVault) commands.get(cliCommand
				    .ordinal())).run();
		    break;

		case DeleteVault:
		    new DeleteVaultCommand(config,
			    (CommandDeleteVault) commands.get(cliCommand
				    .ordinal())).run();
		    break;

		case ListVaultJobs:
		    new ListVaultJobsCommand(config,
			    (CommandListVaultJobs) commands.get(cliCommand
				    .ordinal())).run();
		    break;

		case VaultJobInfo:
		    new VaultJobInfoCommand(config,
			    (CommandVaultJobInfo) commands.get(cliCommand
				    .ordinal())).run();
		    break;

		case InventoryRetrieve:
		    new InventoryRetrievalCommand(config,
			    (CommandInventoryRetrieval) commands.get(cliCommand
				    .ordinal())).run();
		    break;

		case InventoryDownload:
		    new InventoryDownloadCommand(config,
			    (CommandInventoryDownload) commands.get(cliCommand
				    .ordinal())).run();
		    break;

		case DeleteArchive:
		    new DeleteArchiveCommand(config,
			    (CommandDeleteArchive) commands.get(cliCommand
				    .ordinal())).run();
		    break;

		case UploadArchive:
		    new UploadArchiveCommand(config,
			    (CommandUploadArchive) commands.get(cliCommand
				    .ordinal())).run();
		    break;

		case ListMultipartUploads:
		    new ListMultipartUploadsCommand(config,
			    (CommandListMultipartUploads) commands
				    .get(cliCommand.ordinal())).run();
		    break;

		case MultipartUploadInfo:
		    new MultipartUploadInfoCommand(config,
			    (CommandMultipartUploadInfo) commands
				    .get(cliCommand.ordinal())).run();
		    break;

		case AbortMultipartUpload:
		    new AbortMultipartUploadCommand(config,
			    (CommandAbortMultipartUpload) commands
				    .get(cliCommand.ordinal())).run();
		    break;

		default:
		    break;
		}
	    } catch (RuntimeException e) {
		System.err.println(String.format("ERROR: %s", e.getMessage()));
	    } catch (RegionNotSupportedException e) {
		System.err.println(String.format(
			"Service glacier not support in region: %s",
			config.getRegion()));
	    } catch (VaultNameNotPresentException e) {
		System.err
			.println("ERROR: Missing one or more required parameters");
		System.err.println("\t--aws-vault or --vault");
	    } catch (Exception e) {
		System.err.println(e);
		System.exit(1);
	    }
	}

	if (null != arguments.createConfig) {
	    try {
		config.createConfigurationFile(arguments.createConfig);
		System.out.println(String.format(
			"Created a configuration file: %s",
			arguments.createConfig));
	    } catch (IOException e) {
		System.err.println("ERROR: Failed to write the configuration");
		e.printStackTrace();
		System.exit(1);
	    }
	}

	System.out.println();
	System.out.println("Finished");

    }

}

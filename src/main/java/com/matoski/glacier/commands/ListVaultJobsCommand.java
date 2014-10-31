package com.matoski.glacier.commands;

import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.glacier.model.GlacierJobDescription;
import com.amazonaws.services.glacier.model.ListJobsRequest;
import com.amazonaws.services.glacier.model.ListJobsResult;
import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandListVaultJobs;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;

public class ListVaultJobsCommand extends AbstractCommand<CommandListVaultJobs> {

    public ListVaultJobsCommand(Config config, CommandListVaultJobs command)
	    throws VaultNameNotPresentException, RegionNotSupportedException {
	super(config, command);

	if ((null == command.vaultName || command.vaultName.isEmpty())
		&& (null == config.getVault() || config.getVault().isEmpty())) {
	    throw new VaultNameNotPresentException();
	}

	if ((null == command.vaultName) || command.vaultName.isEmpty()) {
	    command.vaultName = config.getVault();
	}
    }

    @Override
    public void run() {

	System.out.println("START: list-vault-jobs\n");

	try {

	    ListJobsRequest request = new ListJobsRequest()
		    .withVaultName(command.vaultName);
	    ListJobsResult result = this.client.listJobs(request);

	    List<GlacierJobDescription> jobs = result.getJobList();

	    if (jobs.isEmpty()) {
		System.out.println(String.format(
			"No available jobs for vault: %s", command.vaultName));
	    } else {
		for (GlacierJobDescription job : jobs) {

		    if (command.fullDetails) {

			System.out.println(String.format("%1$25s : %2$s",
				"Action", job.getAction()));
			System.out.println(String.format("%1$25s : %2$s",
				"Archive Id", job.getArchiveId()));
			System.out.println(String.format("%1$25s : %2$s",
				"Archive Size In Bytes",
				job.getArchiveSizeInBytes()));
			System.out.println(String.format("%1$25s : %2$s",
				"Completed", job.getCompleted()));
			System.out.println(String.format("%1$25s : %2$s",
				"CompletionDate", job.getCompletionDate()));
			System.out.println(String.format("%1$25s : %2$s",
				"CreationDate", job.getCreationDate()));
			System.out.println(String.format("%1$25s : %2$s",
				"Inventory Size In Bytes",
				job.getInventorySizeInBytes()));
			System.out.println(String.format("%1$25s : %2$s",
				"Job Description", job.getJobDescription()));
			System.out.println(String.format("%1$25s : %2$s",
				"Job Id", job.getJobId()));
			System.out.println(String.format("%1$25s : %2$s",
				"SHA256 Tree Hash", job.getSHA256TreeHash()));
			System.out.println(String.format("%1$25s : %2$s",
				"SNS Topic", job.getSNSTopic()));
			System.out.println(String.format("%1$25s : %2$s",
				"Status Code", job.getStatusCode()));
			System.out.println(String.format("%1$25s : %2$s",
				"Status Message", job.getStatusMessage()));
			System.out.println(String.format("%1$25s : %2$s",
				"Vault ARN", job.getVaultARN()));
			System.out.println();

		    } else {
			System.out
				.println(String
					.format("%s [Action: %s] %s (Completed: %s, On: %s) ID: %s",
						job.getCreationDate(),
						job.getAction(),
						job.getStatusCode(),
						job.getCompleted(),
						job.getCompletionDate(),
						job.getJobId()));
		    }
		}
	    }

	} catch (AmazonServiceException e) {
	    switch (e.getErrorCode()) {
	    case "InvalidSignatureException":
		System.err
			.println(String
				.format("ERROR: Invalid credentials, check you key and secret key."));
		break;
	    default:
		System.err
			.println(String
				.format("ERROR: Failed to retrieve the jobs for vault: %s\n\t%s",
					command.vaultName, e.getMessage()));
		break;
	    }
	} catch (AmazonClientException e) {
	    System.err.println(String.format(
		    "ERROR: Cannot connect to the amazon web services.\n\t%s",
		    e.getMessage()));
	}

	System.out.println("\nEND: list-vault-jobs");
    }
}

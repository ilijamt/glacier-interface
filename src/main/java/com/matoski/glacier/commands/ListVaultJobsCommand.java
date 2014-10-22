package com.matoski.glacier.commands;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.glacier.model.GlacierJobDescription;
import com.amazonaws.services.glacier.model.ListJobsRequest;
import com.amazonaws.services.glacier.model.ListJobsResult;
import com.matoski.glacier.cli.CommandListVaultJobs;
import com.matoski.glacier.pojo.Config;

public class ListVaultJobsCommand extends AbstractCommand {

    protected CommandListVaultJobs command;

    public ListVaultJobsCommand(Config config, CommandListVaultJobs command) {
	super(config);
	this.command = command;
	if ((null == command.vaultName) || command.vaultName.isEmpty()) {
	    command.vaultName = config.getVault();
	}
    }

    @Override
    public void run() {

	System.out.println("START: list-jobs\n");

	try {

	    ListJobsRequest request = new ListJobsRequest()
		    .withVaultName(command.vaultName);
	    ListJobsResult result = this.client.listJobs(request);

	    for (GlacierJobDescription job : result.getJobList()) {
		System.out.println();
		System.out.println("Action               : " + job.getAction());
		System.out.println("ArchiveId            : "
			+ job.getArchiveId());
		System.out.println("ArchiveSizeInBytes   : "
			+ job.getArchiveSizeInBytes());
		System.out.println("Completed            : "
			+ job.getCompleted());
		System.out.println("CompletionDate       : "
			+ job.getCompletionDate());
		System.out.println("CreationDate         : "
			+ job.getCreationDate());
		System.out.println("InventorySizeInBytes : "
			+ job.getInventorySizeInBytes());
		System.out.println("JobDescription       : "
			+ job.getJobDescription());
		System.out.println("JobId                : " + job.getJobId());
		System.out.println("SHA256TreeHash       : "
			+ job.getSHA256TreeHash());
		System.out.println("SNSTopic             : "
			+ job.getSNSTopic());
		System.out.println("StatusCode           : "
			+ job.getStatusCode());
		System.out.println("StatusMessage        : "
			+ job.getStatusMessage());
		System.out.println("VaultARN             : "
			+ job.getVaultARN());
	    }

	} catch (AmazonServiceException e) {
	    switch (e.getErrorCode()) {
	    case "InvalidSignatureException":
		System.out
			.println(String
				.format("ERROR: Invalid credentials, check you key and secret key."));
		break;
	    default:
		System.out
			.println(String
				.format("ERROR: Failed to retrieve the jobs for vault: %s\n\t%s",
					command.vaultName, e.getMessage()));
		break;
	    }
	} catch (AmazonClientException e) {
	    System.out.println(String.format(
		    "ERROR: Cannot connect to the amazon web services.\n\t%s",
		    e.getMessage()));
	}

	System.out.println("\nEND: list-jobs");
    }

}

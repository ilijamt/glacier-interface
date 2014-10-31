package com.matoski.glacier.commands;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.glacier.model.DescribeJobRequest;
import com.amazonaws.services.glacier.model.DescribeJobResult;
import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandVaultJobInfo;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;

public class VaultJobInfoCommand extends AbstractCommand<CommandVaultJobInfo> {

    public VaultJobInfoCommand(Config config, CommandVaultJobInfo command)
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

	System.out.println("START: vault-job-info\n");

	try {

	    DescribeJobRequest request = new DescribeJobRequest()
		    .withVaultName(command.vaultName).withJobId(command.id);

	    DescribeJobResult job = client.describeJob(request);

	    System.out.println(String.format("%1$25s : %2$s", "Action",
		    job.getAction()));
	    System.out.println(String.format("%1$25s : %2$s", "Archive Id",
		    job.getArchiveId()));
	    System.out.println(String.format("%1$25s : %2$s",
		    "Archive Size In Bytes", job.getArchiveSizeInBytes()));
	    System.out.println(String.format("%1$25s : %2$s", "Completed",
		    job.getCompleted()));
	    System.out.println(String.format("%1$25s : %2$s", "CompletionDate",
		    job.getCompletionDate()));
	    System.out.println(String.format("%1$25s : %2$s", "CreationDate",
		    job.getCreationDate()));
	    System.out.println(String.format("%1$25s : %2$s",
		    "Inventory Size In Bytes", job.getInventorySizeInBytes()));
	    System.out.println(String.format("%1$25s : %2$s",
		    "Job Description", job.getJobDescription()));
	    System.out.println(String.format("%1$25s : %2$s", "Job Id",
		    job.getJobId()));
	    System.out.println(String.format("%1$25s : %2$s",
		    "SHA256 Tree Hash", job.getSHA256TreeHash()));
	    System.out.println(String.format("%1$25s : %2$s", "SNS Topic",
		    job.getSNSTopic()));
	    System.out.println(String.format("%1$25s : %2$s", "Status Code",
		    job.getStatusCode()));
	    System.out.println(String.format("%1$25s : %2$s", "Status Message",
		    job.getStatusMessage()));
	    System.out.println(String.format("%1$25s : %2$s", "Vault ARN",
		    job.getVaultARN()));
	    System.out.println();

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
				.format("ERROR: Failed to retrieve the jobs for vault: %s\n\n%s",
					command.vaultName, e.getMessage()));
		break;
	    }
	} catch (AmazonClientException e) {
	    System.err.println(String.format(
		    "ERROR: Cannot connect to the amazon web services.\n\t%s",
		    e.getMessage()));
	}

	System.out.println("\nEND: vault-job-info");
    }
}

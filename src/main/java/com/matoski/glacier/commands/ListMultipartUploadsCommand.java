package com.matoski.glacier.commands;

import java.util.List;

import org.apache.commons.io.FileUtils;

import com.amazonaws.services.glacier.model.DescribeVaultOutput;
import com.amazonaws.services.glacier.model.ListVaultsRequest;
import com.amazonaws.services.glacier.model.ListVaultsResult;
import com.amazonaws.services.glacier.model.UploadListElement;
import com.matoski.glacier.cli.CommandListMultipartUploads;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.util.upload.AmazonGlacierUploadUtil;

public class ListMultipartUploadsCommand extends
	AbstractCommand<CommandListMultipartUploads> {

    public ListMultipartUploadsCommand(Config config,
	    CommandListMultipartUploads command)
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

	System.out.println("START: list-multipart-uploads\n");

	AmazonGlacierUploadUtil upload = new AmazonGlacierUploadUtil(
		credentials, client, region);
	Boolean canceled = false;

	List<UploadListElement> list = upload
		.ListMultipartUploads(command.vaultName);

	System.out.println(String.format("Cancel all multipart uploads: %s",
		command.cancel));
	System.out.println(String.format(
		"Total available multipart uploads: %s\n", list.size()));

	for (UploadListElement element : list) {

	    System.out.println(String.format("%1$20s: %2$s", "ID",
		    element.getMultipartUploadId()));
	    System.out.println(String.format("%1$20s: %2$s", "ARN",
		    element.getVaultARN()));
	    System.out.println(String.format("%1$20s: %2$s", "Creation date",
		    element.getCreationDate()));
	    System.out.println(String.format("%1$20s: %2$s", "Part size",
		    element.getPartSizeInBytes()));

	    if (command.cancel) {
		canceled = upload.CancelMultipartUpload(
			element.getMultipartUploadId(), command.vaultName);
		System.out.println(String.format("%1$20s: %2$s", "Canceled",
			canceled));
	    }
	    
	    System.out.println();
	}

	System.out.println("END: list-multipart-uploads");
    }
}

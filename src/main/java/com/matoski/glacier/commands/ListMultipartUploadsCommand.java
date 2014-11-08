package com.matoski.glacier.commands;

import java.util.List;

import com.amazonaws.services.glacier.model.ListPartsResult;
import com.amazonaws.services.glacier.model.PartListElement;
import com.amazonaws.services.glacier.model.UploadListElement;
import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandListMultipartUploads;
import com.matoski.glacier.enums.Metadata;
import com.matoski.glacier.errors.InvalidMetadataException;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.interfaces.IGlacierInterfaceMetadata;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.util.Parser;
import com.matoski.glacier.util.upload.AmazonGlacierUploadUtil;

/**
 * List's all multipart uploads 
 * 
 * @author ilijamt
 *
 */
public class ListMultipartUploadsCommand extends AbstractCommand<CommandListMultipartUploads> {

    /**
     * Metadata 
     */
    final private Metadata metadata;

    /**
     * Constructor 
     * 
     * @param config
     * @param command
     * @throws VaultNameNotPresentException
     * @throws RegionNotSupportedException
     */
    public ListMultipartUploadsCommand(Config config, CommandListMultipartUploads command) throws VaultNameNotPresentException,
	    RegionNotSupportedException {
	super(config, command);

	if ((null == command.vaultName || command.vaultName.isEmpty()) && (null == config.getVault() || config.getVault().isEmpty())) {
	    throw new VaultNameNotPresentException();
	}

	if ((null == command.vaultName) || command.vaultName.isEmpty()) {
	    command.vaultName = config.getVault();
	}

	metadata = Metadata.from(command.metadata);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

	System.out.println("START: list-multipart-uploads\n");

	AmazonGlacierUploadUtil upload = new AmazonGlacierUploadUtil(credentials, client, region);
	Boolean canceled = false;

	List<UploadListElement> list = upload.ListMultipartUploads(command.vaultName);

	System.out.println(String.format("Cancel all multipart uploads: %s", command.cancel));
	System.out.println(String.format("Total available multipart uploads: %s\n", list.size()));

	for (UploadListElement element : list) {

	    if (command.full) {

		ListPartsResult result = upload.GetMultipartUploadInfo(command.vaultName, element.getMultipartUploadId());

		System.out.println(String.format("%1$20s: %2$s", "ID", result.getMultipartUploadId()));
		System.out.println(String.format("%1$20s: %2$s", "ARN", result.getVaultARN()));
		System.out.println(String.format("%1$20s: %2$s", "Creation date", result.getCreationDate()));
		System.out.println(String.format("%1$20s: %2$s", "Part size", result.getPartSizeInBytes()));
		System.out.println(String.format("%1$20s: %2$s", "Description", result.getArchiveDescription()));
		try {
		    IGlacierInterfaceMetadata metadata = Parser.parse(this.metadata, element.getArchiveDescription());
		    System.out.println(String.format("%1$20s: %2$s", "Name", metadata.giGetName()));
		} catch (NullPointerException | InvalidMetadataException e) {
		    // nothing we can do, not the correct metadata
		}

		System.out.println(String.format("Total available parts for the upload: %s", result.getParts().size()));

		for (PartListElement partList : result.getParts()) {
		    System.out.println(String.format("%1$20s: %2$s", "Byte Range", partList.getRangeInBytes()));
		    System.out.println(String.format("%1$20s: %2$s", "SHA256 Tree Hash", partList.getSHA256TreeHash()));
		}

	    } else {

		System.out.println(String.format("%1$20s: %2$s", "ID", element.getMultipartUploadId()));
		System.out.println(String.format("%1$20s: %2$s", "ARN", element.getVaultARN()));
		System.out.println(String.format("%1$20s: %2$s", "Creation date", element.getCreationDate()));
		System.out.println(String.format("%1$20s: %2$s", "Part size", element.getPartSizeInBytes()));
		System.out.println(String.format("%1$20s: %2$s", "Description", element.getArchiveDescription()));

		try {
		    IGlacierInterfaceMetadata metadata = Parser.parse(this.metadata, element.getArchiveDescription());
		    System.out.println(String.format("%1$20s: %2$s", "Name", metadata.giGetName()));
		} catch (NullPointerException | InvalidMetadataException e) {
		    // nothing we can do, not the correct metadata
		}

	    }

	    if (command.cancel) {
		canceled = upload.CancelMultipartUpload(element.getMultipartUploadId(), command.vaultName);
		System.out.println(String.format("%1$20s: %2$s", "Canceled", canceled));
	    }

	    System.out.println();
	}

	System.out.println("END: list-multipart-uploads");
    }
}

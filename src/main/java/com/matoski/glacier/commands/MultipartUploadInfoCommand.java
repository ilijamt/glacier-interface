package com.matoski.glacier.commands;

import com.amazonaws.services.glacier.model.ListPartsResult;
import com.amazonaws.services.glacier.model.PartListElement;
import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandMultipartUploadInfo;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.util.upload.AmazonGlacierUploadUtil;

/**
 * Show details about multipart info
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 *
 */
public class MultipartUploadInfoCommand extends AbstractCommand<CommandMultipartUploadInfo> {

  /**
   * Constructor.
   * 
   * @param config
   *          Application config
   * @param command
   *          The command configuration
   * 
   * @throws VaultNameNotPresentException
   *           Vault not present in config
   * @throws RegionNotSupportedException
   *           Region not supported
   */
  public MultipartUploadInfoCommand(Config config, CommandMultipartUploadInfo command)
      throws VaultNameNotPresentException, RegionNotSupportedException {
    super(config, command);

    if ((null == command.vaultName || command.vaultName.isEmpty())
        && (null == config.getVault() || config.getVault().isEmpty())) {
      throw new VaultNameNotPresentException();
    }

    if (null == command.vaultName || command.vaultName.isEmpty()) {
      command.vaultName = config.getVault();
    }

  }

  @Override
  public void run() {

    System.out.println("START: multipart-upload-info\n");

    AmazonGlacierUploadUtil upload = new AmazonGlacierUploadUtil(credentials, client, region);

    ListPartsResult result = upload.getMultipartUploadInfo(command.vaultName, command.multipartId);

    System.out.println(String.format("%1$20s: %2$s", "ID", result.getMultipartUploadId()));
    System.out.println(String.format("%1$20s: %2$s", "ARN", result.getVaultARN()));
    System.out.println(String.format("%1$20s: %2$s", "Creation date", result.getCreationDate()));
    System.out.println(String.format("%1$20s: %2$s", "Part size", result.getPartSizeInBytes()));
    System.out
        .println(String.format("%1$20s: %2$s", "Description", result.getArchiveDescription()));
    System.out.println();

    System.out.println(String.format("Total available parts for the upload: %s%n", result
        .getParts().size()));

    for (PartListElement element : result.getParts()) {

      System.out.println(String.format("%1$20s: %2$s", "Byte Range", element.getRangeInBytes()));
      System.out.println(String.format("%1$20s: %2$s", "SHA256 Tree Hash",
          element.getSHA256TreeHash()));
      System.out.println();

    }

    System.out.println("END: multipart-upload-info");
  }
}

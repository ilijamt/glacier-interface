package com.matoski.glacier.commands;

import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandAbortMultipartUpload;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.util.upload.AmazonGlacierUploadUtil;

/**
 * Aborts a multipart upload
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class AbortMultipartUploadCommand extends AbstractCommand<CommandAbortMultipartUpload> {

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
  public AbortMultipartUploadCommand(Config config, CommandAbortMultipartUpload command)
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

    System.out.println("START: abort-multipart-upload\n");

    AmazonGlacierUploadUtil upload = new AmazonGlacierUploadUtil(credentials, client, region);

    Boolean canceled = upload.cancelMultipartUpload(command.multipartId, command.vaultName);
    System.out.println(String.format("Multipart upload canceled: %s\n", canceled));

    System.out.println("END: abort-multipart-upload");
  }
}

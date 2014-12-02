package com.matoski.glacier.commands;

import java.io.IOException;
import java.util.Date;

import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandUploadArchive;
import com.matoski.glacier.enums.Metadata;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.pojo.journal.State;
import com.matoski.glacier.util.AmazonGlacierBaseUtil;
import com.matoski.glacier.util.upload.AmazonGlacierUploadUtil;

/**
 * Upload archive
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class UploadArchiveCommand extends AbstractCommand<CommandUploadArchive> {

  /**
   * Metadata.
   */
  protected Metadata metadata;

  /**
   * The helper utility for uploading.
   */
  protected AmazonGlacierUploadUtil upload;

  /**
   * The journal, we use this for storing the data.
   */
  protected State journal;

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
  public UploadArchiveCommand(Config config, CommandUploadArchive command)
      throws VaultNameNotPresentException, RegionNotSupportedException {
    super(config, command);

    Boolean validVaultName = null != command.vaultName;
    Boolean validVaultNameConfig = null != config.getVault();

    if (!validVaultName && !validVaultNameConfig) {
      throw new VaultNameNotPresentException();
    }

    if (validVaultNameConfig) {
      command.vaultName = config.getVault();
    }

    if (command.partSize % 2 != 0 && command.partSize != 1) {
      throw new IllegalArgumentException("Part size has to be a multiple of 2");
    }

    this.metadata = Metadata.from(command.metadata);
    this.upload = new AmazonGlacierUploadUtil(credentials, client, region);

    try {
      this.journal = State.load(command.journal);
    } catch (IOException e) {
      System.out.println(String.format("Creating a new journal: %s", command.journal));
      this.journal = new State();
      this.journal.setMetadata(metadata);
      this.journal.setName(command.vaultName);
      this.journal.setDate(new Date());
      this.journal.setFile(command.journal);
    }

    command.partSize = command.partSize * (int) AmazonGlacierBaseUtil.MINIMUM_PART_SIZE;

  }

  @Override
  public void run() {

    System.out.println("START: upload-archive\n");

    if (command.files.isEmpty()) {
      System.out.println("ERROR: No files specified");
    } else {

      AmazonGlacierUploadUtil upload = new AmazonGlacierUploadUtil(credentials, client, region);

      for (String fileName : command.files) {
        upload.uploadArchive(journal, command.vaultName, fileName, command.forceUpload,
            command.concurrent, command.retryFailedUpload, command.partSize,
            command.uploadReplaceModified);
      }

    }

    System.out.println("\nEND: upload-archive");
  }
}

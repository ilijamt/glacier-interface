package com.matoski.glacier.commands;

import java.io.IOException;

import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandDeleteArchive;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.pojo.archive.Archive;
import com.matoski.glacier.pojo.journal.State;
import com.matoski.glacier.util.upload.AmazonGlacierUploadUtil;

/**
 * Delete an archive
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class DeleteArchiveCommand extends AbstractCommand<CommandDeleteArchive> {

  /**
   * The journal, we use this for storing the data
   */
  protected State journal;

  /**
   * The archive ID we need to delete
   */
  protected String archiveId;

  /**
   * Constructor
   * 
   * @param config
   * @param command
   * 
   * @throws VaultNameNotPresentException
   * @throws RegionNotSupportedException
   * @throws IllegalArgumentException
   */
  public DeleteArchiveCommand(Config config, CommandDeleteArchive command)
      throws VaultNameNotPresentException, RegionNotSupportedException, IllegalArgumentException {
    super(config, command);

    Boolean validVaultName = null != command.vaultName;
    Boolean validVaultNameConfig = null != config.getVault();
    Boolean validId = (null != command.id);
    Boolean validName = (null != command.name);

    if (!command.ignoreJournal) {
      try {
        this.journal = State.load(command.journal);
      } catch (IOException e) {
        throw new RuntimeException("Journal doesn't exist");
      }
    }

    if (!validVaultName && !validVaultNameConfig) {
      throw new VaultNameNotPresentException();
    }

    if (validVaultNameConfig) {
      command.vaultName = config.getVault();
    }

    if (command.ignoreJournal && !validId) {
      throw new IllegalArgumentException("ID is required, when --ignore-journal is supplied");
    }

    if (!validId && !validName) {
      throw new IllegalArgumentException("ID or NAME are required");
    }

    if (command.ignoreJournal && !validId) {
      throw new IllegalArgumentException("ID is required");
    }

    if (validId && !validName) {
      archiveId = command.id;
    } else if (!command.ignoreJournal) {
      Archive archive = journal.getByName(command.name);
      if (null == archive) {
        throw new IllegalArgumentException("The archive is not present in the journal");
      }
      archiveId = archive.getId();
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {

    System.out.println("START: delete-archive\n");

    AmazonGlacierUploadUtil upload = new AmazonGlacierUploadUtil(credentials, client, region);

    try {

      upload.DeleteArchive(command.vaultName, archiveId);

      System.out.println("Archive deleted.\n");

      this.journal.deleteArchive(archiveId);

      System.out.println(String.format("%1$10s: %2$s", "Vault", command.vaultName));
      System.out.println(String.format("%1$10s: %2$s", "Archive ID", command.id));

      this.journal.save();

    } catch (Exception e) {
      System.err.println("Failed to delete the archive");
    }

    System.out.println("\nEND: delete-archive");
  }
}

package com.matoski.glacier.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandPurgeVault;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.pojo.archive.Archive;
import com.matoski.glacier.pojo.journal.State;
import com.matoski.glacier.util.upload.AmazonGlacierUploadUtil;

/**
 * Purges a vault from all files not present into the journal
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 *
 */
public class PurgeVaultCommand extends AbstractCommand<CommandPurgeVault> {

  /**
   * The journal, we use this for storing the data
   */
  protected State journal;

  /**
   * Constructor
   * 
   * @param config
   * @param command
   * @throws VaultNameNotPresentException
   * @throws RegionNotSupportedException
   */
  public PurgeVaultCommand(Config config, CommandPurgeVault command)
      throws VaultNameNotPresentException, RegionNotSupportedException {
    super(config, command);

    Boolean validVaultName = null != command.vaultName;
    Boolean validVaultNameConfig = null != config.getVault();

    try {
      this.journal = State.load(command.journal);
    } catch (IOException e) {
      throw new RuntimeException("Journal doesn't exist");
    }

    if (!validVaultName && !validVaultNameConfig) {
      throw new VaultNameNotPresentException();
    }

    if (validVaultNameConfig) {
      command.vaultName = config.getVault();
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {

    System.out.println("START: purge-vault\n");

    if (journal.size() > 0) {
      Archive archive = null;
      AmazonGlacierUploadUtil upload = new AmazonGlacierUploadUtil(credentials, client, region);

      Set<Entry<String, Archive>> archives = journal.getArchives().entrySet();
      List<String> ids = new ArrayList<String>();

      for (Entry<String, Archive> entry : archives) {

        try {
          archive = entry.getValue();
          ids.add(archive.getId());

          upload.DeleteArchive(command.vaultName, archive.getId());
          System.out.println(String.format("DELETED [%s] %s", archive.getId(), archive.getName()));
        } catch (Exception e) {
          System.err.println("Failed to delete the archive");
        }

      }

      for (String id : ids) {
        try {
          this.journal.deleteArchive(id);
          this.journal.save();
        } catch (Exception e) {
          System.err.println("Failed to clean journal");
        }

      }

    } else {
      System.out.println("No items available in the journal");
    }

    System.out.println("\nEND: purge-vault");
  }
}

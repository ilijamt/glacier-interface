package com.matoski.glacier.commands;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.amazonaws.services.glacier.model.GetJobOutputResult;
import com.amazonaws.services.glacier.model.GlacierJobDescription;
import com.google.gson.Gson;
import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandInventoryDownload;
import com.matoski.glacier.enums.Metadata;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.pojo.archive.GlacierInventory;
import com.matoski.glacier.pojo.journal.State;
import com.matoski.glacier.util.FileWriteUtils;
import com.matoski.glacier.util.upload.AmazonGlacierUploadUtil;

/**
 * Download inventory
 * 
 * @author ilijamt
 *
 */
public class InventoryDownloadCommand extends AbstractCommand<CommandInventoryDownload> {

  /**
   * Metadata used
   */
  protected Metadata metadata;

  /**
   * Constructor
   * 
   * @param config
   * @param command
   * @throws VaultNameNotPresentException
   * @throws RegionNotSupportedException
   */
  public InventoryDownloadCommand(Config config, CommandInventoryDownload command)
      throws VaultNameNotPresentException, RegionNotSupportedException {
    super(config, command);

    if ((null == command.vaultName || command.vaultName.isEmpty())
        && (null == config.getVault() || config.getVault().isEmpty())) {
      throw new VaultNameNotPresentException();
    }

    if ((null == command.vaultName) || command.vaultName.isEmpty()) {
      command.vaultName = config.getVault();
    }

    this.metadata = Metadata.from(command.metadata);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {

    System.out.println("START: inventory-download\n");

    AmazonGlacierUploadUtil upload = new AmazonGlacierUploadUtil(credentials, client, region);

    String jobId = null;

    if (null == command.id || command.id.isEmpty()) {

      GlacierJobDescription job = null;

      for (GlacierJobDescription j : upload.ListVaultJobs(command.vaultName)) {

        if (j.isCompleted() && j.getStatusCode().equalsIgnoreCase("Succeeded")) {
          job = j;
          jobId = job.getJobId();
        }

      }

    } else {

      jobId = command.id;

    }

    if (null == jobId) {

      System.out.println("ERROR: No completed InventoryJobs available");

    } else {

      GetJobOutputResult result = upload.InventoryDownload(command.vaultName, jobId);

      System.out.println("Inventory downloaded.\n");

      System.out.println(String.format("%1$10s: %2$s", "Job ID", jobId));
      System.out.println(String.format("%1$10s: %2$s", "Vault", command.vaultName));

      System.out.println();
      GlacierInventory inventory = null;

      try {
        String json = IOUtils.toString(result.getBody());
        inventory = new Gson().fromJson(json, GlacierInventory.class);

        if (command.raw) {
          FileWriteUtils.toJson(command.journal, inventory);
        } else {
          State journal = State.parse(inventory, command.vaultName, metadata);
          journal.setFile(command.journal);
          journal.save();
        }

      } catch (IOException e) {
        System.err.println("ERROR: " + e.getMessage());
      }

    }

    System.out.println("\nEND: inventory-download");
  }
}

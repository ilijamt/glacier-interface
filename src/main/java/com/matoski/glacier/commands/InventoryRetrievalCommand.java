package com.matoski.glacier.commands;

import com.amazonaws.services.glacier.model.InitiateJobResult;
import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandInventoryRetrieval;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.util.upload.AmazonGlacierUploadUtil;

/**
 * Initiate an inventory retrieval
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 *
 */
public class InventoryRetrievalCommand extends AbstractCommand<CommandInventoryRetrieval> {

  /**
   * Constructor
   * 
   * @param config
   * @param command
   * @throws VaultNameNotPresentException
   * @throws RegionNotSupportedException
   */
  public InventoryRetrievalCommand(Config config, CommandInventoryRetrieval command)
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {

    System.out.println("START: inventory-retrieve\n");

    AmazonGlacierUploadUtil upload = new AmazonGlacierUploadUtil(credentials, client, region);

    InitiateJobResult job = upload.inventoryRetrieval(command.vaultName);

    System.out.println("Inventory retrieved.\n");

    System.out.println(String.format("%1$10s: %2$s", "Job ID", job.getJobId()));
    System.out.println(String.format("%1$10s: %2$s", "Vault", command.vaultName));

    System.out.println("\nEND: inventory-retrieve");
  }
}

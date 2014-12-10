package com.matoski.glacier.commands;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandDeleteVault;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.util.upload.AmazonGlacierUploadUtil;

/**
 * A command used to delete a vault.
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class DeleteVaultCommand extends AbstractCommand<CommandDeleteVault> {

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
  public DeleteVaultCommand(Config config, CommandDeleteVault command)
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

    System.out.println("START: delete-vault\n");

    try {

      AmazonGlacierUploadUtil upload = new AmazonGlacierUploadUtil(credentials, client, region);

      upload.deleteVault(command.vaultName);
      System.out.println(String.format(
          "%s deleted. (Currently Amazon Glacier does not return error if vault does not exists)",
          command.vaultName));

    } catch (AmazonServiceException e) {
      switch (e.getErrorCode()) {
        case "InvalidSignatureException":
          System.err.println(String
              .format("ERROR: Invalid credentials, check you key and secret key."));
          break;
        default:
          System.err.println(String.format("ERROR: Failed to delete a vault: %s%n\t%s",
              command.vaultName, e.getMessage()));
          break;
      }
    } catch (AmazonClientException e) {
      System.err.println(String.format("ERROR: Cannot connect to the amazon web services.%n\t%s",
          e.getMessage()));
    }

    System.out.println("\nEND: delete-vault");

  }

}

package com.matoski.glacier.commands;

import java.util.List;

import com.amazonaws.services.glacier.model.DescribeVaultOutput;
import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandListVaults;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.util.FileUtils;
import com.matoski.glacier.util.upload.AmazonGlacierUploadUtil;

/**
 * List vaults
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 *
 */
public class ListVaultsCommand extends AbstractCommand<CommandListVaults> {

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
  public ListVaultsCommand(Config config, CommandListVaults command)
      throws VaultNameNotPresentException, RegionNotSupportedException {
    super(config, command);
  }

  @Override
  public void run() {

    System.out.println("START: list-vaults\n");

    AmazonGlacierUploadUtil upload = new AmazonGlacierUploadUtil(credentials, client, region);

    List<DescribeVaultOutput> result = upload.listVaults();

    System.out.println(String.format("Total available vaults: %s%n", result.size()));

    for (DescribeVaultOutput vault : result) {

      System.out.println(String.format("%1$20s: %2$s", "ARN", vault.getVaultARN()));
      System.out.println(String.format("%1$20s: %2$s", "Vault Name", vault.getVaultName()));
      System.out.println(String.format("%1$20s: %2$s", "Created", vault.getCreationDate()));
      System.out.println(String.format("%1$20s: %2$s (%3$s bytes)", "Inventory Size",
          FileUtils.humanReadableByteCount(vault.getSizeInBytes()), vault.getSizeInBytes()));
      System.out.println(String.format("%1$20s: %2$s", "Last Inventory Date",
          vault.getLastInventoryDate()));
      System.out.println();
    }

    System.out.println("END: list-vaults");
  }
}

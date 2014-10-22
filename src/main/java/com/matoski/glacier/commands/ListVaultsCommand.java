package com.matoski.glacier.commands;

import java.util.List;

import org.apache.commons.io.FileUtils;

import com.amazonaws.services.glacier.model.DescribeVaultOutput;
import com.amazonaws.services.glacier.model.ListVaultsRequest;
import com.amazonaws.services.glacier.model.ListVaultsResult;
import com.matoski.glacier.cli.CommandListVaults;
import com.matoski.glacier.pojo.Config;

public class ListVaultsCommand extends AbstractCommand {

    protected CommandListVaults command;

    public ListVaultsCommand(Config config, CommandListVaults command) {
	super(config);
	this.command = command;
    }

    public void run() {

	System.out.println("START: list-vaults\n");

	String marker = null;

	do {

	    ListVaultsRequest request = new ListVaultsRequest()
		    .withMarker(marker);

	    ListVaultsResult listVaultsResult = client.listVaults(request);

	    List<DescribeVaultOutput> vaultList = listVaultsResult
		    .getVaultList();
	    marker = listVaultsResult.getMarker();

	    System.out.println(String.format("Total available vaults: %s\n",
		    vaultList.size()));

	    for (DescribeVaultOutput vault : vaultList) {

		System.out
			.println(String
				.format("ARN: %s\nName: %s\nCreated: %s\nInventory Size: %s (%s bytes)\nLast Inventory Date: %s\n",
					vault.getVaultARN(), vault
						.getVaultName(), vault
						.getCreationDate(), FileUtils
						.byteCountToDisplaySize(vault
							.getSizeInBytes()),
					vault.getSizeInBytes(), vault
						.getLastInventoryDate()));

	    }

	} while (marker != null);

	System.out.println("END: list-vaults");
    }
}

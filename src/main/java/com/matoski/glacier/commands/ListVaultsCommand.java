package com.matoski.glacier.commands;

import java.util.List;

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
		String[] parts = null;

		do {

			ListVaultsRequest request = new ListVaultsRequest()
					.withMarker(marker);

			ListVaultsResult listVaultsResult = client.listVaults(request);

			List<DescribeVaultOutput> vaultList = listVaultsResult
					.getVaultList();
			marker = listVaultsResult.getMarker();

			System.out.println(String.format("Total vaults: %s\n",
					vaultList.size()));

			for (DescribeVaultOutput vault : vaultList) {

				parts = vault.getVaultARN().split(":");

				System.out
						.println(String
								.format("Location: %s/%s/%s\nARN: %s\nName: %s\nCreated: %s\nInventory Size: %s\nLast Inventory Date: %s\n",
										this.region
												.getServiceEndpoint("glacier"),
										parts[parts.length - 2],
										parts[parts.length - 1], vault
												.getVaultARN(), vault
												.getVaultName(), vault
												.getCreationDate(), vault
												.getSizeInBytes(), vault
												.getLastInventoryDate()));

			}

		} while (marker != null);

		System.out.println("END: list-vaults");
	}

	public boolean valid() {
		return false;
	}

}

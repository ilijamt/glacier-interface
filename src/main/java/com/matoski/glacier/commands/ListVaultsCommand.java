package com.matoski.glacier.commands;

import java.util.List;

import com.amazonaws.services.glacier.model.DescribeVaultOutput;
import com.amazonaws.services.glacier.model.ListVaultsRequest;
import com.amazonaws.services.glacier.model.ListVaultsResult;
import com.matoski.glacier.Output;
import com.matoski.glacier.cli.CommandListVaults;
import com.matoski.glacier.pojo.Config;

public class ListVaultsCommand extends AbstractCommand {

	protected CommandListVaults command;

	public ListVaultsCommand(Config config, CommandListVaults command) {
		super(config);
		this.command = command;
	}

	public void run() {

		String marker = null;

		do {

			ListVaultsRequest request = new ListVaultsRequest()
					.withMarker(marker);

			ListVaultsResult listVaultsResult = client.listVaults(request);

			List<DescribeVaultOutput> vaultList = listVaultsResult
					.getVaultList();
			marker = listVaultsResult.getMarker();

			Output.process(vaultList);

		} while (marker != null);

	}

	public boolean valid() {
		return false;
	}

}

package com.matoski.glacier.commands;

import java.util.List;

import com.amazonaws.services.glacier.model.DescribeVaultOutput;
import com.amazonaws.services.glacier.model.ListVaultsRequest;
import com.amazonaws.services.glacier.model.ListVaultsResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matoski.glacier.cli.CommandListVaults;
import com.matoski.glacier.pojo.Config;

public class ListVaultsCommand extends AbstractCommand {

	protected CommandListVaults command;

	public ListVaultsCommand(Config config,
			CommandListVaults command) {
		super(config);
		this.command = command;
	}

	public void run() {

		String marker = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		do {
			
			ListVaultsRequest request = new ListVaultsRequest()
					.withMarker(marker);

			ListVaultsResult listVaultsResult = client.listVaults(request);

			List<DescribeVaultOutput> vaultList = listVaultsResult
					.getVaultList();
			marker = listVaultsResult.getMarker();
			
			System.out.println(gson.toJson(vaultList));
			
		} while (marker != null);

	}

	public boolean valid() {
		return false;
	}

}

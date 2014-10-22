package com.matoski.glacier;

import java.util.List;

import com.amazonaws.services.glacier.model.CreateVaultResult;
import com.amazonaws.services.glacier.model.DescribeVaultOutput;
import com.amazonaws.services.glacier.model.DescribeVaultResult;
import com.google.gson.GsonBuilder;

public class Output {

	public static void process(String message) {
		System.out.println(message);
	}

	public static void process(List<DescribeVaultOutput> result) {
		generic(result);
	}

	public static void process(CreateVaultResult result, String endpoint) {
		System.out.println(String.format(
				"Vault created succesfully location: %s%s", endpoint,
				result.getLocation()));
	}

	public static void process(DescribeVaultResult result) {
		System.out.println(String.format("ARN: %s\nName: %s\nCreated: %s",
				result.getVaultARN(), result.getVaultName(),
				result.getCreationDate()));
	};

	private static void generic(Object obj) {
		System.out.println(new GsonBuilder().setPrettyPrinting().create()
				.toJson(obj));

	}

}

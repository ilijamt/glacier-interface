package com.matoski.glacier.pojo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A POJO class used to store generic information about how to connect to the system
 * 
 * @author ilijamt
 */
public class Config {

	/**
	 * Create the config from the JSON data 
	 * 
	 * @param json
	 * 
	 * @return
	 */
	public static Config fromJSON(String json) {
		return new Gson().fromJson(json, Config.class);
	}


	/**
	 * The name of the configuration file
	 */
	private String name;


	/**
	 * The location of the journal to be used in the 
	 */
	private String journal;
	
	/**
	 * Amazon region to use
	 */
	private String region;


	/**
	 * Amazon access secret key
	 */
	private String secretKey;


	/**
	 * Amazon access key
	 */
	private String key;


	/**
	 * Amazon vault
	 */
	private String vault;


	/**
	 * Directory used 
	 */
	private String directory;


	/**
	 * @return the directory
	 */
	public String getDirectory() {
		return directory;
	}


	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}


	/**
	 * @return the secretKey
	 */
	public String getSecretKey() {
		return secretKey;
	}


	/**
	 * @return the vault
	 */
	public String getVault() {
		return vault;
	}
	
	
	/**
	 * @param directory the directory to set
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}
		
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	
	/**
	 * @param secretKey the secretKey to set
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	/**
	 * @param vault the vault to set
	 */
	public void setVault(String vault) {
		this.vault = vault;
	}

	
	/**
	 * Convert the object to JSON, so we can write the config on the file system
	 * 
	 * @return The generated JSON string
	 */
	public String toJSON() {
		return new GsonBuilder().setPrettyPrinting().create().toJson(this);
	}

}

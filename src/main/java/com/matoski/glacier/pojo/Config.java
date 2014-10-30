package com.matoski.glacier.pojo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.matoski.glacier.cli.Arguments;

/**
 * A POJO class used to store generic information about how to connect to the
 * system
 * 
 * @author ilijamt
 */
public class Config {

    /**
     * Static instance
     */
    private static Config instance;

    /**
     * Constructor
     */
    private Config() {

    }

    /**
     * Get the current instance
     * 
     * @return
     */
    public static Config getInstance() {

	if (null != instance) {
	    return instance;
	}

	return null;

    }

    /**
     * Create the config from the JSON data
     * 
     * @param json
     * 
     * @return
     * 
     * @throws JsonSyntaxException
     */
    public static Config fromJSON(String json) throws JsonSyntaxException {
	instance = new Gson().fromJson(json, Config.class);
	return instance;
    }

    /**
     * Create the config file from the arguments
     * 
     * @param arguments
     * @return
     */
    public static Config fromArguments(Arguments arguments) {

	instance = new Config();

	if (null != arguments.amazonKey) {
	    instance.setKey(arguments.amazonKey);
	}

	if (null != arguments.amazonSecretKey) {
	    instance.setSecretKey(arguments.amazonSecretKey);
	}

	if (null != arguments.amazonRegion) {
	    instance.setRegion(arguments.amazonRegion);
	}

	if (null != arguments.amazonVault) {
	    instance.setVault(arguments.amazonVault);
	}

	if (null != arguments.directory) {
	    instance.setDirectory(arguments.directory);
	}

	return instance;
    }

    /**
     * Create the config object from the file
     * 
     * @param filename
     * 
     * @return
     * 
     * @throws JsonSyntaxException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static Config fromFile(String filename) throws JsonSyntaxException,
	    FileNotFoundException, IOException {

	File f = new File(filename);

	if (f.exists() && !f.isFile()) {
	    throw new FileNotFoundException(filename);
	}

	instance = fromJSON(new String(Files.readAllBytes(Paths.get(filename))));
	return instance;

    }

    /**
     * Merge all the arguments into the configuration file, we use this so we
     * can actually override from the command line.
     * 
     * @param arguments
     */
    public void merge(Arguments arguments) {

	if (null != arguments.amazonKey) {
	    this.setKey(arguments.amazonKey);
	}

	if (null != arguments.amazonSecretKey) {
	    this.setSecretKey(arguments.amazonSecretKey);
	}

	if (null != arguments.amazonRegion) {
	    this.setRegion(arguments.amazonRegion);
	}

	if (null != arguments.amazonVault) {
	    this.setVault(arguments.amazonVault);
	}

    }

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
	if (null == directory) {
	    directory = System.getProperty("user.dir");
	}
	return directory;
    }

    /**
     * @return the key
     */
    public String getKey() {
	return key;
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
     * @param directory
     *            the directory to set
     */
    public void setDirectory(String directory) {
	this.directory = directory;
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(String key) {
	this.key = key;
    }

    /**
     * @param region
     *            the region to set
     */
    public void setRegion(String region) {
	this.region = region;
    }

    /**
     * @param secretKey
     *            the secretKey to set
     */
    public void setSecretKey(String secretKey) {
	this.secretKey = secretKey;
    }

    /**
     * @param vault
     *            the vault to set
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

    /**
     * Is the config valid, has minimum required parameters
     * 
     * Amazon Secret Key Amazon Key Amazon Vault Amazon Region
     * 
     * @return
     */
    public boolean valid() {
	return valid(true);
    }

    /**
     * 
     * Is the config valid, has minimum required parameters
     * 
     * Amazon Secret Key Amazon Key Amazon Vault Amazon Region
     *
     * @param vault
     *            Is the vault required
     * 
     * @return
     */
    public boolean valid(boolean vault) {
	return (null != this.getKey()) && (null != this.getSecretKey())
		&& (vault ? (null != this.getVault()) : true)
		&& (null != this.getRegion());

    }

    /**
     * Creates a configuration file based on the data in the configuration
     * 
     * @param filename
     * @throws IOException
     */
    public void createConfigurationFile(String filename) throws IOException {
	File file = new File(filename);
	FileWriter fileWriter = null;
	BufferedWriter bufferedWriter = null;

	if (!file.exists()) {
	    file.createNewFile();
	}

	fileWriter = new FileWriter(file.getAbsoluteFile());
	bufferedWriter = new BufferedWriter(fileWriter);
	bufferedWriter.write(new GsonBuilder().setPrettyPrinting().create()
		.toJson(this));

	bufferedWriter.close();
	fileWriter.close();

    }

}
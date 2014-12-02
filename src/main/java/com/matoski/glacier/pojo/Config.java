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
 * A POJO class used to store generic information about how to connect to the system
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class Config {

  /**
   * Create the config file from the arguments.
   * 
   * @param arguments
   *          The arguments from which to generate {@link Config}
   * 
   * @return A configuration class generated from arguments
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
   * Create the config object from the file.
   * 
   * @param filename
   *          The file name from where to read the {@link Config} object
   * 
   * @return The configuration object
   * 
   * @throws JsonSyntaxException
   *           Invalid JSON format
   * @throws FileNotFoundException
   *           If the file cannot be found
   * @throws IOException
   *           If the file cannot be read, or an error occurs during reading
   */
  public static Config fromFile(String filename) throws JsonSyntaxException, FileNotFoundException,
      IOException {

    File file = new File(filename);

    if (file.exists() && !file.isFile()) {
      throw new FileNotFoundException(filename);
    }

    instance = fromJson(new String(Files.readAllBytes(Paths.get(filename))));
    return instance;

  }

  /**
   * Create the config from the JSON data.
   * 
   * @param json
   *          The json string from which we create {@link Config} object
   * 
   * @return The created configuration object
   * 
   * @throws JsonSyntaxException
   *           Invalid JSON format
   */
  public static Config fromJson(String json) throws JsonSyntaxException {
    instance = new Gson().fromJson(json, Config.class);
    return instance;
  }

  /**
   * Get the current instance.
   * 
   * @return The {@link Config} instance
   */
  public static Config getInstance() {

    if (null != instance) {
      return instance;
    }

    return null;

  }

  /**
   * Static instance for the application configuration.
   */
  private static Config instance;

  /**
   * Amazon region to use.
   */
  private String region;

  /**
   * Amazon access secret key.
   */
  private String secretKey;

  /**
   * Amazon access key.
   */
  private String key;

  /**
   * Amazon vault.
   */
  private String vault;

  /**
   * Directory used.
   */
  private String directory;

  /**
   * Constructor.
   */
  private Config() {

  }

  /**
   * Creates a configuration file based on the data in the configuration.
   * 
   * @param filename
   *          Which filename to save the configuration to
   * 
   * @throws IOException
   *           Cannot write to file
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
    bufferedWriter.write(new GsonBuilder().setPrettyPrinting().create().toJson(this));

    bufferedWriter.close();
    fileWriter.close();

  }

  /**
   * Gets the directory, this is used to supply a directory which will be used to create all the
   * pats for a directory.
   * 
   * @return The working directory
   */
  public String getDirectory() {
    if (null == directory) {
      directory = System.getProperty("user.dir");
    }
    return directory;
  }

  /**
   * Gets the amazon key from {@link #key}.
   * 
   * @return the amazon key
   */
  public String getKey() {
    return key;
  }

  /**
   * Gets the amazon region from {@link #region}.
   * 
   * @return the region
   */
  public String getRegion() {
    return region;
  }

  /**
   * Gets the amazon secret key from {@link #secretKey}.
   * 
   * @return the secretKey
   */
  public String getSecretKey() {
    return secretKey;
  }

  /**
   * Gets the amazon glacier vault name from {@link #vault}.
   * 
   * @return the vault
   */
  public String getVault() {
    return vault;
  }

  /**
   * Merge all the arguments into the configuration file, we use this so we can actually override
   * from the command line.
   * 
   * @param arguments
   *          The arguments supplied by the command line, this is used to merge the arguments from a
   *          configuration file with the arguments supplied in the command line
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

    if (null != arguments.directory) {
      this.setDirectory(arguments.directory);
    }

  }

  /**
   * Sets the working directory.
   * 
   * @param directory
   *          the directory to set
   */
  public void setDirectory(String directory) {
    this.directory = directory;
  }

  /**
   * Sets the amazon key.
   * 
   * @param key
   *          the key to set
   */
  public void setKey(String key) {
    this.key = key;
  }

  /**
   * Sets the amazon region.
   * 
   * @param region
   *          the region to set
   */
  public void setRegion(String region) {
    this.region = region;
  }

  /**
   * Sets the secret key.
   * 
   * @param secretKey
   *          the secretKey to set
   */
  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  /**
   * Sets the vault name.
   * 
   * @param vault
   *          the vault to set
   */
  public void setVault(String vault) {
    this.vault = vault;
  }

  /**
   * Convert the object to JSON, so we can write the config on the file system.
   * 
   * @return The generated JSON string
   */
  public String toJson() {
    return new GsonBuilder().setPrettyPrinting().create().toJson(this);
  }

  /**
   * Is the config valid, and has minimum required parameters.
   * 
   * @return true if the configuration is valid and has all required parameters to run
   */
  public boolean valid() {
    return valid(true);
  }

  /**
   * Is the config valid, has minimum required parameters, this also checks if vault is present too.
   * 
   * @param vault
   *          Is the vault required
   * 
   * @return true if the configuration is valid and has all required parameters to run
   */
  public boolean valid(boolean vault) {
    return (null != this.getKey()) && (null != this.getSecretKey())
        && (vault ? (null != this.getVault()) : true) && (null != this.getRegion());

  }

}
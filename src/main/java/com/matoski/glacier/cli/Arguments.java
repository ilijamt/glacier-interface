package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;

public class Arguments {

  @Parameter(names = "--aws-region", description = "Sets the amazon region, if specified it will override the data loaded from the configuration file")
  public String amazonRegion = "eu-west-1";

  @Parameter(names = "--aws-key", description = "Sets the amazon key, if specified it will override the data loaded from the configuration file")
  public String amazonKey;

  @Parameter(names = "--aws-secret-key", description = "Sets the amazon secret key, if specified it will override the data loaded from the configuration file")
  public String amazonSecretKey;

  @Parameter(names = "--aws-vault", description = "Sets the amazon vault, if specified it will override the data loaded from the configuration file")
  public String amazonVault;

  @Parameter(names = "--config", description = "Location to the configuration file to load")
  public String config;

  @Parameter(names = "--create-config", description = "Create a config file based on the parameters you have supplied into the application")
  public String createConfig;

  @Parameter(names = "--directory", description = "The base directory from which we start, if not specified then the directory is set to the current working directory")
  public String directory;

}

package com.matoski.glacier.base;

import com.matoski.glacier.enums.CliCommands;

/**
 * A generic command, all the cli commands need to extend from this, it can be used to autoload all
 * of them
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public abstract class GenericCommand {

  /**
   * The command name
   */
  final private String __name;

  /**
   * Constructor
   * 
   * @param command
   */
  public GenericCommand(CliCommands command) {
    this.__name = command.getPropertyName();
  }

  /**
   * Constructor
   * 
   * @param name
   */
  public GenericCommand(String name) {
    this.__name = name;
  }

  /**
   * Get the command name
   * 
   * @return
   */
  public String __getCommandName() {
    return __name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int hashCode = 1;

    hashCode = prime * hashCode + __name.hashCode();
    return hashCode;
  }
}

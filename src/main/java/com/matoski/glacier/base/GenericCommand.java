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
   * The command name.
   */
  private final transient String genericCommandName;

  /**
   * Constructor.
   * 
   * @param command
   *          The command
   */
  public GenericCommand(CliCommands command) {
    this.genericCommandName = command.getPropertyName();
  }

  /**
   * Constructor.
   * 
   * @param name
   *          The command
   */
  public GenericCommand(String name) {
    this.genericCommandName = name;
  }

  /**
   * Get the command name.
   * 
   * @return {@link #genericCommandName}
   */
  public String getGenericCommandName() {
    return genericCommandName;
  }

  @Override
  public boolean equals(Object obj) {

    if (null == obj) {
      return false;
    }

    if (this.getClass() != obj.getClass()) {
      return false;
    }

    return this.genericCommandName.equals(((GenericCommand) obj).getGenericCommandName());

  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hashCode = 1;

    hashCode = prime * hashCode + genericCommandName.hashCode();
    return hashCode;
  }
}

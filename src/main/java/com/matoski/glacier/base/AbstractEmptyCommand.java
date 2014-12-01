package com.matoski.glacier.base;

import com.matoski.glacier.interfaces.ICommand;
import com.matoski.glacier.pojo.Config;

public abstract class AbstractEmptyCommand<T> implements ICommand, Runnable {

  /**
   * The configuration
   */
  protected Config config = null;

  /**
   * The specific command for the command in question
   */
  protected T command;

  /**
   * Constructor
   * 
   * @param config
   * @param command
   */
  public AbstractEmptyCommand(Config config, T command) {
    this.command = command;
    this.config = config;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean valid() {
    return true;
  }

}
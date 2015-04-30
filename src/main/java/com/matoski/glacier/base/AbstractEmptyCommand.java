package com.matoski.glacier.base;

import com.matoski.glacier.interfaces.ICommand;
import com.matoski.glacier.pojo.Config;

/**
 * Abstract empty command, this doesn't have any parameters included in the command.
 *
 * @param <T> usually a class that extends from {@link GenericCommand}
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public abstract class AbstractEmptyCommand<T> implements ICommand, Runnable {

    /**
     * Application configuration.
     */
    protected Config config = null;

    /**
     * The specific command for the command in question.
     */
    protected T command;

    /**
     * Constructor.
     *
     * @param config  Application configuration
     * @param command The command to process
     */
    public AbstractEmptyCommand(Config config, T command) {
        this.command = command;
        this.config = config;
    }

    @Override
    public boolean valid() {
        return true;
    }

}
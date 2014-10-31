package com.matoski.glacier.cli;

import com.matoski.glacier.enums.CliCommands;

/**
 * A generic command, all the cli commands need to extend from this, it can be
 * used to autoload all of them
 * 
 * @author ilijamt
 */
public abstract class GenericCommand {

    /**
     * The command name
     */
    final private String name;

    /**
     * Constructor
     * 
     * @param command
     */
    public GenericCommand(CliCommands command) {
	this.name = command.getPropertyName();
    }

    /**
     * Constructor
     * 
     * @param name
     */
    public GenericCommand(String name) {
	this.name = name;
    }

    /**
     * Get the command name
     * 
     * @return
     */
    public String getName() {
	return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int hashCode = 1;

	hashCode = prime * hashCode + name.hashCode();
	return hashCode;
    }
}

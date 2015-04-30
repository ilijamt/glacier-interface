package com.matoski.glacier.interfaces;

/**
 * Generic command that is used for all commands in the application, this is just abstraction to
 * make the command interface easier, and simpler.
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public interface ICommand {

    /**
     * Runs the command.
     */
    void run();

    /**
     * Checks if the command is valid or not.
     *
     * @return true if the command is valid
     */
    boolean valid();
}

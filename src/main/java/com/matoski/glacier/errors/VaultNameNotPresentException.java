package com.matoski.glacier.errors;

import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.pojo.Config;

/**
 * Thrown when the vault name is not present in the configuration either in {@link Config} or in the
 * class that extends from {@link GenericCommand}
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class VaultNameNotPresentException extends Exception {

    /**
     * Serial version ID.
     */
    private static final long serialVersionUID = -4095924865953917183L;

}

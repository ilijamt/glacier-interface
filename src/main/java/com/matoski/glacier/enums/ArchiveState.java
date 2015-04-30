package com.matoski.glacier.enums;

/**
 * The state of the archive.
 * <p/>
 * <p>
 * The archives get a state that is used to define what has happened with them in the past. You can
 * create an archive, then download, then delete, all that data will be present in the journal log.
 * </p>
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public enum ArchiveState {

    /**
     * Archive has not been defined.
     */
    NOT_DEFINED,

    /**
     * Archive has been created.
     */
    CREATE,

    /**
     * Archive has been deleted.
     */
    DELETE,

    /**
     * Archive has been downloaded.
     */
    DOWNLOAD
}

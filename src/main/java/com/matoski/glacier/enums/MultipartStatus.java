package com.matoski.glacier.enums;

/**
 * Multipart status, contains the states as they go through while uploading or downloading.
 * <p/>
 * <p>
 * Used to track the upload/download process, as it goes through it.
 * </p>
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public enum MultipartStatus {

    /**
     * No operation.
     */
    NOP,

    /**
     * In progress.
     */
    START,

    /**
     * The process is in progress.
     */
    IN_PROGRESS,

    /**
     * The process is complete.
     */
    COMPLETE,

    /**
     * Some kind of error occured.
     */
    ERROR

}

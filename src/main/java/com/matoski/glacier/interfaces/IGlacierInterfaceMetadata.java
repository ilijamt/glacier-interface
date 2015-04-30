package com.matoski.glacier.interfaces;

import com.matoski.glacier.pojo.archive.Archive;

/**
 * Generic interface to the metadata, all the metadata definitions have to extend this, as this will
 * tell us how to define the required parameters by our definition
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public interface IGlacierInterfaceMetadata {

    /**
     * Get the modified date, this goes into {@link Archive#setModifiedDate(long)}.
     *
     * @return the last modified date from the metadata
     */
    public long giGetModifiedDate();

    /**
     * Get the name of the archive, this goes into {@link Archive#setName(String)}.
     *
     * @return the name of the archive from the metadata
     */
    public String giGetName();

}

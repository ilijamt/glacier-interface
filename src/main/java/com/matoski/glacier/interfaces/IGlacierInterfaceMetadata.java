package com.matoski.glacier.interfaces;

import java.util.Date;

import com.matoski.glacier.pojo.Archive;

/**
 * Generic interface to the metadata, all the metadata definitions have to
 * extend this, as this will tell us how to define the required parameters by
 * our definition
 * 
 * @author ilijamt
 *
 */
public interface IGlacierInterfaceMetadata {

    /**
     * Get the modified date, this goes into
     * {@link Archive#setModifiedDate(Date)}
     * 
     * @return
     */
    public long giGetModifiedDate();

    /**
     * Get the name of the archive, this goes into
     * {@link Archive#setName(String)}
     * 
     * @return
     */
    public String giGetName();

}

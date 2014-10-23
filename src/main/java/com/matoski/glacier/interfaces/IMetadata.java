package com.matoski.glacier.interfaces;

import com.matoski.glacier.errors.InvalidMetadataException;

/**
 * Metadata definition
 * 
 * @author ilijamt
 */
public interface IMetadata {

    /**
     * Verify if this is the correct metadata to use
     * 
     * @param data
     * 
     * @return
     */
    public boolean verify(String data);

    /**
     * Process the data
     * 
     * @param data
     * @return
     * 
     * @throws InvalidMetadataException
     */
    public IGlacierInterfaceMetadata process(String data)
	    throws InvalidMetadataException;

}

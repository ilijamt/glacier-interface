package com.matoski.glacier.interfaces;

import com.matoski.glacier.errors.InvalidMetadataException;
import com.matoski.glacier.pojo.Archive;

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
    public IGlacierInterfaceMetadata parse(String data)
	    throws InvalidMetadataException;

    /**
     * Encode the data into the correct format for the metadata in question
     * 
     * @param archive
     * 
     * @return
     */
    public String encode(Archive archive);

}

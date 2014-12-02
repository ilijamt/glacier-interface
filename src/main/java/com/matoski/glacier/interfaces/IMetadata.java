package com.matoski.glacier.interfaces;

import com.matoski.glacier.errors.InvalidMetadataException;
import com.matoski.glacier.pojo.archive.Archive;

/**
 * Metadata definition
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public interface IMetadata {

  /**
   * Encode the data into the correct format for the metadata in question
   * 
   * @param archive
   * 
   * @return
   */
  public String encode(Archive archive);

  /**
   * Process the data
   * 
   * @param data
   * @return
   * 
   * @throws InvalidMetadataException
   */
  public IGlacierInterfaceMetadata parse(String data) throws InvalidMetadataException;

  /**
   * Verify if this is the correct metadata to use
   * 
   * @param data
   * 
   * @return
   */
  public boolean verify(String data);

}

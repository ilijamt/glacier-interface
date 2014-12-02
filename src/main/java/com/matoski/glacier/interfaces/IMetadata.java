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
   * Encode the data into the correct format for the metadata in question.
   * 
   * @param archive
   *          The archive
   * 
   * @return The encoded data as string
   * 
   */
  public String encode(Archive archive);

  /**
   * Process the data.
   * 
   * @param data
   *          The data to process
   * 
   * @return Objects that extend from {@link IGlacierInterfaceMetadata}
   * 
   * @throws InvalidMetadataException
   *           If the metadata doesn't exist
   */
  public IGlacierInterfaceMetadata parse(String data) throws InvalidMetadataException;

  /**
   * Verify if this is the correct metadata to use.
   * 
   * @param data
   *          The data to verify
   * 
   * @return true if the data is valid
   * 
   */
  public boolean verify(String data);

}

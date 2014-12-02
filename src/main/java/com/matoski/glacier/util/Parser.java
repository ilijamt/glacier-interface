package com.matoski.glacier.util;

import com.matoski.glacier.enums.Metadata;
import com.matoski.glacier.errors.InvalidMetadataException;
import com.matoski.glacier.interfaces.IGlacierInterfaceMetadata;
import com.matoski.glacier.metadata.FastGlacierV2;
import com.matoski.glacier.metadata.GenericParser;
import com.matoski.glacier.metadata.MT_AWS_GLACIER_B;
import com.matoski.glacier.pojo.archive.Archive;

/**
 * Generic parser
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 *
 */
public class Parser {

  /**
   * Encode
   * 
   * @param metadata
   * @param archive
   * 
   * @return
   */
  public static String encode(Metadata metadata, Archive archive) {
    return getParser(metadata).encode(archive);
  }

  /**
   * Get the parser dependent on the metadata
   * 
   * @param metadata
   * 
   * @return
   */
  public static GenericParser getParser(Metadata metadata) {

    switch (metadata) {
      case MT_AWS_GLACIER_B:
        return (GenericParser) (new MT_AWS_GLACIER_B());
      case FAST_GLACIER_V2:
        return (GenericParser) (new FastGlacierV2());
    }

    return null;
  }

  /**
   * Parse
   * 
   * @param metadata
   * @param data
   * 
   * @return
   * 
   * @throws InvalidMetadataException
   * @throws NullPointerException
   */
  public static IGlacierInterfaceMetadata parse(Metadata metadata, String data)
      throws InvalidMetadataException, NullPointerException {
    return getParser(metadata).parse(data);
  }

}

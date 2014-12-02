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
   * Encode an archive.
   * 
   * @param metadata
   *          The metadata to which we encode
   * @param archive
   *          The archive we encode
   * 
   * @return Encoded archive
   */
  public static String encode(Metadata metadata, Archive archive) {
    return getParser(metadata).encode(archive);
  }

  /**
   * Get the parser dependent on the metadata.
   * 
   * @param metadata
   *          The metadata for the parser
   * 
   * @return The parser
   */
  public static GenericParser getParser(Metadata metadata) {

    switch (metadata) {
      case MT_AWS_GLACIER_B:
        return (GenericParser) (new MT_AWS_GLACIER_B());
      case FAST_GLACIER_V2:
        return (GenericParser) (new FastGlacierV2());
      default:
        break;
    }

    return null;
  }

  /**
   * Parse the data.
   * 
   * @param metadata
   *          What is the metadata for the data
   * @param data
   *          The data to parse
   * 
   * @return
   * 
   * @throws InvalidMetadataException
   *           If invalid metadata
   * @throws NullPointerException
   *           If the parser doesn't exist
   */
  public static IGlacierInterfaceMetadata parse(Metadata metadata, String data)
      throws InvalidMetadataException, NullPointerException {
    return getParser(metadata).parse(data);
  }

}

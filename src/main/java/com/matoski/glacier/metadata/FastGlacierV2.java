package com.matoski.glacier.metadata;

import com.matoski.glacier.enums.Metadata;
import com.matoski.glacier.errors.InvalidMetadataException;
import com.matoski.glacier.interfaces.IGlacierInterfaceMetadata;
import com.matoski.glacier.pojo.archive.Archive;

/**
 * mt-aws-glacier type B metadata
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class FastGlacierV2 extends GenericParser implements IGlacierInterfaceMetadata {

  /**
   * The version of the metadata.
   */
  private int v = 2;

  /**
   * The file name.
   */
  private String p;

  /**
   * The last modified date.
   */
  private String lm;

  /**
   * Constructor.
   */
  public FastGlacierV2() {
    super(Metadata.FAST_GLACIER_V2);
  }

  @Override
  public String encode(Archive archive) {
    return new String();
  }

  /**
   * Gets the last modified entry from the metadata.
   * 
   * @return the lm
   */
  public String getLm() {
    return lm;
  }

  /**
   * Gets the path from the metadata.
   * 
   * @return {@link #p}
   */
  public String getP() {
    return p;
  }

  /**
   * Gets the version of the metadata. 
   * 
   * @return the v
   */
  public int getV() {
    return v;
  }

  @Override
  public long giGetModifiedDate() {
    return 0;
  }

  @Override
  public String giGetName() {
    return this.p;
  }

  @Override
  public IGlacierInterfaceMetadata parse(String data) throws InvalidMetadataException {
    if (!verify(data)) {
      throw new InvalidMetadataException();
    }

    return null;
  }

  /**
   * @param lm
   *          the lm to set
   */
  public void setLm(String lm) {
    this.lm = lm;
  }

  /**
   * Sets the Path
   * 
   * @param p
   *          the p to set
   */
  public void setP(String p) {
    this.p = p;
  }

  /**
   * Sets the version
   * 
   * @param v
   *          the v to set
   */
  public void setV(int v) {
    this.v = v;
  }

}

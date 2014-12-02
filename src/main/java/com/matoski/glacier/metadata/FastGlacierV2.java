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
   * The version of the metadata
   */
  private int v = 2;

  /**
   * The file name
   */
  private String p;

  /**
   * The last modified date
   */
  private String lm;

  /**
   * Constructor
   */
  public FastGlacierV2() {
    super(Metadata.FAST_GLACIER_V2);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String encode(Archive archive) {
    return new String();
  }

  /**
   * @return the lm
   */
  public String getLm() {
    return lm;
  }

  /**
   * @return the p
   */
  public String getP() {
    return p;
  }

  /**
   * @return the v
   */
  public int getV() {
    return v;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long giGetModifiedDate() {
    return 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String giGetName() {
    return this.p;
  }

  /**
   * {@inheritDoc}
   */
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
   * @param p
   *          the p to set
   */
  public void setP(String p) {
    this.p = p;
  }

  /**
   * @param v
   *          the v to set
   */
  public void setV(int v) {
    this.v = v;
  }

}

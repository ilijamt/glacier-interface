package com.matoski.glacier.metadata;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.matoski.glacier.enums.Metadata;
import com.matoski.glacier.errors.InvalidMetadataException;
import com.matoski.glacier.interfaces.IGlacierInterfaceMetadata;
import com.matoski.glacier.pojo.archive.Archive;

/**
 * mt-aws-glacier type B metadata
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class MtAwsGlacierB extends GenericParser implements IGlacierInterfaceMetadata {

  /**
   * Identifier.
   */
  public static final String IDENTIFIER = "mt2 ";

  /**
   * The original filename.
   */
  private String filename;

  /**
   * The last modified time.
   */
  @SerializedName(value = "mtime")
  private String lastModifiedDate;

  /**
   * Constructor.
   */
  public MtAwsGlacierB() {
    super(Metadata.MT_AWS_GLACIER_B);
  }

  @Override
  public String encode(Archive archive) {
    this.lastModifiedDate = Long.toString(archive.getModifiedDate());
    this.filename = archive.getName();
    return IDENTIFIER + Base64.encodeBase64String(new Gson().toJson(this).getBytes());

  }

  /**
   * Gets the filename from metadata.
   * 
   * @return {@link #filename}
   */
  public String getFilename() {
    return filename;
  }

  /**
   * Gets the last modified time.
   * 
   * @return Last modified time as unix timestamp
   */
  public long getLastModifiedDate() {
    if (lastModifiedDate.contains("T") && lastModifiedDate.contains("Z")) {
      StringBuilder builder = new StringBuilder();
      builder.append(lastModifiedDate.substring(0, 4));
      builder.append("-");
      builder.append(lastModifiedDate.substring(4, 6));
      builder.append("-");
      builder.append(lastModifiedDate.substring(6, 11));
      builder.append(":");
      builder.append(lastModifiedDate.substring(11, 13));
      builder.append(":");
      builder.append(lastModifiedDate.substring(13));
      return ISO8601Utils.parse(builder.toString()).getTime();
    } else {
      return Long.valueOf(lastModifiedDate);
    }
  }

  @Override
  public long giGetModifiedDate() {
    return this.getLastModifiedDate();
  }

  @Override
  public String giGetName() {
    return this.getFilename();
  }

  @Override
  public IGlacierInterfaceMetadata parse(String data) throws InvalidMetadataException {
    if (!verify(data)) {
      throw new InvalidMetadataException();
    }

    String base64data = data.substring(IDENTIFIER.length(), data.length());

    String json = new String(Base64.decodeBase64(base64data)).trim();
    MtAwsGlacierB obj = new Gson().fromJson(json, MtAwsGlacierB.class);

    return (IGlacierInterfaceMetadata) obj;

  }

  /**
   * Sets the filename.
   * 
   * @param filename
   *          the filename to set
   */
  public void setFilename(String filename) {
    this.filename = filename;
  }

  /**
   * Sets the last modified time.
   * 
   * @param mtime
   *          the last modified time to set
   */
  public void setLastModifiedDate(String mtime) {
    this.lastModifiedDate = mtime;
  }

  @Override
  public boolean verify(String data) {
    return data.startsWith(IDENTIFIER);
  }

}

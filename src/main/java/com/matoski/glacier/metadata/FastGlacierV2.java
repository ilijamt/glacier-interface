package com.matoski.glacier.metadata;

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
public class FastGlacierV2 extends GenericParser implements IGlacierInterfaceMetadata {

    /**
     * The version of the metadata.
     */
    @SerializedName(value = "v")
    private int version = 2;

    /**
     * The file name.
     */
    @SerializedName(value = "p")
    private String filename;

    /**
     * The last modified date.
     */
    @SerializedName(value = "lm")
    private String lastModifiedDate;

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
     * @return {@link #lastModifiedDate}
     */
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Set the last modified date.
     *
     * @param lastModifiedDate the lm to set
     */
    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * Gets the filename from the metadata.
     *
     * @return {@link #filename}
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the filename.
     *
     * @param filename the p to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Gets the version of the metadata.
     *
     * @return the v
     */
    public int getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param ver the version to set
     */
    public void setVersion(int ver) {
        this.version = ver;
    }

    @Override
    public long giGetModifiedDate() {
        return 0;
    }

    @Override
    public String giGetName() {
        return this.filename;
    }

    @Override
    public IGlacierInterfaceMetadata parse(String data) throws InvalidMetadataException {
        if (!verify(data)) {
            throw new InvalidMetadataException();
        }

        return null;
    }

}

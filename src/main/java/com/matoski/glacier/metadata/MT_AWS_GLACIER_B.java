package com.matoski.glacier.metadata;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;
import com.matoski.glacier.enums.Metadata;
import com.matoski.glacier.errors.InvalidMetadataException;
import com.matoski.glacier.interfaces.IGlacierInterfaceMetadata;
import com.matoski.glacier.pojo.Archive;

/**
 * mt-aws-glacier type B metadata
 * 
 * @author ilijamt
 */
public class MT_AWS_GLACIER_B extends GenericParser implements
	IGlacierInterfaceMetadata {

    /**
     * Identifier
     */
    public static final String IDENTIFIER = "mt2 ";

    /**
     * The original filename
     */
    private String filename;

    /**
     * The last modified time
     */
    private String mtime;

    /**
     * Constructor
     */
    public MT_AWS_GLACIER_B() {
	super(Metadata.MT_AWS_GLACIER_B);
    }

    /**
     * @return the filename
     */
    public String getFilename() {
	return filename;
    }

    /**
     * @return the mtime
     */
    public long getMtime() {
	return Long.parseLong(this.mtime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long giGetModifiedDate() {
	return this.getMtime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String giGetName() {
	return this.getFilename();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IGlacierInterfaceMetadata parse(String data)
	    throws InvalidMetadataException {
	if (!verify(data)) {
	    throw new InvalidMetadataException();
	}

	String base64data = data.substring(IDENTIFIER.length(), data.length());

	String json = new String(Base64.decodeBase64(base64data)).trim();
	MT_AWS_GLACIER_B obj = new Gson()
		.fromJson(json, MT_AWS_GLACIER_B.class);

	return (IGlacierInterfaceMetadata) obj;

    }

    /**
     * @param filename
     *            the filename to set
     */
    public void setFilename(String filename) {
	this.filename = filename;
    }

    /**
     * @param mtime
     *            the mtime to set
     */
    public void setMtime(String mtime) {
	this.mtime = mtime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verify(String data) {
	return data.startsWith(IDENTIFIER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String encode(Archive archive) {
	this.mtime = Long.toString(archive.getModifiedDate());
	this.filename = archive.getName();
	return IDENTIFIER
		+ Base64.encodeBase64String(new Gson().toJson(this).getBytes());

    }

}

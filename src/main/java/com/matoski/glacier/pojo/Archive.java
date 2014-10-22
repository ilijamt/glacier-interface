package com.matoski.glacier.pojo;

import java.util.Date;

/**
 * An archive element, it's used to store all the relevant information about the
 * archive in one place
 * 
 * @author ilijamt
 */
public class Archive {

    /**
     * The ID of the Archive
     */
    private String id;

    /**
     * The name of the Archive, it will be stored in the metadata, as amazon
     * glacier doesn't support names
     */
    private String name;

    /**
     * The date when the archive was last modified
     */
    private Date mtime;

    /**
     * When was the archive created
     */
    private Date created;

    /**
     * What is the size of the archive
     */
    private long size;

    /**
     * What is the hash of the archive
     */
    private String hash;

    /**
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return the mtime
     */
    public Date getMtime() {
	return mtime;
    }

    /**
     * @param mtime
     *            the mtime to set
     */
    public void setMtime(Date mtime) {
	this.mtime = mtime;
    }

    /**
     * @return the created
     */
    public Date getCreated() {
	return created;
    }

    /**
     * @param created
     *            the created to set
     */
    public void setCreated(Date created) {
	this.created = created;
    }

    /**
     * @return the size
     */
    public long getSize() {
	return size;
    }

    /**
     * @param size
     *            the size to set
     */
    public void setSize(long size) {
	this.size = size;
    }

    /**
     * @return the hash
     */
    public String getHash() {
	return hash;
    }

    /**
     * @param hash
     *            the hash to set
     */
    public void setHash(String hash) {
	this.hash = hash;
    }

}

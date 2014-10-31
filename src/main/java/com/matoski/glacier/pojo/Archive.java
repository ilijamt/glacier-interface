package com.matoski.glacier.pojo;

import java.util.Date;

import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.matoski.glacier.enums.ArchiveState;

/**
 * An archive element, it's used to store all the relevant information about the
 * archive in one place
 * 
 * @author ilijamt
 */
public class Archive {

    /**
     * The state of the file in the journal, default state is
     * {@link ArchiveState#NOT_DEFINED}
     */
    private ArchiveState state = ArchiveState.NOT_DEFINED;

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
     * The date when the archive was last modified, as a timestamp
     */
    private long modifiedDate;

    /**
     * When was the archive created
     */
    private Date createdDate;

    /**
     * What is the size of the archive
     */
    private long size;

    /**
     * What is the hash of the archive
     */
    private String hash;

    /**
     * Uri of the uploaded file
     */
    private String uri;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {

	if (!(obj instanceof Archive)) {
	    return false;
	}

	Archive archive = (Archive) obj;

	return ((null != this.id) ? this.id.equals(archive.id) : true)
		&& ((null != this.name) ? this.name.equals(archive.name) : true)
		&& (this.size == archive.size)
		&& ((null != this.hash) ? this.hash.equals(archive.hash) : true)
		&& ((null != this.uri) ? this.uri.equals(archive.uri) : true)
		&& ((null != this.createdDate) ? (this.createdDate
			.compareTo(archive.createdDate) == 0) : true)
		&& (this.modifiedDate == archive.modifiedDate)
		&& (this.state.equals(archive.state));

    }

    /**
     * @return the created
     */
    public Date getCreatedDate() {
	return createdDate;
    }

    /**
     * @return the hash
     */
    public String getHash() {
	return hash;
    }

    /**
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * Get's the key used to store it in the Journal
     * 
     * @return
     */
    public String getKeyId() {
	return this.name;
    }

    /**
     * @return the mtime
     */
    public long getModifiedDate() {
	return modifiedDate;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @return the size
     */
    public long getSize() {
	return size;
    }

    /**
     * Get the state of the Archive
     * 
     * @return
     */
    public ArchiveState getState() {
	return state;
    }

    /**
     * get the uploaded uri
     * 
     * @return
     */
    public String getUri() {
	return uri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
	return this.id.hashCode();
    }

    /**
     * @param created
     *            the created to set
     */
    public void setCreatedDate(Date created) {
	this.createdDate = created;
    }

    public void setCreatedDate(String date) {
	this.createdDate = ISO8601Utils.parse(date);
    }

    /**
     * @param hash
     *            the hash to set
     */
    public void setHash(String hash) {
	this.hash = hash;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * @param mtime
     *            the mtime to set
     */
    public void setModifiedDate(long date) {
	this.modifiedDate = date;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @param size
     *            the size to set
     */
    public void setSize(long size) {
	this.size = size;
    }

    /**
     * Sets the archive status
     * 
     * @param state
     */
    public void setState(ArchiveState state) {
	this.state = state;
    }

    /**
     * Set the uploaded uri
     * 
     * @param uri
     */
    public void setUri(String uri) {
	this.uri = uri;
    }
}

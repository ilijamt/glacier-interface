package com.matoski.glacier.pojo.archive;

import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.matoski.glacier.enums.ArchiveState;

import java.io.File;
import java.util.Date;

/**
 * An archive element, it's used to store all the relevant information about the archive in one
 * place
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class Archive implements Cloneable {

    /**
     * The state of the file in the journal, default state is {@link ArchiveState#NOT_DEFINED}.
     */
    private ArchiveState state = ArchiveState.NOT_DEFINED;

    /**
     * The ID of the Archive.
     */
    private String id;

    /**
     * The name of the Archive, it will be stored in the metadata, as amazon glacier doesn't support
     * names.
     */
    private String name;

    /**
     * The date when the archive was last modified, as a timestamp.
     */
    private long modifiedDate;

    /**
     * When was the archive created.
     */
    private Date createdDate;

    /**
     * What is the size of the archive.
     */
    private long size;

    /**
     * What is the hash of the archive.
     */
    private String hash;

    /**
     * Uri of the uploaded file.
     */
    private String uri;

    /**
     * Constructor.
     */
    public Archive() {
        super();
    }

    /**
     * Constructor.
     *
     * @param state        The initial state of the archive
     * @param id           The id of the archive
     * @param name         The name of the archive
     * @param modifiedDate The modified date of the archive
     * @param createdDate  The created date of the archive
     * @param size         The size of the archive
     * @param hash         The hash of the archive
     * @param uri          The uri of the archive
     */
    public Archive(ArchiveState state, String id, String name, long modifiedDate, Date createdDate,
                   long size, String hash, String uri) {
        super();
        this.state = state;
        this.id = id;
        this.name = name;
        this.modifiedDate = modifiedDate;
        this.createdDate = createdDate;
        this.size = size;
        this.hash = hash;
        this.uri = uri;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public Object clone() {
        return new Archive(getState(), getId(), getName(), getModifiedDate(), getCreatedDate(),
                getSize(), getHash(), getUri());
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Archive)) {
            return false;
        }

        Archive archive = (Archive) obj;

        return (null != this.id ? this.id.equals(archive.id) : true)
                && (null != this.name ? this.name.equals(archive.name) : true)
                && this.size == archive.size
                && (null != this.hash ? this.hash.equals(archive.hash) : true)
                && (null != this.uri ? this.uri.equals(archive.uri) : true)
                && (null != this.createdDate ? this.createdDate.compareTo(archive.createdDate) == 0 : true)
                && this.modifiedDate == archive.modifiedDate && this.state.equals(archive.state);
    }

    /**
     * Gets the created date of the archive.
     *
     * @return {@link #createdDate}
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * Sets the created date for the archive, this sets {@link #createdDate}.
     *
     * @param date The string to parse for date
     */
    public void setCreatedDate(String date) {
        this.createdDate = ISO8601Utils.parse(date);
    }

    /**
     * Gets the hash of the archive.
     *
     * @return {@link #hash}
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets the hash for the archive, this sets {@link #hash}.
     *
     * @param hash the hash to set
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Gets the Amazon Glacier Archive ID, this is used to do various operations on the archive.
     *
     * @return {@link #id}
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the archive ID on amazon glacier, this sets {@link #id}.
     *
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get's the key used to store it in the Journal.
     *
     * @return {@link #id}
     */
    public String getKeyId() {
        return this.id;
    }

    /**
     * Gets the last modified date.
     *
     * @return {@link #modifiedDate}
     */
    public long getModifiedDate() {
        return modifiedDate;
    }

    /**
     * Sets the modified date for the archive, this sets {@link #modifiedDate}.
     *
     * @param date the date to set
     */
    public void setModifiedDate(long date) {
        this.modifiedDate = date;
    }

    /**
     * Gets the archive name, this is stored in the archive description as glacier doesn't allow for
     * storage of names.
     *
     * @return {@link #name}
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for the archive, this sets {@link #name}.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the archive size.
     *
     * @return {@link #size}
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets the archive size, this sets {@link #size}.
     *
     * @param size the size to set
     */
    public void setSize(long size) {
        this.size = size;
    }

    /**
     * Get the state of the Archive.
     *
     * @return {@link #state}
     */
    public ArchiveState getState() {
        return state;
    }

    /**
     * Sets the archive status, this sets {@link #state}.
     *
     * @param state The archive state to set.
     */
    public void setState(ArchiveState state) {
        this.state = state;
    }

    /**
     * Gets the uploaded URI.
     *
     * @return {@link #uri}
     */
    public String getUri() {
        return uri;
    }

    /**
     * Set the uploaded uri, this sets {@link #uri}.
     *
     * @param uri The uri to set.
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * Sets the created date for the archive, this sets {@link #createdDate}.
     *
     * @param created the created to set
     */
    public void setCreatedDate(Date created) {
        this.createdDate = created;
    }

    /**
     * Get the file to the archive.
     *
     * @return {@link File}({@link #getName()})
     */
    public File getFile() {
        return new File(getName());
    }

    /**
     * Get the file to the archive with a parent.
     *
     * @param parent The parent to use for creating {@link File}
     * @return {@link File}({@link #getName()})
     */
    public File getFile(String parent) {
        return new File(parent, getName());
    }
}
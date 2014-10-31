package com.matoski.glacier.pojo.journal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.matoski.glacier.enums.Metadata;
import com.matoski.glacier.pojo.Archive;

public class FileJournal {

    /**
     * The real, contains all the action done in the journal, this is used to
     * generate and save the journal. All actions are replayed on creation, so
     * we can actually have the whole picture.
     */
    private List<Archive> journal = new ArrayList<Archive>();

    /**
     * Vault ARN
     */
    private String ARN;

    /**
     * Inventory date
     */
    private Date date;

    /**
     * Metadata used in the inventory
     */
    private Metadata metadata;

    /**
     * Vault Name
     */
    private String name;

    /**
     * The size of the journal
     * 
     * @return
     */
    public Integer size() {
	return journal.size();
    }

    /**
     * Add an archive to the journal
     * 
     * @param archive
     */
    public void addArchive(Archive archive) {
	this.journal.add(archive);
    }

    /**
     * @return the aRN
     */
    public String getARN() {
	return ARN;
    }

    /**
     * @return the date
     */
    public Date getDate() {
	return date;
    }

    /**
     * @return the journal
     */
    public List<Archive> getJournal() {
	return journal;
    }

    /**
     * @return the metadata
     */
    public Metadata getMetadata() {
	return metadata;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param aRN
     *            the aRN to set
     */
    public void setARN(String aRN) {
	ARN = aRN;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(Date date) {
	this.date = date;
    }

    /**
     * @param journal
     *            the journal to set
     */
    public void setJournal(List<Archive> journal) {
	this.journal = journal;
    }

    /**
     * @param metadata
     *            the metadata to set
     */
    public void setMetadata(Metadata metadata) {
	this.metadata = metadata;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

}

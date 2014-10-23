package com.matoski.glacier.pojo;

import java.util.List;

/**
 * Journal for the vault
 * 
 * @author ilijamt
 */
public class Journal {

    /**
     * List of archives
     */
    private List<Archive> archives;

    /**
     * The vault to which the journal belongs to
     */
    private String vault;

    /**
     * The inventory id used to fetch the data
     */
    private String inventory;

    /**
     * Get all archives
     * 
     * @return
     */
    public List<Archive> getArchives() {
	return archives;
    }

    /**
     * Set the archives
     * 
     * @param archives
     */
    public void setArchives(List<Archive> archives) {
	this.archives = archives;
    }

    /**
     * Add an archive to the journal
     * 
     * @param archive
     */
    public void addArchive(Archive archive) {
	this.archives.add(archive);
    }

    /**
     * @return the vault
     */
    public String getVault() {
	return vault;
    }

    /**
     * @param vault
     *            the vault to set
     */
    public void setVault(String vault) {
	this.vault = vault;
    }

}
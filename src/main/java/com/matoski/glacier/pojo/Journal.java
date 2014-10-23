package com.matoski.glacier.pojo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.google.gson.GsonBuilder;
import com.matoski.glacier.enums.Metadata;
import com.matoski.glacier.errors.InvalidMetadataException;
import com.matoski.glacier.interfaces.IGlacierInterfaceMetadata;
import com.matoski.glacier.pojo.GlacierInventory.ArchiveItem;
import com.matoski.glacier.util.Parser;

/**
 * Journal for the vault
 * 
 * @author ilijamt
 */
public class Journal {

    /**
     * History, contains all the action done in the journal, whenever you
     * replace an item it is pushed into the history.
     */
    private List<Archive> history;

    /**
     * List of archives
     */
    private HashMap<String, Archive> archives = new HashMap<String, Archive>();

    /**
     * Vault ARN
     */
    private String ARN;

    /**
     * @return the metadata
     */
    public Metadata getMetadata() {
	return metadata;
    }

    /**
     * @param metadata
     *            the metadata to set
     */
    public void setMetadata(Metadata metadata) {
	this.metadata = metadata;
    }

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
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @return the archives
     */
    public HashMap<String, Archive> getArchives() {
	return archives;
    }

    /**
     * @param archives
     *            the archives to set
     */
    public void setArchives(HashMap<String, Archive> archives) {
	this.archives = archives;
    }

    /**
     * Add an archive in the HashMap
     * 
     * @param archive
     * @return
     */
    public boolean addArchive(Archive archive) {
	Boolean keyExists = this.archives.containsKey(archive.getId());

	if (keyExists && archive.equals(this.archives.get(archive.getId()))) {
	    // we already have the same archive into journal,
	    // why ????
	    return false;
	}

	Archive previous = this.archives.put(archive.getId(), archive);

	if (null != previous) {
	    this.history.add(previous);
	}

	return true;
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
    public void setDate(String date) {
	this.date = ISO8601Utils.parse(date);
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(Date date) {
	this.date = date;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Parse the data so we can store it
     * 
     * @param inventory
     * @param vault
     * @param metadata
     * 
     * @return
     */
    public static Journal parse(GlacierInventory inventory, String vault,
	    String metadata) {
	return Journal.parse(inventory, vault, Metadata.from(metadata));
    }

    /**
     * Parse the data so we can store it
     * 
     * @param inventory
     * @param vault
     * @param metadata
     * 
     * @return
     */
    public static Journal parse(GlacierInventory inventory, String vault,
	    Metadata metadata) {

	Journal journal = new Journal();

	journal.setName(vault);
	journal.setARN(inventory.getVaultARN());
	journal.setDate(inventory.getInventoryDate());
	journal.setMetadata(metadata);

	for (ArchiveItem archiveItem : inventory.getArchiveList()) {

	    Archive archive = new Archive();

	    archive.setId(archiveItem.getArchiveId());
	    archive.setCreatedDate(archiveItem.getCreationDate());
	    archive.setSize(archiveItem.getSize());
	    archive.setHash(archiveItem.getSHA256TreeHash());

	    IGlacierInterfaceMetadata interfaceMetadata;

	    try {

		interfaceMetadata = Parser.parse(metadata,
			archiveItem.getArchiveDescription());

		archive.setName(interfaceMetadata.giGetName());
		
		Date d = interfaceMetadata.giGetModifiedDate();
		
		archive.setModifiedDate(d);

	    } catch (InvalidMetadataException e) {
		System.err.println(String.format("ERROR: %s", e.getMessage()));
	    }

	    journal.addArchive(archive);

	}

	return journal;
    }

    /**
     * Save the journal to a file as a JSON
     * 
     * @param filename
     * 
     * @throws IOException
     */
    public void save(String filename) throws IOException {

	File file = new File(filename);
	FileWriter fileWriter = null;
	BufferedWriter bufferedWriter = null;

	if (!file.exists()) {
	    file.createNewFile();
	}

	fileWriter = new FileWriter(file.getAbsoluteFile());
	bufferedWriter = new BufferedWriter(fileWriter);
	bufferedWriter.write(new GsonBuilder().setPrettyPrinting().create()
		.toJson(this));

	bufferedWriter.close();
	fileWriter.close();

    }
}
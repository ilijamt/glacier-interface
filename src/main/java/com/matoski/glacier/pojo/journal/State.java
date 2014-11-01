package com.matoski.glacier.pojo.journal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import com.amazonaws.services.glacier.TreeHashGenerator;
import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matoski.glacier.enums.ArchiveState;
import com.matoski.glacier.enums.GenericValidateEnum;
import com.matoski.glacier.enums.Metadata;
import com.matoski.glacier.errors.InvalidMetadataException;
import com.matoski.glacier.interfaces.IGlacierInterfaceMetadata;
import com.matoski.glacier.pojo.Archive;
import com.matoski.glacier.pojo.GlacierInventory;
import com.matoski.glacier.pojo.GlacierInventory.ArchiveItem;
import com.matoski.glacier.util.Parser;

/**
 * Journal for the vault
 * 
 * @author ilijamt
 */
public class State {

    /**
     * Checks if the size in the archive is the same as it's in the file
     * 
     * @param archive
     * @return
     */
    public static GenericValidateEnum archiveValidateFileSize(Archive archive) {
	return new File(archive.getName()).length() == archive.getSize() ? GenericValidateEnum.VALID : GenericValidateEnum.INVALID;
    }

    /**
     * We check if the last modified is the same as the one in archive
     * 
     * @param archive
     * @return
     */
    public static GenericValidateEnum archiveValidateLastModified(Archive archive) {
	return new File(archive.getName()).lastModified() == archive.getModifiedDate() ? GenericValidateEnum.VALID
		: GenericValidateEnum.INVALID;
    }

    /**
     * Checks if the hash is correct between the archive and the file on the
     * disk
     * 
     * @param archive
     * @return
     */
    public static GenericValidateEnum archiveValidateTreeHash(Archive archive) {
	File file = new File(archive.getName());
	String checksum = TreeHashGenerator.calculateTreeHash(file);

	return archive.getHash().equals(checksum) ? GenericValidateEnum.VALID : GenericValidateEnum.INVALID;
    }

    /**
     * Load the journal into memory
     * 
     * @param file
     * 
     * @return
     * 
     * @throws IOException
     */
    public static State load(File file) throws IOException {

	if (!file.exists()) {
	    throw new IOException();
	}

	State journal = new State();

	String json = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);

	FileJournal fileJournal = new Gson().fromJson(json, FileJournal.class);

	journal.replay(fileJournal);
	journal.setFile(file);

	return journal;

    }

    /**
     * Load the journal into memory
     * 
     * @param file
     * 
     * @return
     * 
     * @throws IOException
     */
    public static State load(String file) throws IOException {
	return load(new File(file));
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
    public static State parse(GlacierInventory inventory, String vault, Metadata metadata) {

	State journal = new State();

	journal.setName(vault);
	journal.setDate(inventory.getInventoryDate());
	journal.setMetadata(metadata);

	for (ArchiveItem archiveItem : inventory.getArchiveList()) {

	    Archive archive = new Archive();

	    archive.setState(ArchiveState.CREATE);
	    archive.setId(archiveItem.getArchiveId());
	    archive.setCreatedDate(archiveItem.getCreationDate());
	    archive.setSize(archiveItem.getSize());
	    archive.setHash(archiveItem.getSHA256TreeHash());

	    IGlacierInterfaceMetadata interfaceMetadata;

	    try {

		interfaceMetadata = Parser.parse(metadata, archiveItem.getArchiveDescription());

		archive.setName(interfaceMetadata.giGetName());
		archive.setModifiedDate(interfaceMetadata.giGetModifiedDate());

	    } catch (NullPointerException e) {
		System.err.println("ERROR: Invalid metadata parser");
	    } catch (InvalidMetadataException e) {
		System.err.println(String.format("ERROR: %s", e.getMessage()));
	    }

	    journal.addArchive(archive);

	}

	return journal;
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
    public static State parse(GlacierInventory inventory, String vault, String metadata) {
	return State.parse(inventory, vault, Metadata.from(metadata));
    }

    /**
     * The replayed journal
     */
    private HashMap<String, Archive> archives = new HashMap<String, Archive>();

    /**
     * The journal
     */
    private transient FileJournal journal = new FileJournal();

    /**
     * The file that is used for the journal
     */
    private transient File file;

    /**
     * Add an archive in the HashMap
     * 
     * @param archive
     */
    public void addArchive(Archive archive) {

	journal.addArchive(archive);

	switch (archive.getState()) {
	case DELETE:
	    archives.remove(archive.getKeyId());
	    break;
	case CREATE:
	case DOWNLOAD:
	case NOT_DEFINED:
	    archives.put(archive.getKeyId(), archive);
	    break;
	default:
	    break;
	}
    }

    /**
     * Delete archive from the journal
     * 
     * @param id
     */
    public void deleteArchive(String id) {

	Archive archive = getById(id);

	if (null != archive) {
	    archive = (Archive) archive.clone();
	    archive.setState(ArchiveState.DELETE);
	    addArchive(archive);
	}

    }

    /**
     * @return the archives
     */
    public HashMap<String, Archive> getArchives() {
	return archives;
    }

    /**
     * Get the archive by ID
     * 
     * @param id
     * @return
     */
    public Archive getById(String id) {
	Archive archive = null;

	for (Entry<String, Archive> entry : this.archives.entrySet()) {

	    if (entry.getValue().getId().equals(id)) {
		archive = entry.getValue();
		break;
	    }

	}

	return archive;
    }

    /**
     * Get the archive by Name
     * 
     * @param name
     * @return
     */
    public Archive getByName(String name) {

	Archive archive = null;

	// go through all the archives and check if it's there or not
	for (Entry<String, Archive> entry : this.archives.entrySet()) {

	    if (entry.getValue().getName().equals(name)) {
		archive = entry.getValue();
		break;
	    }

	}

	return archive;
    }

    /**
     * @return the date
     */
    public Date getDate() {
	return journal.getDate();
    }

    /**
     * Get the file to use
     * 
     * @return
     */
    public File getFile() {
	return this.file;
    }

    /**
     * Gets the journal
     * 
     * @return
     */
    public FileJournal getJournal() {
	return journal;
    }

    /**
     * @return the metadata
     */
    public Metadata getMetadata() {
	return journal.getMetadata();
    }

    /**
     * @return the name
     */
    public String getName() {
	return journal.getName();
    }

    /**
     * Is the file in archive?
     * 
     * @param file
     * @return
     */
    public Boolean isFileInArchive(String file) {
	return null != getByName(file);
    }

    /**
     * Replay the journal from {@link FileJournal}
     * 
     * @param journal
     */
    protected void replay(FileJournal journal) {

	for (Archive archive : journal.getJournal()) {
	    this.addArchive(archive);
	}

	this.setName(journal.getName());
	this.setMetadata(journal.getMetadata());
	this.setDate(journal.getDate());

    }

    /**
     * Save the journal to a file as a JSON
     * 
     * @throws IOException
     */
    public void save() throws IOException {
	save(this.getFile());
    }

    /**
     * Save the journal to a file as a JSON
     * 
     * @param file
     * 
     * @throws IOException
     */
    public void save(File file) throws IOException {

	if (!file.exists()) {
	    file.createNewFile();
	}

	FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
	BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	bufferedWriter.write(new GsonBuilder().setPrettyPrinting().create().toJson(journal));

	bufferedWriter.close();
	fileWriter.close();

    }

    /**
     * Save the journal to a file as a JSON
     * 
     * @param file
     * 
     * @throws IOException
     */
    public void save(String file) throws IOException {
	save(new File(file));
    }

    /**
     * @param date
     *            the date to set
     * @return
     */
    public State setDate(Date date) {
	journal.setDate(date);
	return this;
    }

    /**
     * @param date
     *            the date to set
     * @return
     */
    public State setDate(String date) {
	journal.setDate(ISO8601Utils.parse(date));
	return this;
    }

    /**
     * Sets the file to use
     * 
     * @param file
     * @return
     */
    public State setFile(File file) {
	this.file = file;
	return this;
    }

    /**
     * Sets the file to use
     * 
     * @param file
     * @return
     */
    public State setFile(String file) {
	this.file = new File(file);
	return this;
    }

    /**
     * @param metadata
     *            the metadata to set
     * @return
     */
    public State setMetadata(Metadata metadata) {
	journal.setMetadata(metadata);
	return this;
    }

    /**
     * Set the metadata
     * 
     * @param metadata
     * @return
     */
    public State setMetadata(String metadata) {
	journal.setMetadata(Metadata.from(metadata));
	return this;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	journal.setName(name);
    }

    /**
     * The size of the journal
     * 
     * @return
     */
    public Integer size() {
	return archives.size();
    }

}
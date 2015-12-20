package com.matoski.glacier.pojo.journal;

import com.amazonaws.services.glacier.TreeHashGenerator;
import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matoski.glacier.enums.ArchiveState;
import com.matoski.glacier.enums.GenericValidateEnum;
import com.matoski.glacier.enums.Metadata;
import com.matoski.glacier.errors.InvalidMetadataException;
import com.matoski.glacier.interfaces.IGlacierInterfaceMetadata;
import com.matoski.glacier.pojo.archive.Archive;
import com.matoski.glacier.pojo.archive.GlacierInventory;
import com.matoski.glacier.pojo.archive.GlacierInventory.ArchiveItem;
import com.matoski.glacier.util.Parse;
import com.matoski.glacier.util.Parser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Journal for the vault
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class State {

    /**
     * The replayed journal.
     */
    private HashMap<String, Archive> archives = new HashMap<String, Archive>();
    /**
     * The journal.
     */
    private transient FileJournal journal = new FileJournal();
    /**
     * The file that is used for the journal.
     */
    private transient File file;

    /**
     * Checks if the size in the archive is the same as it's in the file.
     *
     * @param archive The archive to validate for file size
     * @return Result of the validation
     */
    public static GenericValidateEnum archiveValidateFileSize(Archive archive) {
        return new File(archive.getName()).length() == archive.getSize() ? GenericValidateEnum.VALID
                : GenericValidateEnum.INVALID;
    }

    /**
     * We check if the last modified is the same as the one in archive.
     *
     * @param archive The archive to validate for last modified
     * @return Result of the validation
     */
    public static GenericValidateEnum archiveValidateLastModified(Archive archive) {
        return (new File(archive.getName()).lastModified() == archive.getModifiedDate())
                ? GenericValidateEnum.VALID : GenericValidateEnum.INVALID;
    }

    /**
     * Checks if the hash is correct between the archive and the file on the disk.
     *
     * @param archive The archive to validate tree hash
     * @return Result of the validation
     */
    public static GenericValidateEnum archiveValidateTreeHash(Archive archive) {
        File file = new File(archive.getName());

        String checksum = null;
        GenericValidateEnum state = GenericValidateEnum.ERROR;

        try {
            checksum = TreeHashGenerator.calculateTreeHash(file);
            state = archive.getHash().equals(checksum) ? GenericValidateEnum.VALID
                    : GenericValidateEnum.INVALID;
        } catch (Exception e) {
            state = GenericValidateEnum.ERROR;
        }

        return state;

    }

    /**
     * Load the journal into memory.
     *
     * @param file The journal file
     * @return The state
     * @throws FileNotFoundException If the journal is not present it will throw an error
     * @throws IOException           If the journal cannot be read or some other IO related error it will throw an
     *                               exception
     */
    public static State load(File file) throws FileNotFoundException, IOException {

        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        State journal = new State();

        String json = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);

        FileJournal fileJournal = new Gson().fromJson(json, FileJournal.class);

        journal.replay(fileJournal);
        journal.setFile(file);

        return journal;

    }

    /**
     * Load the journal into memory.
     *
     * @param file The journal file
     * @return The state
     * @throws FileNotFoundException If the journal is not present it will throw an error
     * @throws IOException           If the journal cannot be read or some other IO related error it will throw an
     *                               exception
     */
    public static State load(String file) throws FileNotFoundException, IOException {
        return load(new File(file));
    }

    /**
     * Parse the data so we can store it.
     *
     * @param inventory The inventory to parse
     * @param vault     The vault name
     * @param metadata  The type of metadata
     * @return The state
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
            archive.setHash(archiveItem.getSha256TreeHash());

            IGlacierInterfaceMetadata interfaceMetadata;

            try {

                interfaceMetadata = Parser.parse(metadata, archiveItem.getArchiveDescription());

                archive.setName(interfaceMetadata.giGetName());
                archive.setModifiedDate(interfaceMetadata.giGetModifiedDate());

            } catch (NullPointerException e) {
                System.err.println("ERROR: Invalid metadata parser");
            } catch (InvalidMetadataException e) {
                System.err.println(String.format("ERROR: %s", e));
            }

            journal.addArchive(archive);

        }

        return journal;
    }

    /**
     * Parse the data so we can store it.
     *
     * @param inventory The inventory to parse
     * @param vault     The vault name
     * @param metadata  The type of metadata
     * @return The state
     */
    public static State parse(GlacierInventory inventory, String vault, String metadata) {
        return State.parse(inventory, vault, Metadata.from(metadata));
    }

    /**
     * Add an archive in the HashMap.
     *
     * @param archive The archive we add to the journal
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
     * Delete archive from the journal.
     *
     * @param id The ID of the archive
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
     * Get's all archives in the journal.
     *
     * @return The archives
     */
    public HashMap<String, Archive> getArchives() {
        return this.archives;
    }

    /**
     * Get the archive by ID.
     *
     * @param id The ID of the archive to get
     * @return The requested ID
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
     * Get the archive by Name.
     *
     * @param name The archive name
     * @return The requested archive
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
     * Get the journal date.
     *
     * @return {@link #getDate()}
     */
    public Date getDate() {
        return journal.getDate();
    }

    /**
     * Sets the journal date.
     *
     * @param date the date to set
     * @return To be able to chain
     */
    public State setDate(String date) {
        journal.setDate(Parse.ISO8601StringDateParse(date));
        return this;
    }

    /**
     * Get the file to use.
     *
     * @return {@link #file}
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Sets the file to use.
     *
     * @param file Which file to use for the journal
     * @return To be able to chain
     */
    public State setFile(String file) {
        this.file = new File(file);
        return this;
    }

    /**
     * Gets the journal.
     *
     * @return {@link #journal}
     */
    public FileJournal getJournal() {
        return journal;
    }

    /**
     * Get the journal metadata.
     *
     * @return Journal metadata
     */
    public Metadata getMetadata() {
        return journal.getMetadata();
    }

    /**
     * The journal metadata.
     *
     * @param metadata the metadata to set
     * @return To be able to chain
     */
    public State setMetadata(String metadata) {
        journal.setMetadata(Metadata.from(metadata));
        return this;
    }

    /**
     * Get the journal name.
     *
     * @return Journal name
     */
    public String getName() {
        return journal.getName();
    }

    /**
     * Set the journal name.
     *
     * @param name the name to set
     * @return To be able to chain
     */
    public State setName(String name) {
        journal.setName(name);
        return this;
    }

    /**
     * Is the file in archive.
     *
     * @param file The file we check if it is present in the archives
     * @return true if the file is present in the archive
     */
    public Boolean isFileInArchive(String file) {
        return null != getByName(file);
    }

    /**
     * Replay the journal from {@link FileJournal}.
     *
     * @param journal The journal to replay
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
     * Save the journal to a file as a JSON.
     *
     * @throws IOException Throws an error if we can't save the journal
     */
    public void save() throws IOException {
        save(this.getFile());
    }

    /**
     * Save the journal to a file as a JSON.
     *
     * @param file The file to which we save the journal
     * @return true if it saved correctly
     * @throws IOException Throws an error if we can't save the journal
     */
    public boolean save(File file) throws IOException {

        if (!file.exists() && !file.createNewFile()) {
            return false;
        }

        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(new GsonBuilder().setPrettyPrinting().create().toJson(journal));

        bufferedWriter.close();
        fileWriter.close();

        return true;

    }

    /**
     * Save the journal to a file as a JSON.
     *
     * @param file The file to which we save the journal
     * @return true if it saved correctly
     * @throws IOException Throws an error if we can't save the journal
     */
    public boolean save(String file) throws IOException {
        return save(new File(file));
    }

    /**
     * Sets the journal date.
     *
     * @param date the date to set
     * @return To be able to chain
     */
    public State setDate(Date date) {
        journal.setDate(date);
        return this;
    }

    /**
     * Sets the file to use.
     *
     * @param file Which file to use for the journal
     * @return To be able to chain
     */
    public State setFile(File file) {
        this.file = file;
        return this;
    }

    /**
     * The journal metadata.
     *
     * @param metadata the metadata to set
     * @return To be able to chain
     */
    public State setMetadata(Metadata metadata) {
        journal.setMetadata(metadata);
        return this;
    }

    /**
     * The size of the journal.
     *
     * @return The size of the journal
     */
    public Integer size() {
        return archives.size();
    }

}
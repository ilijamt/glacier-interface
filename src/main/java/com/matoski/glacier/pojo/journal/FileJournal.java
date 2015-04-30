package com.matoski.glacier.pojo.journal;

import com.matoski.glacier.enums.Metadata;
import com.matoski.glacier.pojo.archive.Archive;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is the file journal, it contains all the actions that have happened with this journal.
 * <p/>
 * <p>
 * This is used to create a real picture of the journal, as all events are replayed until we have
 * the final state that we use in the application.
 * </p>
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class FileJournal {

    /**
     * The real, contains all the action done in the journal, this is used to generate and save the
     * journal. All actions are replayed on creation, so we can actually have the whole picture.
     */
    private List<Archive> journal = new ArrayList<Archive>();

    /**
     * Inventory date.
     */
    private Date date;

    /**
     * Metadata used in the inventory.
     */
    private Metadata metadata;

    /**
     * Vault Name.
     */
    private String name;

    /**
     * Add an archive to the journal.
     *
     * @param archive The archive to add to the journal
     */
    public void addArchive(Archive archive) {
        this.journal.add(archive);
    }

    /**
     * Get date the journal was created.
     *
     * @return {@link #date}
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set the creation date.
     *
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Get the journal entries.
     *
     * @return {@link #journal}
     */
    public List<Archive> getJournal() {
        return journal;
    }

    /**
     * Set the journal.
     *
     * @param journal the journal to set
     */
    public void setJournal(List<Archive> journal) {
        this.journal = journal;
    }

    /**
     * Get the metadata used in the journal.
     *
     * @return {@link #metadata}
     */
    public Metadata getMetadata() {
        return metadata;
    }

    /**
     * Set the metadata.
     *
     * @param metadata the metadata to set
     */
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    /**
     * Get the name of the journal.
     *
     * @return {@link #name}
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the journal.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The size of the journal.
     *
     * @return size of the journal
     */
    public Integer size() {
        return journal.size();
    }

}

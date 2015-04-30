package com.matoski.glacier.pojo.job;

import com.matoski.glacier.base.AbstractWritablePojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Download job info
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class DownloadJobInfo extends AbstractWritablePojo<DownloadJobInfo> {

    /**
     * A list of available jobs.
     */
    private List<DownloadJob> jobs = new ArrayList<DownloadJob>();

    /**
     * The directory where this needs to happen.
     */
    private String directory;

    /**
     * The journal used for the job.
     */
    private String journal;

    /**
     * Get the journal.
     *
     * @return {@link #journal}
     */
    public String getJournal() {
        return journal;
    }

    /**
     * Set journal.
     *
     * @param journal the journal to set
     */
    public void setJournal(String journal) {
        this.journal = journal;
    }

    /**
     * Add a single job.
     *
     * @param job Job to add
     */
    public void addJob(DownloadJob job) {
        this.jobs.add(job);
        setDirty();
    }

    /**
     * Get the working directory.
     *
     * @return {@link #directory}
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Set the working directory.
     *
     * @param directory the directory to set
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * Get the available jobs.
     *
     * @return {@link #jobs}
     */
    public List<DownloadJob> getJobs() {
        return jobs;
    }

    /**
     * Set jobs.
     *
     * @param jobs the jobs to set
     */
    public void setJobs(List<DownloadJob> jobs) {
        this.jobs = jobs;
        setDirty();
    }

}

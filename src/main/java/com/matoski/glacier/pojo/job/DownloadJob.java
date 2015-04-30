package com.matoski.glacier.pojo.job;

import java.util.Date;

/**
 * Download job
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class DownloadJob {

    /**
     * When was this created.
     */
    private Date created;

    /**
     * The ID of the job.
     */
    private String jobId;

    /**
     * The ID of the archive.
     */
    private String archiveId;

    /**
     * The vault name of the download.
     */
    private String vaultName;

    /**
     * The name of the archive.
     */
    private String name;

    /**
     * Constructor.
     */
    public DownloadJob() {
        created = new Date();
    }

    /**
     * Get the archive ID.
     *
     * @return {@link #archiveId}
     */
    public String getArchiveId() {
        return archiveId;
    }

    /**
     * Set the archive ID.
     *
     * @param archiveId the archiveId to set
     */
    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    /**
     * When was this created.
     *
     * @return {@link #created}
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Set when was this job created.
     *
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * Get the job Id.
     *
     * @return {@link #jobId}
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * Sets the jobId.
     *
     * @param jobId the job id
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /**
     * Get the name of the job.
     *
     * @return {@link #name}
     */
    public String getName() {
        return name;
    }

    /**
     * Set name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the vault name where the job is.
     *
     * @return {@link #vaultName}
     */
    public String getVaultName() {
        return vaultName;
    }

    /**
     * Sets the vault name for the jobId.
     *
     * @param vaultName Vault name for the job
     */
    public void setVaultName(String vaultName) {
        this.vaultName = vaultName;
    }

}
package com.matoski.glacier.pojo.job;

import java.util.Date;

/**
 * Download job
 * 
 * @author ilijamt
 *
 */
public class DownloadJob {

    /**
     * When was this created
     */
    private Date created;

    /**
     * The ID of the job.
     */
    private String jobId;

    /**
     * The ID of the archive
     */
    private String archiveId;

    /**
     * The vault name of the download
     */
    private String vaultName;

    /**
     * The name of the archive
     */
    private String name;

    /**
     * Constructor
     */
    public DownloadJob() {
	created = new Date();
    }

    /**
     * @return the archiveId
     */
    public String getArchiveId() {
	return archiveId;
    }

    /**
     * When was this created
     * 
     * @return
     */
    public Date getCreated() {
	return created;
    }

    /**
     * Get the job Id
     * 
     * @return
     */
    public String getJobId() {
	return jobId;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * Get the vault name where the job is
     * 
     * @return
     */
    public String getVaultName() {
	return vaultName;
    }

    /**
     * @param archiveId
     *            the archiveId to set
     */
    public void setArchiveId(String archiveId) {
	this.archiveId = archiveId;
    }

    /**
     * @param created
     *            the created to set
     */
    public void setCreated(Date created) {
	this.created = created;
    }

    /**
     * Sets the jobId
     * 
     * @param jobId
     */
    public void setJobId(String jobId) {
	this.jobId = jobId;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Sets the vault name for the jobId
     * 
     * @param vaultName
     */
    public void setVaultName(String vaultName) {
	this.vaultName = vaultName;
    }

}
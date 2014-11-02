package com.matoski.glacier.pojo;

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
     * The vault name of the download
     */
    private String vaultName;

    /**
     * Constructor
     */
    public DownloadJob() {
	created = new Date();
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
     * Get the vault name where the job is
     * 
     * @return
     */
    public String getVaultName() {
	return vaultName;
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
     * Sets the vault name for the jobId
     * 
     * @param vaultName
     */
    public void setVaultName(String vaultName) {
	this.vaultName = vaultName;
    }

}
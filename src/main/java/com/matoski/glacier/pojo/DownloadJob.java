package com.matoski.glacier.pojo;

public class DownloadJob extends AbstractWritablePojo<DownloadJob> {

    /**
     * The archive ID of the download
     */
    private String archiveId;

    /**
     * The ID of the job.
     */
    private String jobId;

    /**
     * The vault name of the download
     */
    private String vaultName;

    /**
     * The name of the archive, relative to the working directory
     */
    private String archiveName;

    public String getArchiveId() {
	return archiveId;
    }

    public String getArchiveName() {
	return archiveName;
    }

    public String getJobId() {
	return jobId;
    }

    public String getVaultName() {
	return vaultName;
    }

    public void setArchiveId(String archiveId) {
	this.archiveId = archiveId;
    }

    public void setArchiveName(String archiveName) {
	this.archiveName = archiveName;
    }

    public void setJobId(String jobId) {
	this.jobId = jobId;
    }

    public void setVaultName(String vaultName) {
	this.vaultName = vaultName;
    }

}
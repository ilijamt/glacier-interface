package com.matoski.glacier.pojo.job;

import java.util.ArrayList;
import java.util.List;

import com.matoski.glacier.base.AbstractWritablePojo;

/**
 * Download job info
 * 
 * @author ilijamt
 */
public class DownloadJobInfo extends AbstractWritablePojo<DownloadJobInfo> {

    /**
     * A list of available jobs
     */
    private List<DownloadJob> jobs = new ArrayList<DownloadJob>();

    /**
     * The directory where this needs to happen
     */
    private String directory;

    /**
     * Add a single job
     * 
     * @param job
     */
    public void addJob(DownloadJob job) {
	this.jobs.add(job);
	setDirty();
    }

    /**
     * @return the directory
     */
    public String getDirectory() {
	return directory;
    }

    /**
     * @return the jobs
     */
    public List<DownloadJob> getJobs() {
	return jobs;
    }

    /**
     * @param directory
     *            the directory to set
     */
    public void setDirectory(String directory) {
	this.directory = directory;
    }

    /**
     * @param jobs
     *            the jobs to set
     */
    public void setJobs(List<DownloadJob> jobs) {
	this.jobs = jobs;
	setDirty();
    }

}

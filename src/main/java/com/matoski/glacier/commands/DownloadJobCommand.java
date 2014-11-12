package com.matoski.glacier.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandDownloadJob;
import com.matoski.glacier.errors.InvalidChecksumException;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.pojo.job.DownloadJob;
import com.matoski.glacier.pojo.job.DownloadJobInfo;
import com.matoski.glacier.util.AmazonGlacierBaseUtil;
import com.matoski.glacier.util.download.AmazonGlacierDownloadUtil;

/**
 * Creates a download job
 * 
 * @author ilijamt
 */
public class DownloadJobCommand extends AbstractCommand<CommandDownloadJob> {

    /**
     * The job
     */
    DownloadJobInfo jobInfo = null;

    /**
     * Download helper
     */
    AmazonGlacierDownloadUtil download = null;

    /**
     * Constructor
     * 
     * @param config
     * @param command
     * @throws VaultNameNotPresentException
     * @throws RegionNotSupportedException
     * @throws FileNotFoundException
     * @throws IllegalArgumentException
     */
    public DownloadJobCommand(Config config, CommandDownloadJob command) throws VaultNameNotPresentException, RegionNotSupportedException,
	    FileNotFoundException, IllegalArgumentException {
	super(config, command);

	if (command.partSize % 2 != 0 && command.partSize != 1) {
	    throw new IllegalArgumentException("Part size has to be a multiple of 2");
	}

	File jobFile = new File(command.jobFile);

	if (!jobFile.exists()) {
	    throw new FileNotFoundException(command.jobFile);
	}

	try {
	    jobInfo = DownloadJobInfo.load(command.jobFile, DownloadJobInfo.class);
	} catch (InstantiationException | IllegalAccessException | IOException e) {
	    e.printStackTrace();
	    throw new IllegalArgumentException("Job info couldn't be loaded");
	}

	if (null == jobInfo) {
	    throw new FileNotFoundException(command.jobFile);
	}

	download = new AmazonGlacierDownloadUtil(credentials, client, region);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {

	System.out.println("START: download-job");
	System.out.println();

	System.out.println(String.format("%s jobs to process", jobInfo.getJobs().size()));
	System.out.println();

	for (DownloadJob job : jobInfo.getJobs()) {
	    try {
		if (command.dryRun) {
		    System.out.println(String.format("[--dry-run] Skipping download for : %s [%s]", job.getName(), job.getArchiveId()));
		} else {
		    download.DownloadArchive(job, (long) command.partSize * AmazonGlacierBaseUtil.MINIMUM_PART_SIZE, command.overwrite);
		}
	    } catch (FileAlreadyExistsException e) {
		System.out.println(String.format("ERROR: [%s] %s already exists, skipping", job.getArchiveId(), job.getName()));
	    } catch (InvalidChecksumException e) {
		System.out.println(String.format("ERROR: Invalid checksum [%s] %s", job.getArchiveId(), job.getName()));
	    }
	    System.out.println();
	}

	System.out.println();
	System.out.println("END: download-job");
    }
}

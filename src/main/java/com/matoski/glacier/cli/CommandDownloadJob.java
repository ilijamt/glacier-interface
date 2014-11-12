package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.matoski.glacier.Constants;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "download-job", commandDescription = "Download the job")
public class CommandDownloadJob extends GenericCommand {

    @Parameter(required = true, names = "--job-file", description = "Uses this file to download all the archives in the file")
    public String jobFile;

    @Parameter(names = "--overwrite", description = "This will force overwrite the file if it exists")
    public Boolean overwrite = false;

    @Parameter(names = "--part-size", description = "How big chunks of data to download from amazon glacier during one request, the part size has to be multiple of 2, like 1MB, 2MB, 4MB, 8MB, ...")
    public Integer partSize = Constants.DEFAULT_PART_SIZE;

    @Parameter(names = "--dry-run", description = "Do not download the files, just go through the process")
    public Boolean dryRun = false;

    public CommandDownloadJob() {
	super(CliCommands.DownloadJob);
    }

}
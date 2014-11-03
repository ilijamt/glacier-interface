package com.matoski.glacier.cli;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.matoski.glacier.Constants;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "upload-archive", commandDescription = "Initiate an upload of archive")
public class CommandUploadArchive extends GenericCommand {

    @Parameter(names = "--vault", description = "The name of the vault from where the archive will be deleted, will be overwritten by --aws-vault if not specified")
    public String vaultName;

    @Parameter(required = true, names = "--file", description = "The file(s) to be uploaded, you can specify this parameter multiple times")
    public List<String> files = new ArrayList<String>();

    @Parameter(names = "--metadata", description = "Available: mt2, fgv2")
    public String metadata = Constants.DEFAULT_PARSER_METADATA;

    @Parameter(required = true, names = "--journal", description = "Journal")
    public String journal;

    @Parameter(names = "--part-size", description = "How big chunks of data to upload to amazon glacier during one request, the part size has to be multiple of 2, like 1MB, 2MB, 4MB, 8MB, ...")
    public Integer partSize = Constants.DEFAULT_PART_SIZE;

    @Parameter(names = "--retry-failed-upload", description = "How many times should it retry to upload a failed piece before giving up.")
    public Integer retryFailedUpload = Constants.DEFAULT_RETRY_FAILED_UPLOAD;

    @Parameter(names = "--concurrent", description = "How many threads to open to use when uploading the data to amazon glacier, the more threads you have the more memory it will eat. The memory requirements will be partSize * concurrent")
    public Integer concurrent = Constants.DEFAULT_CONCURRENT_THREADS;

    @Parameter(names = "--force-upload", description = "Force upload if it exist in the journal, but it will still keep the old archive, you will have to delete the old one on your own")
    public Boolean forceUpload = false;

    public CommandUploadArchive() {
	super(CliCommands.UploadArchive);
    }

}
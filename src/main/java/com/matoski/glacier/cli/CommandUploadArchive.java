package com.matoski.glacier.cli;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "upload-archive", commandDescription = "Initiate an upload of archive")
public class CommandUploadArchive {

    @Parameter(names = "--vault", description = "The name of the vault from where the archive will be deleted, will be overwriten by --aws-vault if not specified")
    public String vaultName;

    @Parameter(required = true, names = "--file", description = "The file(s) to be uploaded, you can specifiy this parameter multiple times")
    public List<String> files = new ArrayList<String>();

    @Parameter(names = "--metadata", description = "Available: mt2, fgv2")
    public String metadata = "mt2";

    @Parameter(required = true, names = "--journal", description = "Journal")
    public String journal;

    @Parameter(names = "--part-size", description = "How big chunks of data to upload to amazon glacier during one request")
    public Integer partSize = 1;

    @Parameter(names = "--retry-failed-upload", description = "How many times should it retry to upload a failed piece before giving up.")
    public Integer retryFailedUpload = 2;

    @Parameter(names = "--concurrent", description = "How many threads to open to use when uploading the data to amazon glacier, the more threads you have the more memory it will eat. The memory requirements will be partSize * concurrent")
    public Integer concurrent = 1;

    @Parameter(hidden = true, names = "--testing", description = "Testing command")
    public Boolean testing;

}

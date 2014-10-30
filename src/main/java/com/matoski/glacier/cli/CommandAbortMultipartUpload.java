package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "abort-multipart-upload", commandDescription = "Aborts a multipart upload")
public class CommandAbortMultipartUpload {

    @Parameter(names = "--vault", description = "The name of the vault from which the multipart upload will be aborted, will be overwriten by --aws-vault if not specified")
    public String vaultName;

    @Parameter(required = true, names = "--id", description = "The multipart id we need to abort")
    public String multipartId;

}

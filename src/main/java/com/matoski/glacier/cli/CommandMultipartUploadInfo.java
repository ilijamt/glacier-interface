package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "multipart-upload-info", commandDescription = "Gets detailed info about a multipart upload")
public class CommandMultipartUploadInfo {

    @Parameter(names = "--vault", description = "The name of the vault from which the multipart upload info will be retrieved, will be overwriten by --aws-vault if not specified")
    public String vaultName;

    @Parameter(required = true, names = "--id", description = "The multipart Id for whom we need to retrieve the details")
    public String multipartId;

}

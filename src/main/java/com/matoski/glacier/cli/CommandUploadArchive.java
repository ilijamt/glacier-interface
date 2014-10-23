package com.matoski.glacier.cli;

import java.util.HashMap;
import java.util.Map;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "upload-archive", commandDescription = "Initiate an upload of archive")
public class CommandUploadArchive {

    @Parameter(names = "--vault", description = "The name of the vault from where the archive will be deleted, will be overwriten by --aws-vault if not specified")
    public String vaultName;

    @Parameter(required = true, names = "--file", description = "The file(s) to be uploaded, you can specifiy this parameter multiple times")
    public Map<String, String> fileName = new HashMap<String, String>();

}

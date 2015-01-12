package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.matoski.glacier.Constants;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "sync", commandDescription = "Sync a folder contents to amazon glacier")
public class CommandSync extends GenericCommand {

  @Parameter(
      names = "--vault",
      description = "The name of the vault to where the archives will be synced, will be overwritten by --aws-vault if not specified")
  public String vaultName;

  @Parameter(required = true, names = "--journal", description = "Journal")
  public String journal;

  @Parameter(
      names = "--metadata",
      description = "Available: mt2, fgv2, if not specified it will be read from the journal, also if a journal is specified, the metadata will be read from the journal and not from the command line")
  public String metadata = Constants.DEFAULT_PARSER_METADATA;

  @Parameter(
      names = "--part-size",
      description = "How big chunks of data to upload to amazon glacier during one request, the part size has to be multiple of 2, like 1MB, 2MB, 4MB, 8MB, ...")
  public Integer partSize = Constants.DEFAULT_PART_SIZE;

  @Parameter(names = "--retry-failed-upload",
      description = "How many times should it retry to upload a failed piece before giving up.")
  public Integer retryFailedUpload = Constants.DEFAULT_RETRY_FAILED_UPLOAD;

  @Parameter(
      names = "--concurrent",
      description = "How many threads to open to use when uploading the data to amazon glacier, the more threads you have the more memory it will eat. The memory requirements will be partSize * concurrent")
  public Integer concurrent = Constants.DEFAULT_CONCURRENT_THREADS;

  @Parameter(
      names = "--replace-modified",
      description = "Replaces the modified file with a new one, and the old one is deleted from glacier and the journal")
  public Boolean uploadReplaceModified = false;

  @Parameter(
      names = "--new",
      description = "Uploads only files that don't exist in the journal, this is the default behaviour")
  public Boolean uploadNew = true;

  @Parameter(names = "--dry-run",
      description = "Do not upload the files, just go through the process")
  public Boolean dryRun = false;

  public CommandSync() {
    super(CliCommands.Sync);
  }

}
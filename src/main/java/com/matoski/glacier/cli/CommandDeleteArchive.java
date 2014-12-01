package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.matoski.glacier.Constants;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "delete-archive", commandDescription = "Initiate a deletion of archive")
public class CommandDeleteArchive extends GenericCommand {

  @Parameter(names = "--vault", description = "The name of the vault from where the archive will be deleted, will be overwritten by --aws-vault if not specified")
  public String vaultName;

  @Parameter(names = "--metadata", description = "Available: mt2, fgv2")
  public String metadata = Constants.DEFAULT_PARSER_METADATA;

  @Parameter(required = true, names = "--journal", description = "Journal")
  public String journal;

  @Parameter(names = "--id", description = "The id of the archive")
  public String id;

  @Parameter(names = "--name", description = "The name of the archive")
  public String name;

  @Parameter(names = "--ignore-journal", description = "Ignore the journal when deleting, you should only use this with ID")
  public Boolean ignoreJournal = false;

  public CommandDeleteArchive() {
    super(CliCommands.DeleteArchive);
  }

}

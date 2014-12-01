package com.matoski.glacier.cli;

import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "list-vaults", commandDescription = "List the available vaults in the system")
public class CommandListVaults extends GenericCommand {

  public CommandListVaults() {
    super(CliCommands.ListVaults);
  }

}

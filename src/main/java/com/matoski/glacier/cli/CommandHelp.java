package com.matoski.glacier.cli;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "help", commandDescription = "Show the help page")
public class CommandHelp extends GenericCommand {

  @Parameter(help = true, required = false,
      description = "The command you want to display help for")
  public List<String> command = new ArrayList<String>();

  public CommandHelp() {
    super(CliCommands.Help);
  }

}

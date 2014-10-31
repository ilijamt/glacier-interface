package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "list-journal", commandDescription = "List the data in the journal")
public class CommandListJournal extends GenericCommand {

    @Parameter(names = "--journal", description = "The journal to read the data from")
    public String journal;

    @Parameter(names = "--full", description = "Display the full data from the journal")
    public Boolean full = false;

    public CommandListJournal() {
	super(CliCommands.ListJournal);
    }

}

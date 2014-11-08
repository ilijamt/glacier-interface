package com.matoski.glacier.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.matoski.glacier.base.GenericCommand;
import com.matoski.glacier.enums.CliCommands;

@Parameters(commandNames = "verify-journal", commandDescription = "Verifies the data in the journal with the files on the disk")
public class CommandVerifyJournal extends GenericCommand {

    @Parameter(required = true, names = "--journal", description = "The journal we use to verify the data")
    public String journal;

    public CommandVerifyJournal() {
	super(CliCommands.VerifyJournal);
    }

}

package com.matoski.glacier.commands;

import java.io.IOException;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import com.matoski.glacier.base.AbstractEmptyCommand;
import com.matoski.glacier.cli.CommandListJournal;
import com.matoski.glacier.pojo.Archive;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.pojo.journal.State;

public class ListJournalCommand extends
	AbstractEmptyCommand<CommandListJournal> {

    private State journal;

    public ListJournalCommand(Config config, CommandListJournal command)
	    throws IOException {
	super(config, command);
	if (null == command.journal) {
	    throw new IllegalArgumentException(
		    "Missing --journal from the command line");
	}
	this.journal = State.load(command.journal);
    }

    @Override
    public void run() {

	System.out.println("START: list-journal\n");
	Archive archive = null;

	System.out.println(String.format("Total items: %s (in journal %s)",
		journal.getArchives().size(), journal.getJournal().size()));
	System.out.println();

	for (Entry<String, Archive> entry : journal.getArchives().entrySet()) {
	    archive = entry.getValue();

	    if (command.full) {
		System.out.println(String.format("%1$20s: %2$s", "ID",
			archive.getId()));
		System.out.println(String.format("%1$20s: %2$s", "Name",
			archive.getName()));
		System.out.println(String.format("%1$20s: %2$s",
			"SHA256 TreeHash", archive.getHash()));
		System.out.println(String.format("%1$20s: %2$s", "State",
			archive.getState()));
		System.out.println(String.format("%1$20s: %2$s bytes", "Size",
			archive.getSize()));
		System.out.println(String.format("%1$20s: %2$s",
			"Modified Date", archive.getModifiedDate()));

		System.out.println();

	    } else {
		System.out.println(String.format("[%1$7s] %2$s",
			FileUtils.byteCountToDisplaySize(archive.getSize()),
			archive.getName()));
	    }

	}

	System.out.println("\nEND: list-journal");
    }

}

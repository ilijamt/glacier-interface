package com.matoski.glacier.commands;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map.Entry;

import com.matoski.glacier.base.AbstractEmptyCommand;
import com.matoski.glacier.cli.CommandListJournal;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.pojo.archive.Archive;
import com.matoski.glacier.pojo.journal.State;
import com.matoski.glacier.util.FileUtils;

/**
 * Lists items in a journal
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 *
 */
public class ListJournalCommand extends AbstractEmptyCommand<CommandListJournal> {

  /**
   * The journal
   */
  private State journal;

  /**
   * Constructor
   * 
   * @param config
   * @param command
   * @throws IOException
   */
  public ListJournalCommand(Config config, CommandListJournal command) throws IOException {
    super(config, command);
    if (null == command.journal) {
      throw new IllegalArgumentException("Missing --journal from the command line");
    }
    this.journal = State.load(command.journal);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {

    System.out.println("START: list-journal\n");
    Archive archive = null;
    String extras = null;

    System.out.println(String.format("Total items: %s (in journal %s)", journal.getArchives()
        .size(), journal.getJournal().size()));
    System.out.println();

    for (Entry<String, Archive> entry : journal.getArchives().entrySet()) {
      archive = entry.getValue();

      File file = archive.getFile(config.getDirectory());

      if (command.full) {
        System.out.println(String.format("%1$20s: %2$s", "ID", archive.getId()));
        System.out.println(String.format("%1$20s: %2$s", "Name", archive.getName()));
        System.out.println(String.format("%1$20s: %2$s", "SHA256 TreeHash", archive.getHash()));
        System.out.println(String.format("%1$20s: %2$s", "State", archive.getState()));
        System.out.println(String.format("%1$20s: %2$s (%3$s bytes)", "Size",
            FileUtils.humanReadableByteCount(archive.getSize()), archive.getSize()));
        System.out.println(String.format("%1$20s: %2$s (%3$s)", "Modified Date",
            new Date(archive.getModifiedDate()), archive.getModifiedDate()));
        if (command.exists) {
          System.out
              .println(String.format("%1$20s: %2$s", "Exists", file.exists() && file.isFile()));
        }

        System.out.println();

      } else {
        if (command.exists) {
          extras = String.format(", file exists: %s", file.exists() && file.isFile());
        } else {
          extras = "";
        }

        System.out.println(String.format("[%1$10s%3$s] %2$s",
            FileUtils.humanReadableByteCount(archive.getSize()), archive.getName(), extras));

      }

    }

    System.out.println("\nEND: list-journal");
  }
}

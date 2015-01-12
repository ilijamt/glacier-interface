package com.matoski.glacier.commands;

import com.matoski.glacier.Constants;
import com.matoski.glacier.base.AbstractEmptyCommand;
import com.matoski.glacier.cli.CommandVersion;
import com.matoski.glacier.pojo.Config;

public class VersionCommand extends AbstractEmptyCommand<CommandVersion> {

  public VersionCommand(Config config, CommandVersion command) {
    super(config, command);
  }

  @Override
  public void run() {
    System.out.println("START: version\n");

    System.out.println(String.format("VERSION: %s", Constants.VERSION));
    
    System.out.println(String.format("DEFAULT_PART_SIZE: %d MB", Constants.DEFAULT_PART_SIZE));
    System.out.println(String.format("DEFAULT_CONCURRENT_THREADS: %d", Constants.DEFAULT_CONCURRENT_THREADS));
    System.out.println(String.format("DEFAULT_HUMAN_READABLE_DIGITS: %s", Constants.DEFAULT_HUMAN_READABLE_DIGITS));
    System.out.println(String.format("WAIT_TIME_THREAD_CHECK: %d", Constants.WAIT_TIME_THREAD_CHECK));
    System.out.println(String.format("WAIT_FETCH_OBJECT_FROM_POOL: %d", Constants.WAIT_FETCH_OBJECT_FROM_POOL));    
    System.out.println(String.format("DEFAULT_RETRY_FAILED_UPLOAD: %d", Constants.DEFAULT_RETRY_FAILED_UPLOAD));
    System.out.println(String.format("DEFAULT_PARSER_METADATA: %s", Constants.DEFAULT_PARSER_METADATA));
    
    System.out.println("\nEND: version");
  }

}

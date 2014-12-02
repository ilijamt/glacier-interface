package com.matoski.glacier.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.glacier.model.InitiateJobResult;
import com.matoski.glacier.base.AbstractCommand;
import com.matoski.glacier.cli.CommandInitDownload;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.VaultNameNotPresentException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.pojo.archive.Archive;
import com.matoski.glacier.pojo.job.DownloadJob;
import com.matoski.glacier.pojo.job.DownloadJobInfo;
import com.matoski.glacier.pojo.journal.State;
import com.matoski.glacier.util.download.AmazonGlacierDownloadUtil;

/**
 * Init Download command
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class InitDownloadCommand extends AbstractCommand<CommandInitDownload> {

  /**
   * List of archive ids to download.
   */
  protected List<String> archiveId = new ArrayList<String>();

  /**
   * Journal.
   */
  private State journal;

  /**
   * Constructor.
   * 
   * @param config
   *          Application config
   * @param command
   *          The command configuration
   * 
   * @throws VaultNameNotPresentException
   *           Vault not present in config
   * @throws RegionNotSupportedException
   *           Region not supported
   */
  public InitDownloadCommand(Config config, CommandInitDownload command)
      throws VaultNameNotPresentException, RegionNotSupportedException {
    super(config, command);
    archiveId = command.id;

    final Boolean validVaultName = null != command.vaultName;
    final Boolean validVaultNameConfig = null != config.getVault();
    final Boolean validId = (command.id.size() > 0);
    final Boolean validName = (command.name.size() > 0);

    try {
      this.journal = State.load(command.journal);
    } catch (IOException e) {
      if (command.ignoreJournal) {
        this.journal = new State();
      } else {
        throw new RuntimeException("Journal doesn't exist");
      }
    }

    if (!validVaultName && !validVaultNameConfig) {
      throw new VaultNameNotPresentException();
    }

    if (validVaultNameConfig) {
      command.vaultName = config.getVault();
    }

    if (command.ignoreJournal && !validId) {
      throw new IllegalArgumentException("ID is required, when --ignore-journal is supplied");
    }

    // no ID and no NAME
    if (!validId && !validName) {
      throw new IllegalArgumentException("ID or NAME are required");
    }

    // ID and NAME
    if (validId && validName) {
      throw new IllegalArgumentException("ID or NAME are required, not both");
    }

    if (command.ignoreJournal && !validId) {
      throw new IllegalArgumentException("ID is required");
    }

    String archiveName = "N/A";

    if (validId && !validName) {
      archiveId = command.id;
      for (String id : command.id) {
        System.out.println(String.format("Queue download job for %s", id));
      }
      System.out.println();
    } else if (!command.ignoreJournal) {
      Archive archive = null;
      for (String item : command.name) {
        archive = this.journal.getByName(item);
        if (null == archive) {
          System.out
              .println(String.format("%s is not present in the journal, skipping ...", item));
        } else {
          archiveName = archive.getName();
          archiveId.add(archive.getId());
          System.out.println(String.format("Queue download job for %s [%s]", archiveName,
              archive.getId()));
        }
      }
      System.out.println();
    }

    // TODO: Verify the parameters if we have supplied wait we need to have
    // the parameters for downloading the job
  }

  @Override
  public void run() {

    System.out.println("START: init-download\n");

    AmazonGlacierDownloadUtil download = new AmazonGlacierDownloadUtil(credentials, client, region);

    DownloadJobInfo jobs = new DownloadJobInfo();
    Archive archive = null;

    jobs.setFile(new File(command.jobFile));
    jobs.setDirectory(config.getDirectory());

    for (String id : archiveId) {

      DownloadJob job = new DownloadJob();

      InitiateJobResult request = download.initiateDownloadRequest(command.vaultName, id);

      job.setArchiveId(id);
      job.setJobId(request.getJobId());
      job.setVaultName(command.vaultName);

      archive = journal.getById(id);
      if (null == archive) {
        job.setName(id);
      } else {
        job.setName(archive.getName());
      }

      jobs.addJob(job);

      try {
        jobs.write();
      } catch (NullPointerException | IOException e) {
        e.printStackTrace();
      }

    }

    if (command.wait) {
      System.out.println(String
          .format("We will wait for the download job to finish, before downloading"));
    }

    System.out.println("\nEND: init-download");
  }
}

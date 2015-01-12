help
==============

You can add multiple command for which it will show help.

```bash
$ gi help list-vaults create-vault
Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /tmp/demo-glacier-interface
Command: Help

List the available vaults in the system
Usage: list-vaults [options]

Creates a new vault on Amazon Glacier
Usage: create-vault [options]
  Options:
        --vault
       The name of the vault to be created, will be overwritten by --aws-vault
       if not specified


Finished
```

Or if you don't supply all the required parameters, it will print out the usage for the command in question.

```bash
$ gi upload-archive
Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Initiate an upload of archive
Usage: upload-archive [options]
  Options:
        --concurrent
       How many threads to open to use when uploading the data to amazon
       glacier, the more threads you have the more memory it will eat. The memory
       requirements will be partSize * concurrent
       Default: 2
  *     --file
       The file(s) to be uploaded, you can specify this parameter multiple times
       Default: []
        --force-upload
       Force upload if it exist in the journal, but it will still keep the old
       archive, you will have to delete the old one on your own
       Default: false
  *     --journal
       Journal
        --metadata
       Available: mt2, fgv2
       Default: mt2
        --part-size
       How big chunks of data to upload to amazon glacier during one request,
       the part size has to be multiple of 2, like 1MB, 2MB, 4MB, 8MB, ...
       Default: 8
        --replaced-modified
       Replaces the modified file with a new one, and the old one is deleted
       from glacier and the journal
       Default: false
        --retry-failed-upload
       How many times should it retry to upload a failed piece before giving up.
       Default: 2
        --vault
       The name of the vault from where the archive will be deleted, will be
       overwritten by --aws-vault if not specified

ERROR: The following options are required:     --file     --journal 
```
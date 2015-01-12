Glacier Interface
=================

A command line tool to interface with glacier.

It's a multithreaded application, that supports multipart uploads to Amazon Glacier servers, you can specify concurrency, to speed up the upload.  

Anyone is welcome to help in development of this tool, see a bug, or a want a new feature, send me a pull request, or create an issue and I will see what I can do.

**Status: BETA**

Intro
-----
Amazon Glacier is an archive/backup service with very low storage price. However with some caveats in usage and archive retrieval prices. [Read more about Amazon Glacier](http://aws.amazon.com/glacier/)

Installation 
------------

* Debian based systems

```bash
echo "deb http://packages.matoski.com debian-gi main" | sudo tee /etc/apt/sources.list.d/debian-gi.list
sudo wget -O /etc/apt/trusted.gpg.d/pm.gpg http://packages.matoski.com/keyring.gpg
sudo apt-get update
sudo apt-get -y install glacier-interface
```

* Source

You can checkout the code from the repository, and build it with gradle.

Usage
-----
glacier-interface &lt;options&gt; &lt;commands&gt; &lt;command-options&gt;

Config file
-----------

The configuration file is a JSON, you can use this to skip setting some of the configuration on the command line.

```json
{
  key: "<AMAZON KEY>",
  secretKey: "<AMAZON SECRET KEY>",
  region: "<REGION>"
}
```

This file is optional, and can be used to put your parameters in a file to simplify usage and shorten the command

Journal
-------

Journal is a file in local filesystem, which contains list of all files, uploaded to Amazon Glacier. Strictly saying, this file contains a list of operations (list of records), performed with Amazon Glacier vault. 

Main operations are 

* CREATE
* DELETE
* DOWNLOAD

All items except **DELETE** are present in the journal for usage, even though they are present in the journal file, they are not available for usage as they have been deleted, and so removed from the active journal.

The reason why it's present in the file is so we can have it as history.

The journal is a file that keeps a list of all available files in a vault, and all the actions on it. The journal file is a JSON file.

```json
{
  "journal": [],
  "date": "Nov 12, 2014 7:16:55 PM",
  "metadata": "MT_AWS_GLACIER_B",
  "name": "Pictures"
}
```

This is an example of how a journal will look like.

If this is your initial upload you can create a new journal using the command to create a journal.

Options
-------

* --aws-key Sets the amazon region, if specified it will override the data loaded from the configuration file
* --aws-region Sets the amazon key, if specified it will override the data loaded from the configuration file
* --aws-secret-key Sets the amazon secret key, if specified it will override the data loaded from the configuration file
* --aws-vault Sets the amazon vault, if specified it will override the data loaded from the configuration file
* --config Location to the configuration file to load
* --create-config Create a config file based on the parameters you have supplied into the application
* --directory The base directory from which we start, if not specified then the directory is set to the current working directory

Commands
--------

* [help](#help)
* [list-vaults](#list-vaults)
* [create-vault](#create-vault)
* [delete-vault](#delete-vault)
* [list-vault-jobs](#list-vault-jobs)
* [vault-job-info](#vault-job-info)
* [inventory-retrieve](#inventory-retrieve)
* [inventory-download](#inventory-download)
* [list-journal](#list-journal)
* [verify-journal](#verify-journal)
* [init-download](#init-download)
* [download-job](#download-job) (TODO)
* [delete-archive](#delete-archive)
* [upload-archive](#upload-archive)
* [list-multipart-uploads](#list-multipart-uploads)
* [multipart-upload-info](#multipart-upload-info)
* [abort-multipart-upload](#abort-multipart-upload)
* [purge-vault](#purge-vault)
* [sync](#sync)

Priorities
-----------
* download-job

TODO
----
* FastGlacier metadata
* download-job
* bash auto-complete

Commands Description
-------------------
### `help`
Shows all the available command in the system, you can take a look at [Help](HELP) for all the available commands

If you want to show the help for a specific command just execute. Also while running a command, if some required parameters are missing, then it will display the help for that command.

Or you can take a look [here](docs/help.md) for various examples.

### `list-vaults`
Lists all available vaults present on Amazon Glacier servers specified by the region.

[Example](docs/list-vaults.md)

### `create-vault` 
Creates a new vault on Amazon Glacier

[Example](docs/create-vault.md)

### `delete-vault`
Deletes a vault on Amazon Glacier, just a not that you cannot delete a non empty vault, you will have to delete all the archives first and then you can delete the vault after 24 hours.

You can use [purge-vault](#purge-vault) to empty the vault from all the archives.

[Example](docs/delete-vault.md)

### `list-vault-jobs`
Gives you a list of all available vault jobs

[Example](docs/list-vault-jobs.md)

### `vault-job-info`
Gives a detailed information about a vault job

[Example](docs/vault-job-info.md)

### `inventory-retrieve`
If you lose your journal you will need to request and **inventory-retrieve** from Glacier and wait for about 4 hours until you can download it.

This will query a request on the amazon glacier servers which gives you a list of all available archives in the system to download with [inventory-download](#inventory-download)

[Example](docs/inventory-retrieve.md)

### `inventory-download`
You can use this to download the inventory after **inventory-retrieve** has been completed. You will also need to specify the metadata used to store the archives, so we can parse it correctly.

[Example](docs/inventory-download.md)

### `list-journal`
It's used to list the files in a journal, it can give you a detailed information for what is in the journal.

[Example](docs/list-journal.md)

### `verify-journal`
Verifies the data in the journal with the files on the disk

[Example](docs/verify-journal.md)

### `init-download`
Creates an init download job, which then can be used to retrieve the files that you requested.

### `download-job`
TODO 

### `delete-archive`
Delete an archive from Glacier, it can be either done by archive ID or by an archive Name, in which case you will need to supply a valid journal 

[Example](docs/delete-archive.md)

### `upload-archive`
Uploads an archive to Glacier server.

* partSize

How big of chunks should be uploaded at a time

* concurrent

You can specify how many threads to open to use when uploading the data to amazon glacier, the more threads you have the more memory it will eat.

[Example](docs/upload-archive.md)

### `list-multipart-uploads`
Lists all the multipart uploads, and they can be canceled.
Useful for cleaning up.

[Example](docs/list-multipart-uploads.md)

### `multipart-upload-info`
Information about the multipart upload

[Example](docs/multipart-upload-info.md)

### `abort-multipart-upload`
Aborts a multipart upload, you need to specify the correct ID to abort

[Example](docs/abort-multipart-upload.md)

### `purge-vault`
Purges the vault of all files present in the journal, it can be used to empty a vault of all archives.

[Example](docs/purge-vault.md)

### `sync`
Synchronizes a directory to Glacier, this can be useful to sync a whole directory and it's contents.

[Example](docs/sync.md)

Minimum Amazon Glacier permissions:
-----------------------------------

Something like this (including permissions to create/delete vaults):

```json
{
  "Statement": [
    {
      "Effect": "Allow",
      "Resource": [
        "arn:aws:glacier:eu-west-1:*:vaults/test1",
        "arn:aws:glacier:us-east-1:*:vaults/test1",
        "arn:aws:glacier:eu-west-1:*:vaults/test2",
        "arn:aws:glacier:eu-west-1:*:vaults/test3"
      ],
      "Action": [
        "glacier:UploadArchive",
        "glacier:InitiateMultipartUpload",
        "glacier:UploadMultipartPart",
        "glacier:UploadPart",
        "glacier:DeleteArchive",
        "glacier:ListParts",
        "glacier:InitiateJob",
        "glacier:ListJobs",
        "glacier:GetJobOutput",
        "glacier:ListMultipartUploads",
        "glacier:CompleteMultipartUpload"
      ]
    },
    {
      "Effect": "Allow",
      "Resource": [
        "arn:aws:glacier:eu-west-1:*",
        "arn:aws:glacier:us-east-1:*"
      ],
      "Action": [
        "glacier:CreateVault",
        "glacier:DeleteVault",
        "glacier:ListVaults"
      ]
    }
  ]
}
```

License
-------

Copyright (C) 2014 Ilija Matoski (user@gmail.com)
 
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)
 
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Contributors
------------
* **Main Developer** Ilija Matoski (ilijamt@gmail.com)

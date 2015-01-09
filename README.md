Glacier Interface
=================

A command line tool to interface with glacier.

It's a multithreaded application, that supports multipart uploads to Amazon Glacier servers, you can specify concurrency, to speed up the upload.  

Anyone is welcome to help in development of this tool, see a bug, or a want a new feature, drop me a pull request, or create an issue and I will see what I can do.

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
Shows all the available command in the system, you can take a look at [Help](HELP)

### `list-vaults`
Lists all available vaults present on Amazon Glacier servers specified by the region.

```bash
$ gi --config config.json list-vaults

Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /home/user/workspace/java/glacier-interface
Command: ListVaults

START: list-vaults

Total available vaults: 1

                 ARN: arn:aws:glacier:eu-west-1:<uid>:vaults/TestDemo
          Vault Name: Test
             Created: 2015-01-09T22:15:57.881Z
      Inventory Size: 0 B (0 bytes)
 Last Inventory Date: 2015-01-09T22:15:57.881Z

END: list-vaults

Finished

```

### `create-vault` 
Creates a new vault on Amazon Glacier

```bash
$ gi --config config.json create-vault --vault uplTestDemo

Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /home/user/workspace/java/glacier-interface
Command: CreateVault

START: create-vault

    Location: glacier.eu-west-1.amazonaws.com/<uid>/vaults/TestDemo
         ARN: arn:aws:glacier:eu-west-1:uid:vaults/TestDemo
  Vault Name: TestDemo
     Created: 2015-01-09T22:15:57.881Z

END: create-vault

Finished

```

### `delete-vault`
Deletes a vault on Amazon Glacier, just a not that you cannot delete a non empty vault, you will have to delete all the archives first and then you can delete the vault after 24 hours.

You can use [purge-vault)(#purge-vault) to empty the vault from all the archives.

```bash
$ gi --config config.json delete-vault --vault TestDemo

Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /home/user/workspace/java/glacier-interface
Command: DeleteVault

START: delete-vault

TestDemo deleted. (Currently Amazon Glacier does not return error if vault does not exists)

END: delete-vault

Finished

```
  
### `list-vault-jobs`
Gives you a list of all available vault jobs

### `vault-job-info`
Gives a detailed information about a vault job

### `inventory-retrieve`
If you lose your journal you will need to request and **inventory-retrieve** from Glacier and wait for about 4 hours until you can download it.

This gives you a list of all available archives in the system

### `inventory-download`
You can use this to download the inventory after **inventory-retrieve** has been completed. You will also need to specify the metadata used to store the archives, so we can parse it correctly.

You can also use this to download the raw data and use it to create a new metadata parser.

### `list-journal`
It's used to list the files in a journal, it can give you a detailed information for what is in the journal.

### `init-download`
Creates an init download job, which then can be used to retrieve the files that you requested.

### `download-job`
TODO 

### `delete-archive`
Delete an archive from Glacier, it can be either done by archive ID or by an archive Name, in which case you will need to supply a valid journal 

### `upload-archive`
Uploads an archive to Glacier server.

* partSize

How big of chunks should be uploaded at a time

* concurrent

You can specify how many threads to open to use when uploading the data to amazon glacier, the more threads you have the more memory it will eat.

Lets create a random file to upload
```bash
$ dd if=/dev/urandom of=data.log bs=53125 count=100
100+0 records in
100+0 records out
5312500 bytes (5.3 MB) copied, 0.468821 s, 11.3 MB/s
```

Now for upload
```bash
$ gi --config config.json upload-archive --concurrent 2 --vault TestDemo --file data.log --journal TestDemo.journal
Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /home/user/workspace/java/glacier-interface
Command: UploadArchive

START: upload-archive

Processing: data.log (size: 5312500)
[#00001/#00006] PIECE_START     | (/home/user/workspace/java/glacier-interface/data.log) Upload started
[#00002/#00006] PIECE_START     | (/home/user/workspace/java/glacier-interface/data.log) Upload started
[#00002/#00006] PIECE_COMPLETE  | (/home/user/workspace/java/glacier-interface/data.log) Uploaded
[#00003/#00006] PIECE_START     | (/home/user/workspace/java/glacier-interface/data.log) Upload started
[#00001/#00006] PIECE_COMPLETE  | (/home/user/workspace/java/glacier-interface/data.log) Uploaded
[#00004/#00006] PIECE_START     | (/home/user/workspace/java/glacier-interface/data.log) Upload started
[#00003/#00006] PIECE_COMPLETE  | (/home/user/workspace/java/glacier-interface/data.log) Uploaded
[#00005/#00006] PIECE_START     | (/home/user/workspace/java/glacier-interface/data.log) Upload started
[#00004/#00006] PIECE_COMPLETE  | (/home/user/workspace/java/glacier-interface/data.log) Uploaded
[#00006/#00006] PIECE_START     | (/home/user/workspace/java/glacier-interface/data.log) Upload started
[#00005/#00006] PIECE_COMPLETE  | (/home/user/workspace/java/glacier-interface/data.log) Uploaded
[#00006/#00006] PIECE_COMPLETE  | (/home/user/workspace/java/glacier-interface/data.log) Uploaded

END: upload-archive

Finished
```

Now let's see for resuming upload
```bash
$ gi --config config.json upload-archive --concurrent 2 --vault TestDemo --file data.log --journal TestDemo.journal
Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /home/ilijamt/workspace/java/glacier-interface
Command: UploadArchive

Creating a new journal: TestDemo.journal
START: upload-archive

Processing: data.log (size: 5312500)
[#00001/#00006] PIECE_START     | (/home/ilijamt/workspace/java/glacier-interface/data.log) Upload started
[#00002/#00006] PIECE_START     | (/home/ilijamt/workspace/java/glacier-interface/data.log) Upload started
[#00002/#00006] PIECE_COMPLETE  | (/home/ilijamt/workspace/java/glacier-interface/data.log) Uploaded
[#00003/#00006] PIECE_START     | (/home/ilijamt/workspace/java/glacier-interface/data.log) Upload started
[#00001/#00006] PIECE_COMPLETE  | (/home/ilijamt/workspace/java/glacier-interface/data.log) Uploaded
[#00004/#00006] PIECE_START     | (/home/ilijamt/workspace/java/glacier-interface/data.log) Upload started
[#00004/#00006] PIECE_COMPLETE  | (/home/ilijamt/workspace/java/glacier-interface/data.log) Uploaded
[#00005/#00006] PIECE_START     | (/home/ilijamt/workspace/java/glacier-interface/data.log) Upload started
^C
$ gi --config config.json upload-archive --concurrent 2 --vault TestDemo --file data.log --journal TestDemo.journal
gi --config config.json upload-archive --concurrent 2 --vault TestDemo --file data.log --journal TestDemo.journal
Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /home/ilijamt/workspace/java/glacier-interface
Command: UploadArchive

Creating a new journal: TestDemo.journal
START: upload-archive

Processing: data.log (size: 5312500)
Upload state found for data.log, loading
Upload already initiated with location: /166534042608/vaults/TestDemo/multipart-uploads/FsBxrBiV2DxKqzC21bBciBi25P2Z9Y3dwmm8w63_rzb9d3X7kMMRLOcXWje77fVzCooIULlCdzlUU9AGXQ8HkRBgHyJG and id: FsBxrBiV2DxKqzC21bBciBi25P2Z9Y3dwmm8w63_rzb9d3X7kMMRLOcXWje77fVzCooIULlCdzlUU9AGXQ8HkRBgHyJG

[#00001/#00006] PIECE_COMPLETE  | (/home/ilijamt/workspace/java/glacier-interface/data.log) Already uploaded
[#00002/#00006] PIECE_COMPLETE  | (/home/ilijamt/workspace/java/glacier-interface/data.log) Already uploaded
[#00003/#00006] PIECE_START     | (/home/ilijamt/workspace/java/glacier-interface/data.log) Upload started
[#00004/#00006] PIECE_COMPLETE  | (/home/ilijamt/workspace/java/glacier-interface/data.log) Already uploaded
[#00005/#00006] PIECE_START     | (/home/ilijamt/workspace/java/glacier-interface/data.log) Upload started
[#00003/#00006] PIECE_COMPLETE  | (/home/ilijamt/workspace/java/glacier-interface/data.log) Uploaded
[#00005/#00006] PIECE_COMPLETE  | (/home/ilijamt/workspace/java/glacier-interface/data.log) Uploaded
[#00006/#00006] PIECE_START     | (/home/ilijamt/workspace/java/glacier-interface/data.log) Upload started
[#00006/#00006] PIECE_COMPLETE  | (/home/ilijamt/workspace/java/glacier-interface/data.log) Uploaded

END: upload-archive

Finished
```

### `list-multipart-uploads`
Lists all the multipart uploads, and they can be canceled.
Useful for cleaning up.

```bash
$ gi --config config.json list-multipart-uploads --vault TestDemo

Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /home/user/workspace/java/glacier-interface
Command: ListMultipartUploads

START: list-multipart-uploads

Cancel all multipart uploads: false
Total available multipart uploads: 5

                  ID: 7Nr1F2JpjlRpMqYQNGbLQCccqEeIP0GaBLV8t5_NlfyZIdcXoCGtLZmfYQN5IoGPFS_4XZge9KHnU04yeJewOGelErq7
                 ARN: arn:aws:glacier:eu-west-1:<uid>:vaults/TestDemo
       Creation date: 2015-01-09T22:28:21.744Z
           Part size: 1048576
         Description: mt2 eyJtdGltZSI6IjAifQ==
                Name: null

                  ID: uaePOwkbQDy4P8TyNRcxnEomOgNATIMiJFr7D7A6_813TbV20qMTYvCOyqwyq8x4-RCyS9-qxMwWdPbUO6PytSzZiuyf
                 ARN: arn:aws:glacier:eu-west-1:<uid>:vaults/TestDemo
       Creation date: 2015-01-09T22:28:44.684Z
           Part size: 1048576
         Description: mt2 eyJtdGltZSI6IjAifQ==
                Name: null

                  ID: 2UVJNnEQFsSx_g4wkCsUOITW-KmSMssOK1IM9K9P-uEgKUzRV4VXv_XKGrYxnKZ9i-3o_goUP0nobN8QE7_4O9pJ0wN4
                 ARN: arn:aws:glacier:eu-west-1:<uid>:vaults/TestDemo
       Creation date: 2015-01-09T22:29:25.080Z
           Part size: 1048576
         Description: mt2 eyJtdGltZSI6IjAifQ==
                Name: null

                  ID: wPrVOkpyB6P3ESSLwCos8TAEfJABCiUSB73YA38q2oQ-nzWNlerHab-bsKZyny021uRlRR6lE6zgbuXNYu_FYAEg1ESK
                 ARN: arn:aws:glacier:eu-west-1:<uid>:vaults/TestDemo
       Creation date: 2015-01-09T22:38:32.800Z
           Part size: 1048576
         Description: mt2 eyJtdGltZSI6IjAifQ==
                Name: null

                  ID: flYAJrHphH1NgM0iYKhCOm_3TYKe1p4vkxecvB1B4pdlQWL_nyMs91CsbQAN_MG_2GYEV3qdL1yytt0OasNTHasJaeh7
                 ARN: arn:aws:glacier:eu-west-1:<uid>:vaults/TestDemo
       Creation date: 2015-01-09T22:43:46.883Z
           Part size: 1048576
         Description: mt2 eyJtdGltZSI6IjAifQ==
                Name: null

END: list-multipart-uploads

Finished

```

If you add **--cancel** to the command it will also cancel all the uploads in the list

### `multipart-upload-info`
Information about the multipart upload

```bash
$ gi --config config.json multipart-upload-info --id wPrVOkpyB6P3ESSLwCos8TAEfJABCiUSB73YA38q2oQ-nzWNlerHab-bsKZyny021uRlRR6lE6zgbuXNYu_FYAEg1ESK --vault TestDemo

Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /home/user/workspace/java/glacier-interface
Command: MultipartUploadInfo

START: multipart-upload-info

                  ID: wPrVOkpyB6P3ESSLwCos8TAEfJABCiUSB73YA38q2oQ-nzWNlerHab-bsKZyny021uRlRR6lE6zgbuXNYu_FYAEg1ESK
                 ARN: arn:aws:glacier:eu-west-1:<uid>:vaults/TestDemo
       Creation date: 2015-01-09T22:38:32.800Z
           Part size: 1048576
         Description: mt2 eyJtdGltZSI6IjAifQ==

Total available parts for the upload: 0

END: multipart-upload-info

Finished
```


### `abort-multipart-upload`
Aborts a multipart upload, you need to specify the correct ID to abort

```bash
$ gi --config config.json abort-multipart-upload --id wPrVOkpyB6P3ESSLwCos8TAEfJABCiUSB73YA38q2oQ-nzWNlerHab-bsKZyny021uRlRR6lE6zgbuXNYu_FYAEg1ESK --vault TestDemo

Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /home/user/workspace/java/glacier-interface
Command: AbortMultipartUpload

START: abort-multipart-upload

Multipart upload canceled: true

END: abort-multipart-upload

Finished
```

### `purge-vault`
Purges the vault of all files present in the journal, it can be used to empty a vault of all archives

### `sync`
Synchronizes a directory to Glacier

Journal
-------

Journal is a file in local filesystem, which contains list of all files, uploaded to Amazon Glacier. Strictly saying, this file contains a list of operations (list of records), performed with Amazon Glacier vault. 

Main operations are 

* CREATE
* DELETE
* DOWNLOAD

All items except **DELETE** are present in the journal for usage, even though they are present in the journal file, they are not available for usage as they have been deleted, and so removed from the active journal.

The reason why it's present in the file is so we can have it as history.

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

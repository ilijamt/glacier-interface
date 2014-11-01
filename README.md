Glacier Interface
=================

A command line tool to interface with glacier.
It's a multithreaded application, that supports multipart uploads to Amazon Glacier servers, you can specify concurrency, to speed up the upload.  

Intro
-----
Amazon Glacier is an archive/backup service with very low storage price. However with some caveats in usage and archive retrieval prices. [Read more about Amazon Glacier](http://aws.amazon.com/glacier/)

Usage
-----
glacier-interface &lt;options&gt; &lt;commands&gt; &lt;command-options&gt;

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
* [init-download](#init-download) (TODO)
* [download-archive](#download-archive) (TODO)
* [delete-archive](#delete-archive)
* [upload-archive](#upload-archive)
* [list-multipart-uploads](#list-multipart-uploads)
* [multipart-upload-info](#multipart-upload-info)
* [abort-multipart-upload](#abort-multipart-upload)
* [purge-vault](#purge-vault)
* [sync](#sync)

Priorities
-----------
* init-download
* download-archive

Commands Description
-------------------
### `help`
Shows all the available command in the system, you can take a look at [Help](HELP)

### `list-vaults`
Lists all available vaults present on Amazon Glacier servers specified by the region.

### `create-vault` 
Creates a new vault on Amazon Glacier

### `delete-vault`
Deletes a vault on Amazon Glacier, just a not that you cannot delete a non empty vault, you will have to delete all the archives first and then you can delete the vault after 24 hours.

You can use [purge-vault)(#purge-vault) to empty the vault from all the archives.
  
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
TODO

### `download-archive`
TODO 

### `delete-archive`
Delete an archive from Glacier, it can be either done by archive ID or by an archive Name, in which case you will need to supply a valid journal 

### `upload-archive`
Uploads an archive to Glacier server 

### `list-multipart-uploads`
Lists all the multipart uploads, and they can be canceled.
Useful for cleaning up.

### `multipart-upload-info`
Information about the multipart upload

### `abort-multipart-upload`
Aborts a multipart upload, you need to specify the correct ID to abort

### `purge-vault`
Purges the vault of all files present in the journal, it can be used to empty a vault of all archives

### `sync`
Synchronizes a directory to Glacier

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

Copyright (C) 2014 Ilija Matoski (ilijamt@gmail.com)
 
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

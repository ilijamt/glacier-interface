Glacier Interface
=================

A command line tool to interface with glacier.

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
* list-vault
* create-vault
* delete-vault
* list-vault-jobs
* vault-job-info
* inventory-retrieve
* inventory-download
* list-journal
* init-download (TODO)
* download-archive (TODO)
* delete-archive
* upload-archive
* list-multipart-uploads
* multipart-upload-info
* abort-multipart-upload
* purge-vault
* sync

Priorities
-----------
* init-download
* download-archive

### help
### Purge vault

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
 
    http://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Contributors
------------
* **Main Developer** Ilija Matoski (ilijamt@gmail.com)

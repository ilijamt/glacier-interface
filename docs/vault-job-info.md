vault-job-info
==============

```bash
$ gi --config config.json vault-job-info --id v3tylJllMtsziPfJ9lmqVOfz0QqSZNYIKHpTtEmwO3kAYFSm56ttsmEOoNdUoqqXlL2xLaHCRkf-_L_JrwfmzQtpYz21 --vault TestDemo
Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /tmp/demo-glacier-interface
Command: VaultJobInfo

START: vault-job-info

                   Action : InventoryRetrieval
               Archive Id : null
    Archive Size In Bytes : null
                Completed : false
           CompletionDate : null
             CreationDate : 2015-01-11T10:57:15.889Z
  Inventory Size In Bytes : null
          Job Description : null
                   Job Id : v3tylJllMtsziPfJ9lmqVOfz0QqSZNYIKHpTtEmwO3kAYFSm56ttsmEOoNdUoqqXlL2xLaHCRkf-_L_JrwfmzQtpYz21
         SHA256 Tree Hash : null
                SNS Topic : null
              Status Code : InProgress
           Status Message : null
                Vault ARN : arn:aws:glacier:eu-west-1:<uid>:vaults/TestDemo


END: vault-job-info

Finished
```

Now after the job has been completed, ~4+ hours later.
```bash
$ gi --config config.json vault-job-info --id v3tylJllMtsziPfJ9lmqVOfz0QqSZNYIKHpTtEmwO3kAYFSm56ttsmEOoNdUoqqXlL2xLaHCRkf-_L_JrwfmzQtpYz21 --vault TestDemo
Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /tmp/demo-glacier-interface
Command: VaultJobInfo

START: vault-job-info

                   Action : InventoryRetrieval
               Archive Id : null
    Archive Size In Bytes : null
                Completed : true
           CompletionDate : 2015-01-11T15:14:18.402Z
             CreationDate : 2015-01-11T10:57:15.889Z
  Inventory Size In Bytes : 1484
          Job Description : null
                   Job Id : v3tylJllMtsziPfJ9lmqVOfz0QqSZNYIKHpTtEmwO3kAYFSm56ttsmEOoNdUoqqXlL2xLaHCRkf-_L_JrwfmzQtpYz21
         SHA256 Tree Hash : null
                SNS Topic : null
              Status Code : Succeeded
           Status Message : Succeeded
                Vault ARN : arn:aws:glacier:eu-west-1:166534042608:vaults/TestDemo


END: vault-job-info

Finished
```
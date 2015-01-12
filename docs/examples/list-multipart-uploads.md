list-multipart-uploads
===========

```bash
$ gi --config config.json list-multipart-uploads --vault TestDemo

Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /tmp/demo-glacier-interface
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

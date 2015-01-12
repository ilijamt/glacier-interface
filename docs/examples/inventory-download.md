inventory-download
===========

You can also use this to download the raw data and use it to create a new metadata parser.

```bash
gi --config glacier-interface/config.json inventory-download --id v3tylJllMtsziPfJ9lmqVOfz0QqSZNYIKHpTtEmwO3kAYFSm56ttsmEOoNdUoqqXlL2xLaHCRkf-_L_JrwfmzQtpYz21 --journal TestDemo.journal --vault TestDemo
Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /tmp/demo-glacier-interface
Command: InventoryDownload

START: inventory-download

Inventory downloaded.

    Job ID: v3tylJllMtsziPfJ9lmqVOfz0QqSZNYIKHpTtEmwO3kAYFSm56ttsmEOoNdUoqqXlL2xLaHCRkf-_L_JrwfmzQtpYz21
     Vault: TestDemo


END: inventory-download

Finished
```

The journal file

```json
{
  "journal": [
    {
      "state": "CREATE",
      "id": "pyMMzrnjcfbHw6mqvtL5RRc1L3qN8PKe_fRWdx777FZ2Mji4rLx9VGn47kFse4lwrkxMeT3kLdUviyWYWcplOLmpqaAzi9dARhgya9YOFDAmBf_RXtUZrGO-IkBle2fVGS2Bm9LaMA",
      "modifiedDate": 0,
      "createdDate": "Jan 10, 2015 12:04:23 AM",
      "size": 5312500,
      "hash": "a5af821f672c2d41dcb51ce333c23de80c38589d26069703576a866854b5ed64"
    },
    {
      "state": "CREATE",
      "id": "pxpmN9Np-L-MRqqNSJlPds1kNBXEz0Cxrl-mNjEDJGscYTDACipFPRwXUsSZ9qBm6yaqA3WcCgfCMhtZoDlijt_loLt5jOqVUdzSe9a6_IXPWxjQRgdOyy8H_7W3HSJngoKJ1WdwUA",
      "modifiedDate": 0,
      "createdDate": "Jan 10, 2015 12:07:27 AM",
      "size": 5312500,
      "hash": "ea10f2ffdd184f6c705e9757a3acc91afbcca02c9f653c9d54ab3f504b101052"
    },
    {
      "state": "CREATE",
      "id": "5fauTXbfV_iAivGTj-q_wfphxStjUbY_4oxGNuCmK8q0qmQvQ1wpUEGOIjfuHo2879v3Lx7ioDR7VaCJkqwG9qJVYzOWhO_onIQAlpQkk3tlVV137dXOo7JG-YvnGegydb1FhGvCqg",
      "modifiedDate": 0,
      "createdDate": "Jan 10, 2015 12:23:26 AM",
      "size": 5312500,
      "hash": "ea10f2ffdd184f6c705e9757a3acc91afbcca02c9f653c9d54ab3f504b101052"
    },
    {
      "state": "CREATE",
      "id": "PjiQEEWljl1kEmAFBn1WTbHZe_D9VVjxpyGc-GJ_CEjfcne7XYzxD427SdG9J6ARSAjqBLJedPRb1Uy7538atXhtcz4RDSt1q-p-kVFdb6cNRzF3XfKAn8Awt45vgNIfLqC3he0qlw",
      "modifiedDate": 0,
      "createdDate": "Jan 10, 2015 12:26:34 AM",
      "size": 5312500,
      "hash": "ea10f2ffdd184f6c705e9757a3acc91afbcca02c9f653c9d54ab3f504b101052"
    }
  ],
  "date": "Jan 10, 2015 12:25:43 PM",
  "metadata": "MT_AWS_GLACIER_B",
  "name": "TestDemo"
}
```

If you wish you can download the journal raw, not in the format that this application uses, by adding the **-raw** switch, this is very useful if you want to create a new metadata based on how the data was encoded in the description value.

```bash
gi --config glacier-interface/config.json inventory-download --id v3tylJllMtsziPfJ9lmqVOfz0QqSZNYIKHpTtEmwO3kAYFSm56ttsmEOoNdUoqqXlL2xLaHCRkf-_L_JrwfmzQtpYz21 --journal TestDemo.journal.raw --vault TestDemo --raw
Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /tmp/demo-glacier-interface
Command: InventoryDownload

START: inventory-download

Inventory downloaded.

    Job ID: v3tylJllMtsziPfJ9lmqVOfz0QqSZNYIKHpTtEmwO3kAYFSm56ttsmEOoNdUoqqXlL2xLaHCRkf-_L_JrwfmzQtpYz21
     Vault: TestDemo


END: inventory-download

Finished
```

Raw data

```json
{
  "VaultARN": "arn:aws:glacier:eu-west-1:166534042608:vaults/TestDemo",
  "InventoryDate": "2015-01-10T11:25:43Z",
  "ArchiveList": [
    {
      "ArchiveId": "pyMMzrnjcfbHw6mqvtL5RRc1L3qN8PKe_fRWdx777FZ2Mji4rLx9VGn47kFse4lwrkxMeT3kLdUviyWYWcplOLmpqaAzi9dARhgya9YOFDAmBf_RXtUZrGO-IkBle2fVGS2Bm9LaMA",
      "ArchiveDescription": "mt2 eyJtdGltZSI6IjAifQ\u003d\u003d",
      "CreationDate": "2015-01-09T23:04:23Z",
      "Size": 5312500,
      "SHA256TreeHash": "a5af821f672c2d41dcb51ce333c23de80c38589d26069703576a866854b5ed64"
    },
    {
      "ArchiveId": "pxpmN9Np-L-MRqqNSJlPds1kNBXEz0Cxrl-mNjEDJGscYTDACipFPRwXUsSZ9qBm6yaqA3WcCgfCMhtZoDlijt_loLt5jOqVUdzSe9a6_IXPWxjQRgdOyy8H_7W3HSJngoKJ1WdwUA",
      "ArchiveDescription": "mt2 eyJtdGltZSI6IjAifQ\u003d\u003d",
      "CreationDate": "2015-01-09T23:07:27Z",
      "Size": 5312500,
      "SHA256TreeHash": "ea10f2ffdd184f6c705e9757a3acc91afbcca02c9f653c9d54ab3f504b101052"
    },
    {
      "ArchiveId": "5fauTXbfV_iAivGTj-q_wfphxStjUbY_4oxGNuCmK8q0qmQvQ1wpUEGOIjfuHo2879v3Lx7ioDR7VaCJkqwG9qJVYzOWhO_onIQAlpQkk3tlVV137dXOo7JG-YvnGegydb1FhGvCqg",
      "ArchiveDescription": "mt2 eyJtdGltZSI6IjAifQ\u003d\u003d",
      "CreationDate": "2015-01-09T23:23:26Z",
      "Size": 5312500,
      "SHA256TreeHash": "ea10f2ffdd184f6c705e9757a3acc91afbcca02c9f653c9d54ab3f504b101052"
    },
    {
      "ArchiveId": "PjiQEEWljl1kEmAFBn1WTbHZe_D9VVjxpyGc-GJ_CEjfcne7XYzxD427SdG9J6ARSAjqBLJedPRb1Uy7538atXhtcz4RDSt1q-p-kVFdb6cNRzF3XfKAn8Awt45vgNIfLqC3he0qlw",
      "ArchiveDescription": "mt2 eyJtdGltZSI6IjAifQ\u003d\u003d",
      "CreationDate": "2015-01-09T23:26:34Z",
      "Size": 5312500,
      "SHA256TreeHash": "ea10f2ffdd184f6c705e9757a3acc91afbcca02c9f653c9d54ab3f504b101052"
    }
  ]
}
```

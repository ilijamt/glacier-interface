purge-vault
===========

```bash
gi --config glacier-interface/config.json purge-vault --journal TestDemo.journal --vault TestDemo
Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /tmp/demo-glacier-interface
Command: PurgeVault

START: purge-vault

DELETED [5fauTXbfV_iAivGTj-q_wfphxStjUbY_4oxGNuCmK8q0qmQvQ1wpUEGOIjfuHo2879v3Lx7ioDR7VaCJkqwG9qJVYzOWhO_onIQAlpQkk3tlVV137dXOo7JG-YvnGegydb1FhGvCqg] data1.log
DELETED [pxpmN9Np-L-MRqqNSJlPds1kNBXEz0Cxrl-mNjEDJGscYTDACipFPRwXUsSZ9qBm6yaqA3WcCgfCMhtZoDlijt_loLt5jOqVUdzSe9a6_IXPWxjQRgdOyy8H_7W3HSJngoKJ1WdwUA] data2.log
DELETED [pyMMzrnjcfbHw6mqvtL5RRc1L3qN8PKe_fRWdx777FZ2Mji4rLx9VGn47kFse4lwrkxMeT3kLdUviyWYWcplOLmpqaAzi9dARhgya9YOFDAmBf_RXtUZrGO-IkBle2fVGS2Bm9LaMA] data3.log
DELETED [PjiQEEWljl1kEmAFBn1WTbHZe_D9VVjxpyGc-GJ_CEjfcne7XYzxD427SdG9J6ARSAjqBLJedPRb1Uy7538atXhtcz4RDSt1q-p-kVFdb6cNRzF3XfKAn8Awt45vgNIfLqC3he0qlw] data4.log

END: purge-vault

Finished
```

And now in the journal, as you can see all the files are deleted

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
    },
    {
      "state": "DELETE",
      "id": "5fauTXbfV_iAivGTj-q_wfphxStjUbY_4oxGNuCmK8q0qmQvQ1wpUEGOIjfuHo2879v3Lx7ioDR7VaCJkqwG9qJVYzOWhO_onIQAlpQkk3tlVV137dXOo7JG-YvnGegydb1FhGvCqg",
      "modifiedDate": 0,
      "createdDate": "Jan 10, 2015 12:23:26 AM",
      "size": 5312500,
      "hash": "ea10f2ffdd184f6c705e9757a3acc91afbcca02c9f653c9d54ab3f504b101052"
    },
    {
      "state": "DELETE",
      "id": "pxpmN9Np-L-MRqqNSJlPds1kNBXEz0Cxrl-mNjEDJGscYTDACipFPRwXUsSZ9qBm6yaqA3WcCgfCMhtZoDlijt_loLt5jOqVUdzSe9a6_IXPWxjQRgdOyy8H_7W3HSJngoKJ1WdwUA",
      "modifiedDate": 0,
      "createdDate": "Jan 10, 2015 12:07:27 AM",
      "size": 5312500,
      "hash": "ea10f2ffdd184f6c705e9757a3acc91afbcca02c9f653c9d54ab3f504b101052"
    },
    {
      "state": "DELETE",
      "id": "pyMMzrnjcfbHw6mqvtL5RRc1L3qN8PKe_fRWdx777FZ2Mji4rLx9VGn47kFse4lwrkxMeT3kLdUviyWYWcplOLmpqaAzi9dARhgya9YOFDAmBf_RXtUZrGO-IkBle2fVGS2Bm9LaMA",
      "modifiedDate": 0,
      "createdDate": "Jan 10, 2015 12:04:23 AM",
      "size": 5312500,
      "hash": "a5af821f672c2d41dcb51ce333c23de80c38589d26069703576a866854b5ed64"
    },
    {
      "state": "DELETE",
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
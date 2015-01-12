upload-archive
===========

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

Current working directory: /tmp/demo-glacier-interface
Command: UploadArchive

START: upload-archive

Processing: data.log (size: 5312500)
[#00001/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
[#00002/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
[#00002/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Uploaded
[#00003/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
[#00001/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Uploaded
[#00004/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
[#00003/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Uploaded
[#00005/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
[#00004/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Uploaded
[#00006/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
[#00005/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Uploaded
[#00006/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Uploaded

END: upload-archive

Finished
```

Now let's see for resuming upload
```bash
$ gi --config config.json upload-archive --concurrent 2 --vault TestDemo --file data.log --journal TestDemo.journal
Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /tmp/demo-glacier-interface
Command: UploadArchive

Creating a new journal: TestDemo.journal
START: upload-archive

Processing: data.log (size: 5312500)
[#00001/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
[#00002/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
[#00002/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Uploaded
[#00003/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
[#00001/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Uploaded
[#00004/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
[#00004/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Uploaded
[#00005/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
^C
$ gi --config config.json upload-archive --concurrent 2 --vault TestDemo --file data.log --journal TestDemo.journal
gi --config config.json upload-archive --concurrent 2 --vault TestDemo --file data.log --journal TestDemo.journal
Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /tmp/demo-glacier-interface
Command: UploadArchive

Creating a new journal: TestDemo.journal
START: upload-archive

Processing: data.log (size: 5312500)
Upload state found for data.log, loading
Upload already initiated with location: /<uid>/vaults/TestDemo/multipart-uploads/FsBxrBiV2DxKqzC21bBciBi25P2Z9Y3dwmm8w63_rzb9d3X7kMMRLOcXWje77fVzCooIULlCdzlUU9AGXQ8HkRBgHyJG and id: FsBxrBiV2DxKqzC21bBciBi25P2Z9Y3dwmm8w63_rzb9d3X7kMMRLOcXWje77fVzCooIULlCdzlUU9AGXQ8HkRBgHyJG

[#00001/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Already uploaded
[#00002/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Already uploaded
[#00003/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
[#00004/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Already uploaded
[#00005/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
[#00003/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Uploaded
[#00005/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Uploaded
[#00006/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
[#00006/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Uploaded

END: upload-archive

Finished
```
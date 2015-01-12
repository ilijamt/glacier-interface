sync
====

Let's create a couple of files that we can sync
```bash
 dd if=/dev/urandom of=data.log bs=53125 count=100
 dd if=/dev/urandom of=data2.log bs=63125 count=100
 dd if=/dev/urandom of=data12.log bs=43125 count=100
 dd if=/dev/urandom of=data15.log bs=93125 count=100
```

Now let's sync everything
```bash
$ gi --config /tmp/config.json sync --journal /tmp/TestDemo.journal --concurrent 2 --replace-modified --vault TestDemo
Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /tmp/demo-glacier-interface
Command: Sync

Creating a new journal: ../TestDemo.journal
START: sync

4 files found
Processing: data15.log (size: 9312500)
[#00001/#00009] PIECE_START     | (/tmp/demo-glacier-interface/data15.log) Upload started
[#00002/#00009] PIECE_START     | (/tmp/demo-glacier-interface/data15.log) Upload started
[#00002/#00009] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data15.log) Uploaded
[#00003/#00009] PIECE_START     | (/tmp/demo-glacier-interface/data15.log) Upload started
[#00001/#00009] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data15.log) Uploaded
[#00004/#00009] PIECE_START     | (/tmp/demo-glacier-interface/data15.log) Upload started
[#00003/#00009] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data15.log) Uploaded
[#00005/#00009] PIECE_START     | (/tmp/demo-glacier-interface/data15.log) Upload started
[#00004/#00009] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data15.log) Uploaded
[#00006/#00009] PIECE_START     | (/tmp/demo-glacier-interface/data15.log) Upload started
[#00005/#00009] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data15.log) Uploaded
[#00007/#00009] PIECE_START     | (/tmp/demo-glacier-interface/data15.log) Upload started
[#00006/#00009] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data15.log) Uploaded
[#00008/#00009] PIECE_START     | (/tmp/demo-glacier-interface/data15.log) Upload started
[#00007/#00009] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data15.log) Uploaded
[#00009/#00009] PIECE_START     | (/tmp/demo-glacier-interface/data15.log) Upload started
[#00008/#00009] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data15.log) Uploaded
[#00009/#00009] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data15.log) Uploaded
Processing: data.log (size: 5312500)
[#00001/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
[#00002/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
[#00001/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Uploaded
[#00003/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
[#00002/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Uploaded
[#00004/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
[#00004/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Uploaded
[#00005/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
[#00003/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Uploaded
[#00006/#00006] PIECE_START     | (/tmp/demo-glacier-interface/data.log) Upload started
[#00006/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Uploaded
[#00005/#00006] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data.log) Uploaded
Processing: data2.log (size: 6312500)
[#00001/#00007] PIECE_START     | (/tmp/demo-glacier-interface/data2.log) Upload started
[#00002/#00007] PIECE_START     | (/tmp/demo-glacier-interface/data2.log) Upload started
[#00001/#00007] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data2.log) Uploaded
[#00003/#00007] PIECE_START     | (/tmp/demo-glacier-interface/data2.log) Upload started
[#00002/#00007] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data2.log) Uploaded
[#00004/#00007] PIECE_START     | (/tmp/demo-glacier-interface/data2.log) Upload started
[#00003/#00007] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data2.log) Uploaded
[#00005/#00007] PIECE_START     | (/tmp/demo-glacier-interface/data2.log) Upload started
[#00004/#00007] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data2.log) Uploaded
[#00006/#00007] PIECE_START     | (/tmp/demo-glacier-interface/data2.log) Upload started
[#00005/#00007] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data2.log) Uploaded
[#00007/#00007] PIECE_START     | (/tmp/demo-glacier-interface/data2.log) Upload started
[#00006/#00007] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data2.log) Uploaded
[#00007/#00007] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data2.log) Uploaded
Processing: data12.log (size: 4312500)
[#00001/#00005] PIECE_START     | (/tmp/demo-glacier-interface/data12.log) Upload started
[#00002/#00005] PIECE_START     | (/tmp/demo-glacier-interface/data12.log) Upload started
[#00001/#00005] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data12.log) Uploaded
[#00003/#00005] PIECE_START     | (/tmp/demo-glacier-interface/data12.log) Upload started
[#00002/#00005] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data12.log) Uploaded
[#00004/#00005] PIECE_START     | (/tmp/demo-glacier-interface/data12.log) Upload started
[#00004/#00005] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data12.log) Uploaded
[#00005/#00005] PIECE_START     | (/tmp/demo-glacier-interface/data12.log) Upload started
[#00003/#00005] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data12.log) Uploaded
[#00005/#00005] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data12.log) Uploaded

END: sync

Finished
```

As we can see everything is uploaded, now let's run it again to see if it's gonna upload again or not.

```bash
$ gi --config /tmp/config.json sync --journal /tmp/TestDemo.journal --concurrent 2 --replace-modified --vault TestDemo
Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /tmp/demo-glacier-interface
Command: Sync

START: sync

4 files found
data15.log is already present in the journal
Verifying ...
data15.log size is VALID
data15.log modified date is VALID
Verifying hash, this may take a while depending on the file size ...
Hash is: VALID

 Skipping upload for data15.log
data.log is already present in the journal
Verifying ...
data.log size is VALID
data.log modified date is VALID
Verifying hash, this may take a while depending on the file size ...
Hash is: VALID

 Skipping upload for data.log
data2.log is already present in the journal
Verifying ...
data2.log size is VALID
data2.log modified date is VALID
Verifying hash, this may take a while depending on the file size ...
Hash is: VALID

 Skipping upload for data2.log
data12.log is already present in the journal
Verifying ...
data12.log size is VALID
data12.log modified date is VALID
Verifying hash, this may take a while depending on the file size ...
Hash is: VALID

 Skipping upload for data12.log

END: sync

Finished
```

Now lets create a new file and modify an old one and run it again.

```bash
dd if=/dev/urandom of=data12.log bs=43125 count=100
dd if=/dev/urandom of=data45.log bs=23125 count=100
``` 

And now running the sync again.

```bash
$ gi --config /tmp/config.json sync --journal /tmp/TestDemo.journal --concurrent 2 --replace-modified --vault TestDemo
Glacier Interface (v0.3.4), Copyright 2014, Ilija Matoski

Current working directory: /tmp/demo-glacier-interface
Command: Sync

START: sync

5 files found
data15.log is already present in the journal
Verifying ...
data15.log size is VALID
data15.log modified date is VALID
Verifying hash, this may take a while depending on the file size ...
Hash is: VALID
Skipping upload for data15.log

data.log is already present in the journal
Verifying ...
data.log size is VALID
data.log modified date is VALID
Verifying hash, this may take a while depending on the file size ...
Hash is: VALID
Skipping upload for data.log

data2.log is already present in the journal
Verifying ...
data2.log size is VALID
data2.log modified date is VALID
Verifying hash, this may take a while depending on the file size ...
Hash is: VALID
Skipping upload for data2.log

data12.log is already present in the journal
Verifying ...
data12.log size is VALID
data12.log modified date is INVALID
Verifying hash, this may take a while depending on the file size ...
Hash is: INVALID
Processing: data12.log (size: 4312500)
[#00001/#00005] PIECE_START     | (/tmp/demo-glacier-interface/data12.log) Upload started
[#00002/#00005] PIECE_START     | (/tmp/demo-glacier-interface/data12.log) Upload started
[#00002/#00005] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data12.log) Uploaded
[#00003/#00005] PIECE_START     | (/tmp/demo-glacier-interface/data12.log) Upload started
[#00001/#00005] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data12.log) Uploaded
[#00004/#00005] PIECE_START     | (/tmp/demo-glacier-interface/data12.log) Upload started
[#00003/#00005] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data12.log) Uploaded
[#00005/#00005] PIECE_START     | (/tmp/demo-glacier-interface/data12.log) Upload started
[#00004/#00005] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data12.log) Uploaded
[#00005/#00005] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data12.log) Uploaded
Cleaning, removing old archive [OBi2CErfrPdnucr1UyLZWic4-QBBdKavbfiau-xW27H2Up2GdesKvX6eNo49TJD9A6iRlX140UQw2uGErIztAqF9aQJxQpIqTLLuQYB5DClxoVOf6ITgKLPxjz1bjyeJqsW6CZUYLQ] data12.log
Processing: data45.log (size: 2312500)
[#00001/#00003] PIECE_START     | (/tmp/demo-glacier-interface/data45.log) Upload started
[#00002/#00003] PIECE_START     | (/tmp/demo-glacier-interface/data45.log) Upload started
[#00001/#00003] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data45.log) Uploaded
[#00003/#00003] PIECE_START     | (/tmp/demo-glacier-interface/data45.log) Upload started
[#00003/#00003] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data45.log) Uploaded
[#00002/#00003] PIECE_COMPLETE  | (/tmp/demo-glacier-interface/data45.log) Uploaded

END: sync

Finished
```

And there we go, the modified file and the new file are uploaded on glacier, and we can run verify-journal if you want to test.

If you take a look at the journal, you will see theres is an entry for the deleted file.

And here is the journal
```json
{
  "journal": [
    {
      "state": "CREATE",
      "id": "yzmJvaqivZN3dPL2UMmmObeVAUgQCcNms-nefTDHDrc0cpuD_dFfbNxPt8j6BZP1QkW1X-T82PSw_X_mg2v0qanNcv5FWeG7h7dPv-3-xtBpTfJ3W1l_TMD6Lmli00Fk9Xyd1zCvVw",
      "name": "data15.log",
      "modifiedDate": 1421070677000,
      "createdDate": "Jan 12, 2015 3:21:12 PM",
      "size": 9312500,
      "hash": "5b038a5e2236cb55918df399aa98daae0b272e29e2753909571b271d186ef7da",
      "uri": "/166534042608/vaults/TestDemo/archives/yzmJvaqivZN3dPL2UMmmObeVAUgQCcNms-nefTDHDrc0cpuD_dFfbNxPt8j6BZP1QkW1X-T82PSw_X_mg2v0qanNcv5FWeG7h7dPv-3-xtBpTfJ3W1l_TMD6Lmli00Fk9Xyd1zCvVw"
    },
    {
      "state": "CREATE",
      "id": "m1MVZ1yzi2mj51kyp99pkhPsmQGSHD_O3586xmj3o3NQH_mwp86bWLCybBlSx_CG_wM8jXgO1Fuwqtg7uz69KSqKwbjV7W3zzm2dWcOFRxFElLRV82wi5rz7XCzkDK6nG_pv2YgZLg",
      "name": "data.log",
      "modifiedDate": 1421070655000,
      "createdDate": "Jan 12, 2015 3:21:19 PM",
      "size": 5312500,
      "hash": "fab0982c625cd1e865d2622b65e5def9ea75b42016ae42a8b76d51f1f905c72e",
      "uri": "/166534042608/vaults/TestDemo/archives/m1MVZ1yzi2mj51kyp99pkhPsmQGSHD_O3586xmj3o3NQH_mwp86bWLCybBlSx_CG_wM8jXgO1Fuwqtg7uz69KSqKwbjV7W3zzm2dWcOFRxFElLRV82wi5rz7XCzkDK6nG_pv2YgZLg"
    },
    {
      "state": "CREATE",
      "id": "1w4mqsjq2m_68ApdI_AqFgMpqxCj93Dw2bf8eM--iCzBKO8Iojx5EuayKy6RaX4ffWlg_5A5KHwH7mXC5U1g0Pux07bT-oZHZE069w9sDwin_mjkP7q6IAaMVmsdsEHeuGHEXFDY9Q",
      "name": "data2.log",
      "modifiedDate": 1421070663000,
      "createdDate": "Jan 12, 2015 3:21:24 PM",
      "size": 6312500,
      "hash": "e4310e8a310221bbb34884c2392ce7a29eeaed35200603bc49f00d209943870a",
      "uri": "/166534042608/vaults/TestDemo/archives/1w4mqsjq2m_68ApdI_AqFgMpqxCj93Dw2bf8eM--iCzBKO8Iojx5EuayKy6RaX4ffWlg_5A5KHwH7mXC5U1g0Pux07bT-oZHZE069w9sDwin_mjkP7q6IAaMVmsdsEHeuGHEXFDY9Q"
    },
    {
      "state": "CREATE",
      "id": "OBi2CErfrPdnucr1UyLZWic4-QBBdKavbfiau-xW27H2Up2GdesKvX6eNo49TJD9A6iRlX140UQw2uGErIztAqF9aQJxQpIqTLLuQYB5DClxoVOf6ITgKLPxjz1bjyeJqsW6CZUYLQ",
      "name": "data12.log",
      "modifiedDate": 1421070670000,
      "createdDate": "Jan 12, 2015 3:21:29 PM",
      "size": 4312500,
      "hash": "b7a0a0f46ff6576ae9eca46540b2cb0192d23f27c96357d09032533f1c7a26ef",
      "uri": "/166534042608/vaults/TestDemo/archives/OBi2CErfrPdnucr1UyLZWic4-QBBdKavbfiau-xW27H2Up2GdesKvX6eNo49TJD9A6iRlX140UQw2uGErIztAqF9aQJxQpIqTLLuQYB5DClxoVOf6ITgKLPxjz1bjyeJqsW6CZUYLQ"
    },
    {
      "state": "DELETE",
      "id": "OBi2CErfrPdnucr1UyLZWic4-QBBdKavbfiau-xW27H2Up2GdesKvX6eNo49TJD9A6iRlX140UQw2uGErIztAqF9aQJxQpIqTLLuQYB5DClxoVOf6ITgKLPxjz1bjyeJqsW6CZUYLQ",
      "name": "data12.log",
      "modifiedDate": 1421070670000,
      "createdDate": "Jan 12, 2015 3:21:29 PM",
      "size": 4312500,
      "hash": "b7a0a0f46ff6576ae9eca46540b2cb0192d23f27c96357d09032533f1c7a26ef",
      "uri": "/166534042608/vaults/TestDemo/archives/OBi2CErfrPdnucr1UyLZWic4-QBBdKavbfiau-xW27H2Up2GdesKvX6eNo49TJD9A6iRlX140UQw2uGErIztAqF9aQJxQpIqTLLuQYB5DClxoVOf6ITgKLPxjz1bjyeJqsW6CZUYLQ"
    },
    {
      "state": "CREATE",
      "id": "xOOE3X6jmAQ_cPHrHFIydk8FUFMVedsA3Wvz6DDlanAIj8m7xRY5mHfRmEd4AW4Wn-0m7Gdd8IFlTfk29ADQCRWw3m04ziFrMeL5M_lBPZHpyiwJMZdpYBCM0LTx7PkCJ-xbNV_qEg",
      "name": "data12.log",
      "modifiedDate": 1421073037000,
      "createdDate": "Jan 12, 2015 3:30:47 PM",
      "size": 4312500,
      "hash": "a79ecb298c145e7283283d7bf7316fb24157d0a14c6e8bc989d78d400af39e91",
      "uri": "/166534042608/vaults/TestDemo/archives/xOOE3X6jmAQ_cPHrHFIydk8FUFMVedsA3Wvz6DDlanAIj8m7xRY5mHfRmEd4AW4Wn-0m7Gdd8IFlTfk29ADQCRWw3m04ziFrMeL5M_lBPZHpyiwJMZdpYBCM0LTx7PkCJ-xbNV_qEg"
    },
    {
      "state": "CREATE",
      "id": "24ad4IV3_IqA8lOB6qL2e7wjt93JhALiBHP25LTK-QUfjkOmqSyTqR0wKy021ZX8rr3rai-2stmtddL1zVolHbunImFrxBvTA1tiTZGd16ZVLQ0J3eSb3LS5hJyuNv0IyvYd2Aj4Vg",
      "name": "data45.log",
      "modifiedDate": 1421073037000,
      "createdDate": "Jan 12, 2015 3:30:52 PM",
      "size": 2312500,
      "hash": "7cfc948786bafdc3f44fd8600c71ff12bb3c0370a05e4a4960054c9f2024abbb",
      "uri": "/166534042608/vaults/TestDemo/archives/24ad4IV3_IqA8lOB6qL2e7wjt93JhALiBHP25LTK-QUfjkOmqSyTqR0wKy021ZX8rr3rai-2stmtddL1zVolHbunImFrxBvTA1tiTZGd16ZVLQ0J3eSb3LS5hJyuNv0IyvYd2Aj4Vg"
    }
  ],
  "date": "Jan 12, 2015 3:21:12 PM",
  "metadata": "MT_AWS_GLACIER_B",
  "name": "TestDemo"
}
```
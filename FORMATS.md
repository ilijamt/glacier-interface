mt-aws-glacier
==============

Archive description is base64 encoded 

```
IDENTIFIER BASE_64_ENCODED_DATA
```

IDENTIFIER
----------
* mt2

BASE_64_ENCODED_DATA
--------------------

A json string that contains the file name and modified time 

```json
{ 
	filename : String
	mtime : ISO8601
}
```


FastGlacier
===========

Archive description is UTF8 encoded

```
<m>
   <v></v>
   <p></p>
   <lm></lm>
</m>
```

* **v** contains the version of the metadata 
* **p** contains the original filename
* **lm** ISO8601 timestamp

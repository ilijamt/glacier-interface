package com.matoski.glacier.pojo;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.matoski.glacier.pojo.archive.Archive;

public class ArchiveTest {

    private Archive archive;

    private String Uri = RandomStringUtils.randomAlphanumeric(41);
    private String Name = RandomStringUtils.randomAlphanumeric(43);
    private String Id = RandomStringUtils.randomAlphanumeric(12);
    private long ModifiedDate = new Date().getTime();
    private Date CreatedDate = new Date();
    private long Size = Long.valueOf(RandomStringUtils.randomNumeric(2));
    private String Hash = RandomStringUtils.randomAlphanumeric(64);

    @Before
    public void setUp() throws Exception {
	archive = new Archive();

	archive.setId(Id);
	archive.setName(Name);
	archive.setModifiedDate(ModifiedDate);
	archive.setCreatedDate(CreatedDate);
	archive.setSize(Size);
	archive.setHash(Hash);
	archive.setUri(Uri);

    }

    public final void testCreatedConversion() {
	String date = "2014-10-12T07:45:10Z";
	archive.setCreatedDate(date);
	assertEquals(archive.getCreatedDate(), ISO8601Utils.parse(date));
    }

    @Test
    public final void testSetGet() {
	assertEquals(archive.getId(), Id);
	assertEquals(archive.getName(), Name);
	assertEquals(archive.getModifiedDate(), ModifiedDate);
	assertEquals(archive.getCreatedDate(), CreatedDate);
	assertEquals(archive.getSize(), Size);
	assertEquals(archive.getHash(), Hash);
	assertEquals(archive.getUri(), Uri);
    }
}

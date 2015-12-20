package com.matoski.glacier.pojo;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import com.matoski.glacier.util.Parse;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.util.ISO8601Utils;
import com.matoski.glacier.pojo.archive.Archive;

public class ArchiveTest {

  private Archive archive;

  private String uri = RandomStringUtils.randomAlphanumeric(41);
  private String name = RandomStringUtils.randomAlphanumeric(43);
  private String id = RandomStringUtils.randomAlphanumeric(12);
  private long modifiedDate = new Date().getTime();
  private Date createdDate = new Date();
  private long size = Long.valueOf(RandomStringUtils.randomNumeric(2));
  private String hash = RandomStringUtils.randomAlphanumeric(64);

  @Before
  public void setUp() throws Exception {
    archive = new Archive();

    archive.setId(id);
    archive.setName(name);
    archive.setModifiedDate(modifiedDate);
    archive.setCreatedDate(createdDate);
    archive.setSize(size);
    archive.setHash(hash);
    archive.setUri(uri);

  }

  /**
   * Test.
   */
  public final void testCreatedConversion() {
    String date = "2014-10-12T07:45:10Z";
    archive.setCreatedDate(date);
    assertEquals(archive.getCreatedDate(), Parse.ISO8601StringDateParse(date));
  }

  @Test
  public final void testSetGet() {
    assertEquals(archive.getId(), id);
    assertEquals(archive.getName(), name);
    assertEquals(archive.getModifiedDate(), modifiedDate);
    assertEquals(archive.getCreatedDate(), createdDate);
    assertEquals(archive.getSize(), size);
    assertEquals(archive.getHash(), hash);
    assertEquals(archive.getUri(), uri);
  }
}

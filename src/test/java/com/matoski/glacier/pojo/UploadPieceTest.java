package com.matoski.glacier.pojo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.matoski.glacier.enums.UploadMultipartStatus;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UploadPieceTest {

    private UploadPiece piece;

    private String CalculatedChecksum;
    private String UploadedChecksum;
    private String Id;
    private Integer Part;
    private UploadMultipartStatus Status;

    @Before
    public void setUp() throws Exception {

	this.piece = new UploadPiece();
	this.Status = UploadMultipartStatus.PIECE_COMPLETE;
	this.Part = Integer.valueOf(RandomStringUtils.randomNumeric(3));
	this.Id = RandomStringUtils.randomAlphanumeric(12);
	this.UploadedChecksum = RandomStringUtils.randomAlphanumeric(64);
	this.CalculatedChecksum = RandomStringUtils.randomAlphanumeric(64);

    }

    @Test
    public final void test01_CalculatedChecksum() {
	this.piece.setCalculatedChecksum(CalculatedChecksum);
	assertSame("set/get CalculatedChecksum", this.piece.getCalculatedChecksum(), CalculatedChecksum);
    }

    @Test
    public final void test02_Id() {
	this.piece.setId(Id);
	assertSame("set/get Id", this.piece.getId(), Id);
    }

    @Test
    public final void test03_Part() {
	this.piece.setPart(Part);
	assertEquals("set/get Part", this.piece.getPart(), Part.intValue());
    }

    @Test
    public final void test04_Status() {
	this.piece.setStatus(Status);
	assertSame("set/get Status", this.piece.getStatus(), Status);
    }

    @Test
    public final void test05_UploadedChecksum() {
	this.piece.setUploadedChecksum(UploadedChecksum);
	assertSame("set/get UploadedChecksum", this.piece.getUploadedChecksum(), UploadedChecksum);
    }

    @Test
    public final void test06() {
	this.piece.setStatus(UploadMultipartStatus.PIECE_COMPLETE);
	assertTrue(this.piece.isFinished());
	this.piece.setStatus(UploadMultipartStatus.COMPLETE);
	assertFalse(this.piece.isFinished());
    }

}

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

import com.matoski.glacier.enums.MultipartPieceStatus;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PieceTest {

  private Piece piece;

  private String calculatedChecksum;
  private String uploadedChecksum;
  private String id;
  private Integer part;
  private MultipartPieceStatus status;

  @Before
  public void setUp() throws Exception {

    this.piece = new Piece();
    this.status = MultipartPieceStatus.PIECE_COMPLETE;
    this.part = Integer.valueOf(RandomStringUtils.randomNumeric(3));
    this.id = RandomStringUtils.randomAlphanumeric(12);
    this.uploadedChecksum = RandomStringUtils.randomAlphanumeric(64);
    this.calculatedChecksum = RandomStringUtils.randomAlphanumeric(64);

  }

  @Test
  public final void test01_CalculatedChecksum() {
    this.piece.setCalculatedChecksum(calculatedChecksum);
    assertSame("set/get CalculatedChecksum", this.piece.getCalculatedChecksum(),
        calculatedChecksum);
  }

  @Test
  public final void test02_Id() {
    this.piece.setId(id);
    assertSame("set/get Id", this.piece.getId(), id);
  }

  @Test
  public final void test03_Part() {
    this.piece.setPart(part);
    assertEquals("set/get Part", this.piece.getPart(), part.intValue());
  }

  @Test
  public final void test04_Status() {
    this.piece.setStatus(status);
    assertSame("set/get Status", this.piece.getStatus(), status);
  }

  @Test
  public final void test05_UploadedChecksum() {
    this.piece.setUploadedChecksum(uploadedChecksum);
    assertSame("set/get UploadedChecksum", this.piece.getUploadedChecksum(), uploadedChecksum);
  }

  @Test
  public final void test06() {
    this.piece.setStatus(MultipartPieceStatus.PIECE_COMPLETE);
    assertTrue(this.piece.isFinished());
    this.piece.setStatus(MultipartPieceStatus.PIECE_CHECKSUM_MISMATCH);
    assertFalse(this.piece.isFinished());
    this.piece.setStatus(MultipartPieceStatus.PIECE_ERROR);
    assertFalse(this.piece.isFinished());
    this.piece.setStatus(MultipartPieceStatus.PIECE_INVALID_PART);
    assertFalse(this.piece.isFinished());
    this.piece.setStatus(MultipartPieceStatus.PIECE_START);
    assertFalse(this.piece.isFinished());
  }

}

package com.matoski.glacier.pojo;

import com.matoski.glacier.enums.MultipartStatus;

public class Piece {

  /**
   * The calculated checksum
   */
  private String calculatedChecksum;

  /**
   * The ID of the piece
   */
  private String id;

  /**
   * The part of the piece
   */
  private int part;

  /**
   * The status of the piece
   */
  private MultipartStatus status;

  /**
   * The uploaded checksum of the piece
   */
  private String uploadedChecksum;

  /**
   * @return
   */
  public String getCalculatedChecksum() {
    return calculatedChecksum;
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @return
   */
  public int getPart() {
    return part;
  }

  /**
   * @return
   */
  public MultipartStatus getStatus() {
    return status;
  }

  /**
   * @return
   */
  public String getUploadedChecksum() {
    return uploadedChecksum;
  }

  /**
   * Is the piece completed?
   * 
   * Compares the {@link #status} to {@link MultipartStatus#PIECE_COMPLETE}
   * 
   * @return
   */
  public boolean isFinished() {
    return this.status == MultipartStatus.PIECE_COMPLETE;
  }

  /**
   * @param calculatedChecksum
   */
  public Piece setCalculatedChecksum(String calculatedChecksum) {
    this.calculatedChecksum = calculatedChecksum;
    return this;
  }

  /**
   * @param id
   *          the id to set
   */
  public Piece setId(String id) {
    this.id = id;
    return this;
  }

  /**
   * @param part
   */
  public Piece setPart(int part) {
    this.part = part;
    return this;
  }

  /**
   * @param status
   */
  public Piece setStatus(MultipartStatus status) {
    this.status = status;
    return this;
  }

  /**
   * @param uploadedChecksum
   */
  public Piece setUploadedChecksum(String uploadedChecksum) {
    this.uploadedChecksum = uploadedChecksum;
    return this;
  }

}

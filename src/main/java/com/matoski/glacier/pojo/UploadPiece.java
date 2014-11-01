package com.matoski.glacier.pojo;

import com.matoski.glacier.enums.MultipartStatus;

/**
 * @author ilijamt
 *
 */
public class UploadPiece {

    /**
     * 
     */
    private String calculatedChecksum;

    /**
     * 
     */
    private String id;

    /**
     * 
     */
    private int part;

    /**
     * 
     */
    private MultipartStatus status;

    /**
     * 
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
    public UploadPiece setCalculatedChecksum(String calculatedChecksum) {
	this.calculatedChecksum = calculatedChecksum;
	return this;
    }

    /**
     * @param id
     *            the id to set
     */
    public UploadPiece setId(String id) {
	this.id = id;
	return this;
    }

    /**
     * @param part
     */
    public UploadPiece setPart(int part) {
	this.part = part;
	return this;
    }

    /**
     * @param status
     */
    public UploadPiece setStatus(MultipartStatus status) {
	this.status = status;
	return this;
    }

    /**
     * @param uploadedChecksum
     */
    public UploadPiece setUploadedChecksum(String uploadedChecksum) {
	this.uploadedChecksum = uploadedChecksum;
	return this;
    }

}

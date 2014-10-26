package com.matoski.glacier.pojo;

import com.matoski.glacier.enums.UploadMultipartStatus;

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
    private UploadMultipartStatus status;

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
    public UploadMultipartStatus getStatus() {
	return status;
    }

    /**
     * @return
     */
    public String getUploadedChecksum() {
	return uploadedChecksum;
    }

    /**
     * @param calculatedChecksum
     */
    public void setCalculatedChecksum(String calculatedChecksum) {
	this.calculatedChecksum = calculatedChecksum;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * @param part
     */
    public void setPart(int part) {
	this.part = part;
    }

    /**
     * @param status
     */
    public void setStatus(UploadMultipartStatus status) {
	this.status = status;
    }

    /**
     * @param uploadedChecksum
     */
    public void setUploadedChecksum(String uploadedChecksum) {
	this.uploadedChecksum = uploadedChecksum;
    }

}

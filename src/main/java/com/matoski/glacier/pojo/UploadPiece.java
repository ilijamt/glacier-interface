package com.matoski.glacier.pojo;

import com.matoski.glacier.enums.UploadMultipartStatus;

public class UploadPiece {

    private UploadMultipartStatus status;

    private String calculatedChecksum;

    private String uploadedChecksum;

    public String getCalculatedChecksum() {
	return calculatedChecksum;
    }

    public void setCalculatedChecksum(String calculatedChecksum) {
	this.calculatedChecksum = calculatedChecksum;
    }

    public String getUploadedChecksum() {
	return uploadedChecksum;
    }

    public void setUploadedChecksum(String uploadedChecksum) {
	this.uploadedChecksum = uploadedChecksum;
    }

    private int part;

    public UploadMultipartStatus getStatus() {
	return status;
    }

    public void setStatus(UploadMultipartStatus status) {
	this.status = status;
    }

    public int getPart() {
	return part;
    }

    public void setPart(int part) {
	this.part = part;
    }

}

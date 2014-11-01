package com.matoski.glacier.pojo;

import com.matoski.glacier.enums.MultipartStatus;

/**
 * @author ilijamt
 *
 */
public class DownloadPiece {

    /**
     * 
     */
    private String checksum;

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
     * @return
     */
    public String getCalculatedChecksum() {
	return checksum;
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
     * @param checksum
     */
    public DownloadPiece setChecksum(String checksum) {
	this.checksum = checksum;
	return this;
    }

    /**
     * @param id
     *            the id to set
     */
    public DownloadPiece setId(String id) {
	this.id = id;
	return this;
    }

    /**
     * @param part
     */
    public DownloadPiece setPart(int part) {
	this.part = part;
	return this;
    }

    /**
     * @param status
     */
    public DownloadPiece setStatus(MultipartStatus status) {
	this.status = status;
	return this;
    }

}

package com.matoski.glacier.pojo;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.amazonaws.services.glacier.TreeHashGenerator;
import com.amazonaws.util.BinaryUtils;
import com.matoski.glacier.enums.MultipartStatus;

/**
 * A state file, used to store the state of the uploading file, we use this to
 * store all the information necessary to continue uploading the file
 * 
 * @author ilijamt
 *
 */
public class MultipartDownloadStatus extends AbstractWritablePojo<MultipartDownloadStatus> {

    /**
     * The checksums
     */
    private List<byte[]> checksums = new LinkedList<byte[]>();

    /**
     * The final checksum
     */
    private String finalChecksum;

    /**
     * The original location for the upload
     */
    private String location;

    /**
     * Is the transfer initiated
     */
    private boolean initiated = false;

    /**
     * The generic status
     */
    MultipartStatus status = MultipartStatus.NOP;

    /**
     * The ID of the upload process
     */
    private String id;

    /**
     * The total parts available
     */
    private int parts = 0;

    /**
     * What was the part size when we
     */
    private int partSize;

    /**
     * The pieces
     */
    private TreeMap<Integer, DownloadPiece> pieces;

    /**
     * When did this upload start?
     */
    private Date started;

    /**
     * When was the last time this was updated
     */
    private Date lastUpdate;

    /**
     * Constructor
     */
    public MultipartDownloadStatus() {
	setDirty();
	this.pieces = new TreeMap<Integer, DownloadPiece>();
    }

    /**
     * Add a piece that has been processed
     * 
     * @param piece
     * @throws IOException
     */
    public void addPiece(DownloadPiece piece) throws IOException {
	setDirty();
	if (!this.pieces.containsKey(piece.getPart())) {
	    this.pieces.put(piece.getPart(), piece);
	    this.lastUpdate = new Date();
	    update();
	    this.write();
	}
    }

    /**
     * Add a piece that has been processed
     * 
     * @param part
     * @param piece
     * @throws IOException
     * @throws NullPointerException
     */
    public void addPiece(int part, DownloadPiece piece) throws NullPointerException, IOException {
	setDirty();
	if (!this.pieces.containsKey(part)) {
	    this.pieces.put(part, piece);
	    this.lastUpdate = new Date();
	    this.write();
	    update();
	}
    }

    /**
     * Do we actually have the {@link DownloadPiece} already in {@link #pieces}
     * 
     * @param piece
     * @return
     */
    public Boolean exists(DownloadPiece piece) {
	return this.pieces.containsValue(piece);
    }

    /**
     * Get the checksums list
     * 
     * @return
     */
    public List<byte[]> getChecksums() {
	return this.checksums;
    }

    /**
     * Get the calculated checksum
     * 
     * @return
     */
    public String getFinalChecksum() {
	return this.finalChecksum;
    }

    /**
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * @return the lastUpdate
     */
    public Date getLastUpdate() {
	return lastUpdate;
    }

    /**
     * @return the location
     */
    public String getLocation() {
	return location;
    }

    /**
     * @return the parts
     */
    public int getParts() {
	return parts;
    }

    /**
     * @return the partSize
     */
    public int getPartSize() {
	return partSize;
    }

    public DownloadPiece getPiece(int part) {
	return this.pieces.get(part);
    }

    /**
     * @return the pieces
     */
    public TreeMap<Integer, DownloadPiece> getPieces() {
	return pieces;
    }

    /**
     * @return the started
     */
    public Date getStarted() {
	return started;
    }

    /**
     * @return the status
     */
    public MultipartStatus getStatus() {
	return status;
    }

    /**
     * Is everything finished?
     * 
     * It will iterate over {@link MultipartDownloadStatus#pieces} and compare
     * the status with {@link MultipartStatus#PIECE_COMPLETE}
     * 
     * If the state is finished, {@link MultipartDownloadStatus#remove()} is
     * called.
     * 
     * @return
     */
    public Boolean isFinished() {

	if (parts < this.pieces.size()) {
	    // we have less elements in the map than there should be
	    // parts, so it's not finished
	    return false;
	}

	Boolean valid = parts == this.pieces.size();

	// go over the elements, and compare if all the pieces are complete
	for (Entry<Integer, DownloadPiece> piece : this.pieces.entrySet()) {
	    valid &= (piece.getValue().getStatus() == MultipartStatus.PIECE_COMPLETE);
	}

	if (valid) {
	    this.remove();
	}

	this.update();

	return valid;
    }

    /**
     * @return the initiated
     */
    public boolean isInitiated() {
	return initiated;
    }

    /**
     * Is the piece completed?
     * 
     * Compares the {@link DownloadPiece#getStatus()} to
     * {@link MultipartStatus#PIECE_COMPLETE}
     * 
     * @param piece
     * @return
     */
    public boolean isPieceCompleted(int piece) {
	return this.pieces.containsKey(piece) && this.pieces.get(piece).isFinished();
    }

    /**
     * Remove the state file
     * 
     * @return
     */
    public Boolean remove() {

	File file = this.getFile();

	if (null == file) {
	    return false;
	}

	if (!file.exists() || !file.isFile()) {
	    return false;
	}

	return file.delete();
    }

    /**
     * @param checksums
     *            the checksums to set
     */
    public void setChecksums(List<byte[]> checksums) {
	this.checksums = checksums;
    }

    /**
     * @param finalChecksum
     *            the finalChecksum to set
     */
    public void setFinalChecksum(String finalChecksum) {
	this.finalChecksum = finalChecksum;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
	setDirty();
	this.id = id;
    }

    /**
     * @param initiated
     *            the initiated to set
     */
    public void setInitiated(boolean initiated) {
	setDirty();
	this.initiated = initiated;
    }

    /**
     * @param lastUpdate
     *            the lastUpdate to set
     */
    public void setLastUpdate(Date lastUpdate) {
	this.lastUpdate = lastUpdate;
    }

    /**
     * @param location
     *            the location to set
     */
    public void setLocation(String location) {
	setDirty();
	this.location = location;
    }

    /**
     * @param parts
     *            the parts to set
     */
    public void setParts(int parts) {
	setDirty();
	this.parts = parts;
    }

    /**
     * @param partSize
     *            the partSize to set
     */
    public void setPartSize(int partSize) {
	setDirty();
	this.partSize = partSize;
    }

    /**
     * @param pieces
     *            the pieces to set
     */
    public void setPieces(TreeMap<Integer, DownloadPiece> pieces) {
	setDirty();
	this.pieces = pieces;
    }

    /**
     * @param started
     *            the started to set
     */
    public void setStarted(Date started) {
	setDirty();
	this.started = started;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(MultipartStatus status) {
	setDirty();
	this.status = status;
    }

    /**
     * Updates the checksum
     */
    public void update() {
	this.checksums.clear();
	for (Entry<Integer, DownloadPiece> entry : this.pieces.entrySet()) {
	    this.checksums.add(BinaryUtils.fromHex(entry.getValue().getCalculatedChecksum()));
	}

	this.finalChecksum = TreeHashGenerator.calculateTreeHash(checksums);
    }

}

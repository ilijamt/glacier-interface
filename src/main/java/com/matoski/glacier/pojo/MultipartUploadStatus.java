package com.matoski.glacier.pojo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.amazonaws.services.glacier.TreeHashGenerator;
import com.amazonaws.util.BinaryUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matoski.glacier.enums.UploadMultipartStatus;

/**
 * A state file, used to store the state of the uploading file, we use this to
 * store all the information necessary to continue uploading the file
 * 
 * @author ilijamt
 *
 */
public class MultipartUploadStatus {

    /**
     * Create a file based on the file, we use this to keep the state upload
     * 
     * @param file
     * @return
     */
    public static File generateFile(File file) {

	File status = new File(file.getAbsolutePath() + ".state");

	return status;

    };

    /**
     * Do we have an upload status or not ?
     * 
     * @param file
     * @return
     */
    public static Boolean has(File file) {
	file = generateFile(file);

	return file.exists() && file.isFile();
    }

    /**
     * Load the status from file
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static MultipartUploadStatus load(File file) throws IOException {

	file = generateFile(file);

	if (!file.exists()) {
	    // no journal, it's an empty one so we just return an empty Journal
	    return new MultipartUploadStatus();
	}

	String json = new String(Files.readAllBytes(file.toPath()),
		StandardCharsets.UTF_8);

	MultipartUploadStatus status = new Gson().fromJson(json,
		MultipartUploadStatus.class);
	status.setFile(file, true);
	return status;

    }

    /**
     * Write the status to file
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static MultipartUploadStatus load(String file) throws IOException {
	return load(new File(Config.getInstance().getDirectory(), file));
    }

    /**
     * The checksums
     */
    private List<byte[]> checksums = new LinkedList<byte[]>();

    /**
     * The final checksum
     */
    private String finalChecksum;

    /**
     * A dirty flag, used to know if we should write it or not?
     */
    private transient boolean dirty = false;

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
    UploadMultipartStatus status = UploadMultipartStatus.NOP;

    /**
     * The file for the upload state
     */
    private transient File file;

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
    private TreeMap<Integer, UploadPiece> pieces;

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
    public MultipartUploadStatus() {
	dirty = true;
	this.pieces = new TreeMap<Integer, UploadPiece>();
    }

    /**
     * Add a piece that has been processed
     * 
     * @param part
     * @param piece
     * @throws IOException
     * @throws NullPointerException
     */
    public void addPiece(int part, UploadPiece piece)
	    throws NullPointerException, IOException {
	dirty = true;
	if (!this.pieces.containsKey(part)) {
	    this.pieces.put(part, piece);
	    this.lastUpdate = new Date();
	    this.write();
	    update();
	}
    }

    /**
     * Add a piece that has been processed
     * 
     * @param piece
     * @throws IOException
     */
    public void addPiece(UploadPiece piece) throws IOException {
	dirty = true;
	if (!this.pieces.containsKey(piece.getPart())) {
	    this.pieces.put(piece.getPart(), piece);
	    this.lastUpdate = new Date();
	    update();
	    this.write();
	}
    }

    /**
     * Do we actually have the {@link UploadPiece} already in {@link #pieces}
     * 
     * @param piece
     * @return
     */
    public Boolean exists(UploadPiece piece) {
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
     * Get the file
     * 
     * @return
     */
    public File getFile() {
	return file;
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

    public UploadPiece getPiece(int part) {
	return this.pieces.get(part);
    }

    /**
     * @return the pieces
     */
    public TreeMap<Integer, UploadPiece> getPieces() {
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
    public UploadMultipartStatus getStatus() {
	return status;
    }

    /**
     * @return the dirty
     */
    public boolean isDirty() {
	return dirty;
    }

    /**
     * Is everything finished?
     * 
     * It will iterate over {@link MultipartUploadStatus#pieces} and compare the
     * status with {@link UploadMultipartStatus#PIECE_COMPLETE}
     * 
     * If the state is finished, {@link MultipartUploadStatus#remove()} is
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
	for (Entry<Integer, UploadPiece> piece : this.pieces.entrySet()) {
	    valid &= (piece.getValue().getStatus() == UploadMultipartStatus.PIECE_COMPLETE);
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
     * Compares the {@link UploadPiece#getStatus()} to
     * {@link UploadMultipartStatus#PIECE_COMPLETE}
     * 
     * @param piece
     * @return
     */
    public boolean isPieceCompleted(int piece) {
	return this.pieces.containsKey(piece)
		&& this.pieces.get(piece).isFinished();
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
     * @param dirty
     *            the dirty to set
     */
    public void setDirty(boolean dirty) {
	this.dirty = dirty;
    }

    /**
     * Set the file
     * 
     * @param file
     */
    public void setFile(File file) {
	setFile(file, false);
    }

    public void setFile(File file, Boolean doNotGenerate) {
	this.file = doNotGenerate ? file : generateFile(file);
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
	dirty = true;
	this.id = id;
    }

    /**
     * @param initiated
     *            the initiated to set
     */
    public void setInitiated(boolean initiated) {
	dirty = true;
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
	dirty = true;
	this.location = location;
    }

    /**
     * @param parts
     *            the parts to set
     */
    public void setParts(int parts) {
	dirty = true;
	this.parts = parts;
    }

    /**
     * @param partSize
     *            the partSize to set
     */
    public void setPartSize(int partSize) {
	dirty = true;
	this.partSize = partSize;
    }

    /**
     * @param pieces
     *            the pieces to set
     */
    public void setPieces(TreeMap<Integer, UploadPiece> pieces) {
	dirty = true;
	this.pieces = pieces;
    }

    /**
     * @param started
     *            the started to set
     */
    public void setStarted(Date started) {
	dirty = true;
	this.started = started;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(UploadMultipartStatus status) {
	dirty = true;
	this.status = status;
    }

    /**
     * Updates the checksum
     */
    public void update() {
	this.checksums.clear();
	for (Entry<Integer, UploadPiece> entry : this.pieces.entrySet()) {
	    this.checksums.add(BinaryUtils.fromHex(entry.getValue()
		    .getCalculatedChecksum()));
	}

	this.finalChecksum = TreeHashGenerator.calculateTreeHash(checksums);
    }

    /**
     * Write the status to file
     * 
     * @return
     * @throws IOException
     * @throws NullPointerException
     */
    public boolean write() throws NullPointerException, IOException {
	return write(getFile());
    }

    /**
     * Write the status to file
     * 
     * @param file
     * @return
     * @throws IOException
     * @throws NullPointerException
     */
    protected boolean write(File file) throws NullPointerException, IOException {

	if (!dirty) {
	    // no need to write the file as the data has not been updated
	    return true;
	}

	if (null == file) {
	    throw new NullPointerException();
	}

	if (!file.exists()) {
	    file.createNewFile();
	}

	FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
	BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	bufferedWriter.write(new GsonBuilder().setPrettyPrinting().create()
		.toJson(this));

	bufferedWriter.close();
	fileWriter.close();

	dirty = false;

	return true;
    }

}

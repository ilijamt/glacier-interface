package com.matoski.glacier.pojo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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

    }

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

	return new Gson().fromJson(json, MultipartUploadStatus.class);

    }

    /**
     * Write the status to file
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static MultipartUploadStatus load(String file) throws IOException {
	return load(new File(file));
    }

    /**
     * The file for the upload state
     */
    private transient File file;

    /**
     * The ID of the upload process
     */
    private String id;;

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
    private Map<Integer, UploadPiece> pieces;

    /**
     * When did this upload start?
     */
    private Date started = new Date();

    /**
     * Constructor
     */
    public MultipartUploadStatus() {
	this.pieces = new HashMap<Integer, UploadPiece>();
    }

    /**
     * Constructor
     * 
     * @param total
     */
    public MultipartUploadStatus(int total) {
	this.pieces = new HashMap<Integer, UploadPiece>(total);
	for (int i = 0; i < total; i++) {
	    this.pieces.put(i, null);
	}
    }

    /**
     * Add a piece that has been processed
     * 
     * @param part
     * @param piece
     */
    public void addPiece(int part, UploadPiece piece) {
	this.pieces.put(part, piece);
    }

    /**
     * Add a piece that has been processed
     * 
     * @param piece
     */
    public void addPiece(UploadPiece piece) {
	this.pieces.put(piece.getPart(), piece);
	this.write();
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
     * @return the id
     */
    public String getId() {
	return id;
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

    /**
     * @return the pieces
     */
    public Map<Integer, UploadPiece> getPieces() {
	return pieces;
    }

    /**
     * @return the started
     */
    public Date getStarted() {
	return started;
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

	return valid;
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
     * Set the file
     * 
     * @param file
     */
    public void setFile(File file) {
	this.file = generateFile(file);
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * @param parts
     *            the parts to set
     */
    public void setParts(int parts) {
	this.parts = parts;
    }

    /**
     * @param partSize
     *            the partSize to set
     */
    public void setPartSize(int partSize) {
	this.partSize = partSize;
    }

    /**
     * @param pieces
     *            the pieces to set
     */
    public void setPieces(Map<Integer, UploadPiece> pieces) {
	this.pieces = pieces;
    }

    /**
     * @param started
     *            the started to set
     */
    public void setStarted(Date started) {
	this.started = started;
    }

    /**
     * Write the status to file
     * 
     * @return
     * @throws IOException
     */
    public boolean write() throws IOException {
	return write(getFile());
    }

    /**
     * Write the status to file
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public boolean write(File file) throws IOException {

	file = generateFile(file);

	if (!file.exists()) {
	    file.createNewFile();
	}

	FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
	BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	bufferedWriter.write(new GsonBuilder().setPrettyPrinting().create()
		.toJson(this));

	bufferedWriter.close();
	fileWriter.close();

	return true;
    }

    /**
     * Write the status to file
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public boolean write(String file) throws IOException {
	return this.write(new File(file));
    }

}

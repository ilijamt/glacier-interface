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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MultipartUploadStatus {

    /**
     * The ID of the upload process
     */
    private String id;

    /**
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * When did this upload start?
     */
    private Date started = new Date();

    /**
     * @return the started
     */
    public Date getStarted() {
	return started;
    }

    /**
     * @param started
     *            the started to set
     */
    public void setStarted(Date started) {
	this.started = started;
    }

    /**
     * @return the partSize
     */
    public int getPartSize() {
	return partSize;
    }

    /**
     * @param partSize
     *            the partSize to set
     */
    public void setPartSize(int partSize) {
	this.partSize = partSize;
    }

    /**
     * @return the pieces
     */
    public Map<Integer, UploadPiece> getPieces() {
	return pieces;
    }

    /**
     * @param pieces
     *            the pieces to set
     */
    public void setPieces(Map<Integer, UploadPiece> pieces) {
	this.pieces = pieces;
    }

    /**
     * Add a piece that has been processed
     * 
     * @param piece
     */
    public void addPiece(UploadPiece piece) {
	this.pieces.put(piece.getPart(), piece);
    }

    /**
     * What was the part size when we
     */
    private int partSize;

    /**
     * The total parts available
     */
    private int parts = 0;

    /**
     * @return the parts
     */
    public int getParts() {
	return parts;
    }

    /**
     * @param parts
     *            the parts to set
     */
    public void setParts(int parts) {
	this.parts = parts;
    }

    /**
     * The pieces
     */
    private Map<Integer, UploadPiece> pieces;

    public MultipartUploadStatus() {
	this.pieces = new HashMap<Integer, UploadPiece>();
    }

    public MultipartUploadStatus(int total) {
	this.pieces = new HashMap<Integer, UploadPiece>(total);
	for (int i = 0; i < total; i++) {
	    this.pieces.put(i, null);
	}

    }

    public boolean write(String file) throws IOException {
	return this.write(new File(file));
    }

    public static MultipartUploadStatus load(String file) throws IOException {
	return load(new File(file));
    }

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

    public static File generateFile(File file) {

	File status = new File(file.getAbsolutePath() + ".state");

	return status;

    }

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

}

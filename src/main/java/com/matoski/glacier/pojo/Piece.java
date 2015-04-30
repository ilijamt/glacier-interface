package com.matoski.glacier.pojo;

import com.amazonaws.services.glacier.TreeHashGenerator;
import com.matoski.glacier.enums.MultipartPieceStatus;

/**
 * Describes a piece that is being uploaded to Amazon Glacier server, contains all the necessary
 * information to make sure that the piece has uploaded succsefully.
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class Piece {

    /**
     * The calculated checksum.
     */
    private String calculatedChecksum;

    /**
     * The ID of the piece.
     */
    private String id;

    /**
     * The part of the piece.
     */
    private int part;

    /**
     * The status of the piece.
     */
    private MultipartPieceStatus status;

    /**
     * The uploaded checksum of the piece.
     */
    private String uploadedChecksum;

    /**
     * Gets the calculated checksum usually done by
     * {@link TreeHashGenerator#calculateTreeHash(java.io.File)}, or from the response from amazon
     * glacier.
     *
     * @return The calculated checksum
     */
    public String getCalculatedChecksum() {
        return calculatedChecksum;
    }

    /**
     * Sets the calculated checksum.
     *
     * @param calculatedChecksum The calculated checksum
     * @return The current object so we can chain the setters
     */
    public Piece setCalculatedChecksum(String calculatedChecksum) {
        this.calculatedChecksum = calculatedChecksum;
        return this;
    }

    /**
     * Gets the piece ID.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the Piece ID.
     *
     * @param id the id to set
     * @return The current object so we can chain the setters
     */
    public Piece setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Gets the part number.
     *
     * @return The piece part number
     */
    public int getPart() {
        return part;
    }

    /**
     * Sets the piece part.
     *
     * @param part The part number of this piece
     * @return The current object so we can chain the setters
     */
    public Piece setPart(int part) {
        this.part = part;
        return this;
    }

    /**
     * Gets the status of the piece.
     * <p/>
     * <p>
     * If the piece has successfully completed then the result is
     * {@link MultipartPieceStatus#PIECE_COMPLETE}, if the piece competed but has invalid checksum
     * {@link MultipartPieceStatus#PIECE_CHECKSUM_MISMATCH}.
     * </p>
     * <p>
     * For more details take a look at {@link MultipartPieceStatus}
     * </p>
     *
     * @return The status of the piece
     */
    public MultipartPieceStatus getStatus() {
        return status;
    }

    /**
     * Sets the piece status.
     *
     * @param status Status of the piece
     * @return The current object so we can chain the setters
     */
    public Piece setStatus(MultipartPieceStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Gets the uploaded checksum, this is calculated by
     * {@link TreeHashGenerator#calculateTreeHash(java.io.File)}
     *
     * @return The calculated checksum
     */
    public String getUploadedChecksum() {
        return uploadedChecksum;
    }

    /**
     * Sets the uploaded checksum.
     *
     * @param uploadedChecksum The uploaded checksum
     * @return The current object so we can chain the setters
     */
    public Piece setUploadedChecksum(String uploadedChecksum) {
        this.uploadedChecksum = uploadedChecksum;
        return this;
    }

    /**
     * Is the piece completed.
     * <p>
     * Compares the {@link #status} to {@link MultipartPieceStatus#PIECE_COMPLETE}
     * </p>
     *
     * @return true if the piece has succesfully finished
     */
    public boolean isFinished() {
        return this.status == MultipartPieceStatus.PIECE_COMPLETE;
    }

}

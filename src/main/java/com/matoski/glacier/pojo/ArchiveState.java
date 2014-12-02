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
import com.matoski.glacier.base.AbstractWritablePojo;
import com.matoski.glacier.enums.MultipartPieceStatus;
import com.matoski.glacier.enums.MultipartStatus;

/**
 * The generic archive state, used for keeping track of the progress of the download or upload of an
 * archive.
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 *
 * @param <T>
 *          The class type, we can have seperate classes for Download/Upload, and add custom logic
 *          into it
 */
public abstract class ArchiveState<T> extends AbstractWritablePojo<T> {

  /**
   * The checksums.
   */
  private List<byte[]> checksums = new LinkedList<byte[]>();

  /**
   * The final checksum.
   */
  private String finalChecksum;

  /**
   * The original location for the upload.
   */
  private String location;

  /**
   * Is the transfer initiated.
   */
  private boolean initiated = false;

  /**
   * The generic status.
   */
  MultipartStatus status = MultipartStatus.NOP;

  /**
   * The ID of the upload process.
   */
  private String id;

  /**
   * The total parts available.
   */
  private int parts = 0;

  /**
   * What was the part size when we.
   */
  private int partSize;

  /**
   * The pieces.
   */
  private TreeMap<Integer, Piece> pieces;

  /**
   * When did this upload start.
   */
  private Date started;

  /**
   * When was the last time this was updated.
   */
  private Date lastUpdate;

  /**
   * Constructor.
   */
  public ArchiveState() {
    setDirty();
    this.pieces = new TreeMap<Integer, Piece>();
  }

  /**
   * Add a piece that has been processed.
   * 
   * @param piece
   *          The piece that was processed
   * 
   * @throws IOException
   *           We cannot write the data to IO
   */
  public void addPiece(Piece piece) throws NullPointerException, IOException {
    setDirty();
    if (!this.pieces.containsKey(piece.getPart())) {
      this.pieces.put(piece.getPart(), piece);
      this.lastUpdate = new Date();
      update();
      this.write();
    }
  }

  /**
   * Add a piece that has been processed.
   * 
   * @param part
   *          Which part is this piece
   * 
   * @param piece
   *          The piece that was processed
   * 
   * @throws IOException
   *           Cannot write to the file
   * @throws NullPointerException
   *           No file defined
   */
  public void addPiece(int part, Piece piece) throws NullPointerException, IOException {
    setDirty();
    if (!this.pieces.containsKey(part)) {
      this.pieces.put(part, piece);
      this.lastUpdate = new Date();
      this.write();
      update();
    }
  }

  /**
   * Do we actually have the {@link Piece} already in {@link #pieces}.
   * 
   * @param piece
   *          The piece to check
   * 
   * @return true if the piece is present in the state
   */
  public Boolean exists(Piece piece) {
    return this.pieces.containsValue(piece);
  }

  /**
   * Get the checksums list.
   * 
   * @return List of checksums
   */
  public List<byte[]> getChecksums() {
    return this.checksums;
  }

  /**
   * Get the calculated checksum.
   * 
   * @return {@link #finalChecksum}
   */
  public String getFinalChecksum() {
    return this.finalChecksum;
  }

  /**
   * Get the ID of this state.
   * 
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * Get the last update of the state.
   * 
   * @return {@link #lastUpdate}
   */
  public Date getLastUpdate() {
    return lastUpdate;
  }

  /**
   * Get that location of the files.
   * 
   * @return {@link #location}
   */
  public String getLocation() {
    return location;
  }

  /**
   * Get the total parts.
   * 
   * @return {@link #parts}
   */
  public int getParts() {
    return parts;
  }

  /**
   * Get the part size for this archive.
   * 
   * @return the partSize
   */
  public int getPartSize() {
    return partSize;
  }

  public Piece getPiece(int part) {
    return this.pieces.get(part);
  }

  /**
   * Get all the pieces in the state file.
   * 
   * @return the pieces
   */
  public TreeMap<Integer, Piece> getPieces() {
    return pieces;
  }

  /**
   * Gets when was this process started.
   * 
   * @return the started
   */
  public Date getStarted() {
    return started;
  }

  /**
   * Get the status of the archive.
   * 
   * @return the status
   */
  public MultipartStatus getStatus() {
    return status;
  }

  /**
   * Is everything finished.
   * 
   * <p>
   * It will iterate over {@link #pieces} and compare the status with
   * {@link MultipartPieceStatus#PIECE_COMPLETE}
   * </p>
   * <p>
   * If the state is finished, {@link #remove()} is called.
   * </p>
   * *
   * 
   * @return true if the piece is finished
   */
  public Boolean isFinished() {
    return isFinished(true);
  }

  /**
   * Is everything finished.
   * 
   * <p>
   * It will iterate over {@link #pieces} and compare the status with
   * {@link MultipartPieceStatus#PIECE_COMPLETE}
   * </p>
   * <p>
   * If the state is finished, {@link #remove()} is called based on the <code>remove</code>.
   * </p>
   * 
   * @param remove
   *          Remove the state file if is finished?
   * 
   * @return true if the piece is finished
   */
  public Boolean isFinished(boolean remove) {

    if (parts < this.pieces.size()) {
      // we have less elements in the map than there should be
      // parts, so it's not finished
      return false;
    }

    Boolean valid = parts == this.pieces.size();

    // go over the elements, and compare if all the pieces are complete
    for (Entry<Integer, Piece> piece : this.pieces.entrySet()) {
      valid &= piece.getValue().getStatus() == MultipartPieceStatus.PIECE_COMPLETE;
    }

    if (valid) {
      this.remove();
    }

    this.update();

    return valid;
  }

  /**
   * Is it initiated.
   * 
   * @return {@link #initiated}
   */
  public boolean isInitiated() {
    return initiated;
  }

  /**
   * Is the piece completed.
   * 
   * <p>
   * Compares the {@link Piece#getStatus()} to {@link MultipartPieceStatus#PIECE_COMPLETE}
   * </p>
   * 
   * @param piece
   *          Piece to check if it exists
   * 
   * @return true if the piece is completed
   */
  public boolean isPieceCompleted(int piece) {
    return this.pieces.containsKey(piece) && this.pieces.get(piece).isFinished();
  }

  /**
   * Remove the state file.
   * 
   * @return true if we removed the state file, false otherwise
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
   * Set the checksums for the archive.
   * 
   * @param checksums
   *          the checksums to set
   */
  public void setChecksums(List<byte[]> checksums) {
    this.checksums = checksums;
  }

  /**
   * Set the final checksum, which is calculated from all the pieces.
   * 
   * @param finalChecksum
   *          the finalChecksum to set
   */
  public void setFinalChecksum(String finalChecksum) {
    this.finalChecksum = finalChecksum;
  }

  /**
   * Sets the ID for the archive, used to process the download or upload.
   * 
   * @param id
   *          the id to set
   */
  public void setId(String id) {
    setDirty();
    this.id = id;
  }

  /**
   * Sets initiated state.
   * 
   * @param initiated
   *          the initiated to set
   */
  public void setInitiated(boolean initiated) {
    setDirty();
    this.initiated = initiated;
  }

  /**
   * Sets the last update for the state.
   * 
   * @param lastUpdate
   *          the lastUpdate to set
   */
  public void setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  /**
   * Sets the location for the state.
   * 
   * @param location
   *          the location to set
   */
  public void setLocation(String location) {
    setDirty();
    this.location = location;
  }

  /**
   * Sets the total parts for the state.
   * 
   * @param parts
   *          the parts to set
   */
  public void setParts(int parts) {
    setDirty();
    this.parts = parts;
  }

  /**
   * Sets the part size used for the process, this cannot be changed, if you want to change we
   * should delete the state and start it again.
   * 
   * @param partSize
   *          the partSize to set
   */
  public void setPartSize(int partSize) {
    setDirty();
    this.partSize = partSize;
  }

  /**
   * Sets the pieces.
   * 
   * @param pieces
   *          the pieces to set
   */
  public void setPieces(TreeMap<Integer, Piece> pieces) {
    setDirty();
    this.pieces = pieces;
  }

  /**
   * Sets the date and time when it was started.
   * 
   * @param started
   *          the started to set
   */
  public void setStarted(Date started) {
    setDirty();
    this.started = started;
  }

  /**
   * Sets the status of the progress.
   * 
   * @param status
   *          the status to set
   */
  public void setStatus(MultipartStatus status) {
    setDirty();
    this.status = status;
  }

  /**
   * Updates the checksum.
   */
  public void update() {
    this.checksums.clear();
    for (Entry<Integer, Piece> entry : this.pieces.entrySet()) {
      this.checksums.add(BinaryUtils.fromHex(entry.getValue().getCalculatedChecksum()));
    }
    this.finalChecksum = TreeHashGenerator.calculateTreeHash(checksums);
  }

}

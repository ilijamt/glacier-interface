package com.matoski.glacier.enums;

/**
 * Multipart status, contains the states as they go through while uploading or downloading for a
 * piece.
 * 
 * <p>
 * Used to track the upload/download process for a specific piece, as it goes through it.
 * </p>
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 *
 */
public enum MultipartPieceStatus {

  /**
   * No operation.
   */
  NOP,

  /**
   * We have just started a piece.
   */
  PIECE_START,

  /**
   * Piece has been completed.
   */
  PIECE_COMPLETE,

  /**
   * Piece has been completed but the checksum is different from expected.
   */
  PIECE_CHECKSUM_MISMATCH,

  /**
   * Error while processing the piece.
   */
  PIECE_ERROR,

  /**
   * Invalid or non existent part for the piece.
   */
  PIECE_INVALID_PART

}

package com.matoski.glacier.util.upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.Protocol;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.metrics.RequestMetricCollector;
import com.amazonaws.services.glacier.model.RequestTimeoutException;
import com.matoski.glacier.enums.MultipartPieceStatus;
import com.matoski.glacier.errors.InvalidChecksumException;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.pojo.Piece;

/**
 * A threaded upload manager.
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class ThreadAmazonGlacierUploadUtil extends AmazonGlacierUploadUtil implements
    Callable<Piece> {

  /**
   * The file for the upload.
   */
  private final File requestFile;

  /**
   * Total pieces.
   */
  private final int requestPieces;

  /**
   * The part to upload.
   */
  private final int requestPart;

  /**
   * The partition size.
   */
  private final int requestPartSize;

  /**
   * The vault name.
   */
  private final String requestVaultName;

  /**
   * The upload ID for the request.
   */
  private final String requestUploadId;

  /**
   * The progress listener.
   */
  private final ProgressListener requestListener;

  /**
   * The request metric collector.
   */
  private final RequestMetricCollector requestCollector;

  /**
   * How many times to retry uploading a failed upload, either it is an error, or a
   * {@link MultipartPieceStatus#PIECE_CHECKSUM_MISMATCH} is present in the result.
   */
  private final int requestRetryFailedUploads;

  /**
   * Constructor.
   * 
   * @param retryFailedUploads
   *          How many times to retry failed uploads
   * @param file
   *          The file to upload
   * @param pieces
   *          How many pieces there are for the upload
   * @param part
   *          Current part that is uploaded
   * @param partSize
   *          The part size for the upload
   * @param vaultName
   *          The vault name where to save the archive
   * @param uploadId
   *          The upload ID
   * @param accessKey
   *          The amazon access key
   * @param secretKey
   *          The amazon secret key
   * @param region
   *          The region
   * @param protocol
   *          The protocol used to connect to the Amazon Glacier servers
   *          
   * @throws RegionNotSupportedException
   *           Invalid or unsupported region
   */
  public ThreadAmazonGlacierUploadUtil(int retryFailedUploads, File file, int pieces, int part,
      int partSize, String vaultName, String uploadId, String accessKey, String secretKey,
      String region, Protocol protocol) throws RegionNotSupportedException {
    super(accessKey, secretKey, region, protocol);
    this.requestRetryFailedUploads = retryFailedUploads;
    this.requestFile = file;
    this.requestPieces = pieces;
    this.requestPart = part;
    this.requestPartSize = partSize;
    this.requestVaultName = vaultName;
    this.requestUploadId = uploadId;
    this.requestCollector = null;
    this.requestListener = null;
  }

  /**
   * Constructor.
   * 
   * @param retryFailedUploads
   *          How many times to retry failed uploads
   * @param file
   *          The file to upload
   * @param pieces
   *          How many pieces there are for the upload
   * @param part
   *          Current part that is uploaded
   * @param partSize
   *          The part size for the upload
   * @param vaultName
   *          The vault name where to save the archive
   * @param uploadId
   *          The upload ID
   * @param accessKey
   *          The amazon access key
   * @param secretKey
   *          The amazon secret key
   * @param region
   *          The region
   * @param listener
   *          Progress listener
   * @param collector
   *          Metric collector
   * @param protocol
   *          The protocol used to connect to the Amazon Glacier servers
   * 
   * @throws RegionNotSupportedException
   *           Invalid or unsupported region
   */
  public ThreadAmazonGlacierUploadUtil(int retryFailedUploads, File file, int pieces, int part,
      int partSize, String vaultName, String uploadId, String accessKey, String secretKey,
      String region, ProgressListener listener, RequestMetricCollector collector, Protocol protocol)
      throws RegionNotSupportedException {
    super(accessKey, secretKey, region, protocol);
    this.requestRetryFailedUploads = retryFailedUploads;
    this.requestFile = file;
    this.requestPieces = pieces;
    this.requestPart = part;
    this.requestPartSize = partSize;
    this.requestVaultName = vaultName;
    this.requestUploadId = uploadId;
    this.requestListener = listener;
    this.requestCollector = collector;
  }

  @Override
  public Piece call() throws Exception {
    Piece piece = null;
    int count = 0;

    System.out.println(String.format(FORMAT, requestPart + 1, requestPieces,
        MultipartPieceStatus.PIECE_START, requestFile, "Upload started"));

    for (int i = 0; i < requestRetryFailedUploads; i++) {

      try {

        piece = this.upload(count);

        if (piece.getStatus() == MultipartPieceStatus.PIECE_COMPLETE) {
          // we have successfully uploaded the file, so we break now,
          // no need to continue trying to re-upload the part again
          System.out.println(String.format(FORMAT, requestPart + 1, requestPieces,
              piece.getStatus(), requestFile, "Uploaded"));
          break;
        }

      } catch (RequestTimeoutException e) {
        System.err.println(String.format("HTTP 408, retry %s", i + 1));
      } catch (AmazonClientException e) {
        System.err.println(String.format("Amazon client exception, retry %s", i + 1));
      } catch (NoSuchAlgorithmException | IOException e) {
        throw e;
      }

    }

    return piece;
  }

  /**
   * Initiate an upload.
   * 
   * @param time
   *          Which time is the upload started
   * 
   * @return The details about the upload piece
   * 
   * @throws AmazonServiceException
   *           Amazon service exception
   * @throws NoSuchAlgorithmException
   *           The cryptograhic algorithm doesn't exist
   * @throws AmazonClientException
   *           Amazon client exception
   * @throws FileNotFoundException
   *           File doesn't exists
   * @throws IOException
   *           IO Exception
   * @throws RequestTimeoutException
   *           Timeout during request
   * @throws InvalidChecksumException
   *           Invalid checksum during upload
   */
  private Piece upload(int time) throws AmazonServiceException, NoSuchAlgorithmException,
      AmazonClientException, FileNotFoundException, IOException, RequestTimeoutException,
      InvalidChecksumException {

    return this.uploadMultipartPiece(requestFile, requestPieces, requestPart, requestPartSize,
        requestVaultName, requestUploadId, requestListener, requestCollector);

  }

}

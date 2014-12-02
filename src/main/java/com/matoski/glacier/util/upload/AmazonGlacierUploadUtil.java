package com.matoski.glacier.util.upload;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.metrics.RequestMetricCollector;
import com.amazonaws.regions.Region;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.TreeHashGenerator;
import com.amazonaws.services.glacier.model.AbortMultipartUploadRequest;
import com.amazonaws.services.glacier.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.glacier.model.CompleteMultipartUploadResult;
import com.amazonaws.services.glacier.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.glacier.model.InitiateMultipartUploadResult;
import com.amazonaws.services.glacier.model.RequestTimeoutException;
import com.amazonaws.services.glacier.model.UploadArchiveRequest;
import com.amazonaws.services.glacier.model.UploadArchiveResult;
import com.amazonaws.services.glacier.model.UploadMultipartPartRequest;
import com.amazonaws.services.glacier.model.UploadMultipartPartResult;
import com.matoski.glacier.Constants;
import com.matoski.glacier.enums.ArchiveState;
import com.matoski.glacier.enums.GenericValidateEnum;
import com.matoski.glacier.enums.Metadata;
import com.matoski.glacier.enums.MultipartPieceStatus;
import com.matoski.glacier.enums.MultipartStatus;
import com.matoski.glacier.errors.InvalidChecksumException;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.errors.UploadTooManyPartsException;
import com.matoski.glacier.pojo.Config;
import com.matoski.glacier.pojo.Piece;
import com.matoski.glacier.pojo.archive.Archive;
import com.matoski.glacier.pojo.journal.State;
import com.matoski.glacier.pojo.upload.MultipartUploadStatus;
import com.matoski.glacier.util.AmazonGlacierBaseUtil;
import com.matoski.glacier.util.Parser;

/**
 * Amazon Glacier helper utilities.
 * 
 * <p>
 * Contains utilities for uploading archives, initiation of multipart uploads, canceling of
 * multipart uploads
 * </p>
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class AmazonGlacierUploadUtil extends AmazonGlacierBaseUtil {

  /**
   * Constructor.
   * 
   * @param credentials
   *          Amazon credentials
   * @param client
   *          Amazon client
   * @param region
   *          Amazon region
   */
  public AmazonGlacierUploadUtil(BasicAWSCredentials credentials, AmazonGlacierClient client,
      Region region) {
    super(credentials, client, region);
  }

  /**
   * Constructor.
   * 
   * @param accessKey
   *          Amazon access key
   * @param secretKey
   *          Amazon secret key
   * @param region
   *          Amazon region
   * 
   * @throws RegionNotSupportedException
   *           Invalid or unsupported region
   */
  public AmazonGlacierUploadUtil(String accessKey, String secretKey, String region)
      throws RegionNotSupportedException {
    super(accessKey, secretKey, region);
  }

  /**
   * Cancel the multipart upload.
   * 
   * @param uploadId
   *          The upload ID
   * @param vaultName
   *          Vault name
   * 
   * @return true if the upload is canceled succesfully
   */
  public Boolean cancelMultipartUpload(String uploadId, String vaultName) {
    return this.cancelMultipartUpload(uploadId, vaultName, null, null);
  }

  /**
   * Cancel the multipart upload.
   * 
   * @param uploadId
   *          The upload ID
   * @param vaultName
   *          Vault name
   * @param listener
   *          Progress listener
   * @param collector
   *          Metric collector
   * 
   * @return true if the upload is canceled succesfully
   */
  public Boolean cancelMultipartUpload(String uploadId, String vaultName,
      ProgressListener listener, RequestMetricCollector collector) {

    Boolean valid = false;

    AbortMultipartUploadRequest request = new AbortMultipartUploadRequest().withUploadId(uploadId)
        .withVaultName(vaultName);

    if (null != listener) {
      request.withGeneralProgressListener(listener);
    }

    if (null != collector) {
      request.withRequestMetricCollector(collector);
    }

    try {
      this.client.abortMultipartUpload(request);
      valid = true;
    } catch (AmazonServiceException e) {
      valid = false;
    }

    return valid;

  }

  /**
   * Complete a multipart upload.
   * 
   * @param fileSize
   *          The size of the upload
   * @param uploadId
   *          The upload Id
   * @param vaultName
   *          Vault name
   * @param checksum
   *          Upload checksum
   * 
   * @return Details about the request
   * 
   * @throws AmazonClientException
   *           Amazon client exception
   * @throws AmazonServiceException
   *           Amazon service exception
   */
  public CompleteMultipartUploadResult completeMultipartUpload(long fileSize, String uploadId,
      String vaultName, String checksum) throws AmazonClientException, AmazonServiceException {
    return this.completeMultipartUpload(fileSize, uploadId, vaultName, checksum, null, null);
  }

  /**
   * Complete a multipart upload.
   * 
   * @param fileSize
   *          The size of the upload
   * @param uploadId
   *          The upload Id
   * @param vaultName
   *          Vault name
   * @param checksum
   *          Upload checksum
   * @param listener
   *          Progress listener
   * @param collector
   *          Metric collector
   * 
   * @return Details about the request
   * 
   * @throws AmazonClientException
   *           Amazon client exception
   * @throws AmazonServiceException
   *           Amazon service exception
   */
  public CompleteMultipartUploadResult completeMultipartUpload(long fileSize, String uploadId,
      String vaultName, String checksum, ProgressListener listener,
      RequestMetricCollector collector) throws AmazonClientException, AmazonServiceException {

    CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest()
        .withVaultName(vaultName).withUploadId(uploadId).withChecksum(checksum)
        .withArchiveSize(String.valueOf(fileSize));

    if (null != listener) {
      request.withGeneralProgressListener(listener);
    }

    if (null != collector) {
      request.withRequestMetricCollector(collector);
    }

    return this.client.completeMultipartUpload(request);

  }

  /**
   * Initiate a multipart upload.
   * 
   * @param vaultName
   *          Vault name
   * @param archiveDescription
   *          Archive description
   * @param partSize
   *          Archive part
   * 
   * @return Details about the request
   * 
   * @throws AmazonClientException
   *           Amazon client exception
   * @throws AmazonServiceException
   *           Amazon service exception
   */
  public InitiateMultipartUploadResult initiateMultipartUpload(String vaultName,
      String archiveDescription, long partSize) throws AmazonClientException,
      AmazonServiceException {
    return this.initiateMultipartUpload(vaultName, archiveDescription, partSize, null, null);
  }

  /**
   * Initiate a multipart upload.
   * 
   * @param vaultName
   *          Vault name
   * @param archiveDescription
   *          Archive description
   * @param partSize
   *          Archive part
   * @param listener
   *          Progress listener
   * @param collector
   *          Metric collector
   * 
   * @return Details about the request
   * 
   * @throws AmazonClientException
   *           Amazon client exception
   * @throws AmazonServiceException
   *           Amazon service exception
   */
  public InitiateMultipartUploadResult initiateMultipartUpload(String vaultName,
      String archiveDescription, long partSize, ProgressListener listener,
      RequestMetricCollector collector) throws AmazonClientException, AmazonServiceException {

    InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest()
        .withVaultName(vaultName).withArchiveDescription(archiveDescription)
        .withPartSize(String.valueOf(partSize));

    if (null != listener) {
      request.withGeneralProgressListener(listener);
    }

    if (null != collector) {
      request.withRequestMetricCollector(collector);
    }

    return this.client.initiateMultipartUpload(request);
  }

  /**
   * Upload an archive in a single request.
   * 
   * @param file
   *          File to upload
   * @param vaultName
   *          To which vault to upload
   * @param archiveDescription
   *          Archive description
   * @param listener
   *          Progress listener
   * @param collector
   *          Metric collector
   * 
   * @return Details about the request
   * 
   * @throws FileNotFoundException
   *           Upload file not found
   * @throws AmazonServiceException
   *           Amazon service exception
   * @throws AmazonClientException
   *           Amazon client exception
   */
  public UploadArchiveResult singleUpload(File file, String vaultName, String archiveDescription,
      ProgressListener listener, RequestMetricCollector collector) throws FileNotFoundException,
      AmazonServiceException, AmazonClientException {

    InputStream stream = new FileInputStream(file);
    String checksum = TreeHashGenerator.calculateTreeHash(file);
    long fileSize = file.length();

    UploadArchiveRequest request = new UploadArchiveRequest().withVaultName(vaultName)
        .withArchiveDescription(archiveDescription).withChecksum(checksum).withBody(stream)
        .withContentLength(fileSize);

    if (null != listener) {
      request.withGeneralProgressListener(listener);
    }

    if (null != collector) {
      request.withRequestMetricCollector(collector);
    }

    return this.client.uploadArchive(request);
  }

  /**
   * Upload archive to glacier.
   * 
   * @param journal
   *          Journal to use
   * @param vaultName
   *          Vault name
   * @param fileName
   *          File name
   * @param forceUpload
   *          Force upload if the file exists in the journal
   * @param concurrent
   *          How many threads should be used for upload
   * @param retryFailedUpload
   *          How many times to retry upload
   * @param partSize
   *          Upload part size
   * @param replace
   *          Replace the upload, if true it will delete the old archive
   * 
   * @return Details about the request
   */
  public Archive uploadArchive(State journal, String vaultName, String fileName,
      Boolean forceUpload, int concurrent, int retryFailedUpload, int partSize, boolean replace) {
    return uploadArchive(journal, vaultName, fileName, forceUpload, concurrent, retryFailedUpload,
        partSize, replace, false);
  }

  /**
   * Upload archive to glacier.
   * 
   * @param journal
   *          Journal to use
   * @param vaultName
   *          Vault name
   * @param fileName
   *          File name
   * @param forceUpload
   *          Force upload if the file exists in the journal
   * @param concurrent
   *          How many threads should be used for upload
   * @param retryFailedUpload
   *          How many times to retry upload
   * @param partSize
   *          Upload part size
   * @param replace
   *          Replace the upload, if true it will delete the old archive
   * @param dryRun
   *          Dry run, don't do anything just print out to console
   * 
   * @return Details about the request
   */
  public Archive uploadArchive(State journal, String vaultName, String fileName,
      Boolean forceUpload, int concurrent, int retryFailedUpload, int partSize, boolean replace,
      boolean dryRun) {

    Archive archive = null;
    Metadata metadata = journal.getMetadata();
    boolean upload = true;
    Boolean exists = journal.isFileInArchive(fileName);
    Archive old = exists ? journal.getByName(fileName) : null;

    // check if this file exists in the journal
    if (exists && (forceUpload || replace)) {

      upload = false;
      System.out.println(String.format("%s is already present in the journal", fileName));

      if (forceUpload) {

        System.out.println(String.format("Force upload in effect"));
        upload = forceUpload;

      } else if (replace) {

        System.out.println(String.format("Verifying ..."));

        Archive testArchive = journal.getByName(fileName);

        GenericValidateEnum validSize = State.archiveValidateFileSize(testArchive);
        GenericValidateEnum validModifiedDate = State.archiveValidateLastModified(testArchive);

        System.out.println(String.format("%s size is %s", fileName, validSize));
        System.out.println(String.format("%s modified date is %s", fileName, validModifiedDate));
        System.out.println(String
            .format("Verifying hash, this may take a while depending on the file size ..."));

        GenericValidateEnum validTreeHash = State.archiveValidateTreeHash(testArchive);

        System.out.println(String.format("Hash is: %s", validTreeHash));
        System.out.println();

        upload = validSize != GenericValidateEnum.VALID
            || validModifiedDate != GenericValidateEnum.VALID
            || validTreeHash != GenericValidateEnum.VALID;

      } else {

        System.err.println("Why are we here?");

      }
    }

    if (upload && !dryRun) {

      System.out.println(String.format("Processing: %s (size: %s)", fileName, new File(Config
          .getInstance().getDirectory(), fileName).length()));

      try {

        archive = uploadMultipartFile(fileName, new File(Config.getInstance().getDirectory(),
            fileName), concurrent, retryFailedUpload, partSize, vaultName, metadata);

        // delete the old archive if we have replace the file property
        if (replace && archive != null) {
          System.out.println(String.format("Cleaning, removing old archive [%s] %s", old.getId(),
              old.getName()));
          deleteArchive(vaultName, old.getId());
        }

        journal.addArchive(archive);
        journal.save();

      } catch (UploadTooManyPartsException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (RegionNotSupportedException e) {
        e.printStackTrace();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }

    } else {

      System.out.println(String.format("%s Skipping upload for %s", dryRun ? "[--dry-run]" : "",
          fileName));

    }

    return archive;

  }

  /**
   * Upload a multipart file.
   * 
   * @param fileName
   *          Filename to upload
   * @param file
   *          A file representation of Filename
   * @param threads
   *          How many threads to use
   * @param retry
   *          How many times to retry
   * @param partSize
   *          Upload part size
   * @param vaultName
   *          Vault name
   * @param metadata
   *          Metadata
   * 
   * @return The details about the upload of the archive
   * 
   * @throws UploadTooManyPartsException
   *           There are too many parts
   * @throws IOException
   *           IO Exception
   * @throws RegionNotSupportedException
   *           Invalid or unsupported region
   * @throws InstantiationException
   *           Cannot load the state file
   * @throws IllegalAccessException
   *           Invalid data in the state file
   */
  public Archive uploadMultipartFile(String fileName, File file, int threads, int retry,
      int partSize, String vaultName, Metadata metadata) throws UploadTooManyPartsException,
      IOException, RegionNotSupportedException, InstantiationException, IllegalAccessException {

    ExecutorService pool = Executors.newFixedThreadPool(threads);
    HashMap<Integer, Future<Piece>> map = new HashMap<Integer, Future<Piece>>();

    final File stateFile = new File(file, ".state");
    Archive archive = new Archive();
    final long fileSize = file.length();
    int pieces = (int) Math.ceil(fileSize / (double) partSize);

    if (!isValidMaxParts(file, partSize)) {
      throw new UploadTooManyPartsException();
    }

    archive.setCreatedDate(new Date());
    archive.setState(ArchiveState.CREATE);

    // 0. Get the upload state file, check if we have a state already
    final MultipartUploadStatus uploadStatus;

    if (MultipartUploadStatus.has(stateFile)) {
      System.out.println(String.format("Upload state found for %s, loading", file.getName()));
      uploadStatus = MultipartUploadStatus.load(stateFile, MultipartUploadStatus.class);
    } else {
      uploadStatus = new MultipartUploadStatus();
    }

    String location = null;
    String uploadId = null;

    // 1. Initiate MultiPart Upload
    if (uploadStatus.isInitiated()) {
      location = uploadStatus.getLocation();
      uploadId = uploadStatus.getId();
      System.out.println(String.format("Upload already initiated with location: %s and id: %s",
          location, uploadId));

      if (partSize != uploadStatus.getPartSize() || pieces != uploadStatus.getParts()) {
        pieces = uploadStatus.getParts();
        partSize = uploadStatus.getPartSize();
        System.out.println(String.format("[Overriding] Parts: %s and piece size: %s bytes",
            pieces, partSize));
      }
      System.out.println();
    } else {
      InitiateMultipartUploadResult initiate = this.initiateMultipartUpload(vaultName, Parser
          .getParser(metadata).encode(archive), partSize);

      location = initiate.getLocation();
      uploadId = initiate.getUploadId();

      uploadStatus.setStatus(MultipartStatus.START);
      uploadStatus.setPartSize(partSize);
      uploadStatus.setParts(pieces);
      uploadStatus.setInitiated(true);
      uploadStatus.setFile(file);
      uploadStatus.setId(uploadId);
      uploadStatus.setLocation(location);
      uploadStatus.setStarted(new Date());

      try {
        uploadStatus.write();
      } catch (NullPointerException | IOException e) {
        uploadStatus.write();
        System.err.println(String.format("ERROR: %s", e.getMessage()));
      }

      uploadStatus.setStatus(MultipartStatus.IN_PROGRESS);
    }

    Callable<Piece> thread;

    // 2. Upload Pieces
    for (int i = 0; i < pieces; i++) {

      if (uploadStatus.isPieceCompleted(i)) {

        thread = new ThreadAmazonGlacierUploadDummy(pieces, file, uploadStatus.getPiece(i));

      } else {

        thread = new ThreadAmazonGlacierUploadUtil(retry, file, pieces, i, partSize, vaultName,
            uploadId, credentials.getAWSAccessKeyId(), credentials.getAWSSecretKey(),
            region.getName());

      }

      map.put(i, pool.submit(thread));

    }

    pool.shutdown();

    Piece piece = null;
    Future<Piece> future;
    Boolean pieceExists = false;

    try {
      while (!pool.awaitTermination(Constants.WAIT_TIME_THREAD_CHECK, TimeUnit.SECONDS)) {

        for (Entry<Integer, Future<Piece>> entry : map.entrySet()) {

          future = entry.getValue();

          try {

            piece = future.get(Constants.WAIT_FETCH_OBJECT_FROM_POOL, TimeUnit.MILLISECONDS);

            pieceExists = uploadStatus.exists(piece);

            if (!pieceExists && future.isDone() && !future.isCancelled()) {
              uploadStatus.addPiece(piece);
            }
          } catch (InterruptedException e) {
            uploadStatus.write();
          } catch (ExecutionException e) {
            uploadStatus.write();
          } catch (IOException e) {
            uploadStatus.write();
            System.err.println(String.format("ERROR: %s", e.getMessage()));
          } catch (TimeoutException e) {
            // we skip this item as it has not finished yet we don't
            // do anything here
            System.err.println(e);
          }

        }
      }
    } catch (InterruptedException e) {
      System.err.println(String.format("ERROR: %s", e.getMessage()));
    }

    // go through them again, in case we missed them , can happen if it
    // finishes very shortly after completion
    for (Entry<Integer, Future<Piece>> entry : map.entrySet()) {

      future = entry.getValue();

      try {

        piece = future.get();

        pieceExists = uploadStatus.exists(piece);

        if (!pieceExists && future.isDone() && !future.isCancelled()) {
          uploadStatus.addPiece(piece);
        }

      } catch (InterruptedException | ExecutionException e) {
        uploadStatus.write();
        System.err.println(String.format("ERROR: %s", e.getMessage()));
      }

    }

    // process the upload state
    uploadStatus.isFinished();

    // 3. Finish MultiPart Upload
    CompleteMultipartUploadResult complete = this.completeMultipartUpload(fileSize, uploadId,
        vaultName, uploadStatus.getFinalChecksum());

    archive.setHash(complete.getChecksum());
    archive.setId(complete.getArchiveId());
    archive.setUri(complete.getLocation());

    archive.setSize(fileSize);
    archive.setName(fileName);
    archive.setModifiedDate(file.lastModified());

    return archive;
  }

  /**
   * Upload a piece of the file to amazon glacier.
   * 
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
   * 
   * @return The details about the upload of the piece
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
   * @throws InvalidChecksumException
   *           Invalid checksum during upload
   * @throws RequestTimeoutException
   *           Timeout during request
   */
  public Piece uploadMultipartPiece(File file, int pieces, int part, int partSize,
      String vaultName, String uploadId) throws AmazonServiceException, NoSuchAlgorithmException,
      AmazonClientException, FileNotFoundException, IOException, InvalidChecksumException,
      RequestTimeoutException {
    return uploadMultipartPiece(file, pieces, part, partSize, vaultName, uploadId, null, null);
  }

  /**
   * Upload a piece of the file to amazon glacier.
   * 
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
   * @param listener
   *          Progress listener
   * @param collector
   *          Metric collector
   * 
   * @return The details about the upload of the piece
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
   * @throws InvalidChecksumException
   *           Invalid checksum during upload
   * @throws RequestTimeoutException
   *           Timeout during request
   */
  public Piece uploadMultipartPiece(File file, int pieces, int part, int partSize,
      String vaultName, String uploadId, ProgressListener listener,
      RequestMetricCollector collector) throws AmazonServiceException, NoSuchAlgorithmException,
      AmazonClientException, FileNotFoundException, IOException, RequestTimeoutException,
      InvalidChecksumException {

    Piece ret = new Piece();

    ret.setPart(part);
    ret.setId(uploadId);
    ret.setStatus(MultipartPieceStatus.PIECE_START);

    int bufferSize = partSize;

    FileInputStream stream = new FileInputStream(file);

    int position = part * (int) partSize;

    @SuppressWarnings("unused")
    long skipped = stream.skip(position);

    if (part > pieces) {
      ret.setStatus(MultipartPieceStatus.PIECE_INVALID_PART);
      stream.close();
      return ret;
    } else if (part == pieces - 1) {
      bufferSize = (int) (file.length() - position);
    }

    byte[] buffer = new byte[bufferSize];
    int read = stream.read(buffer);
    stream.close();

    if (read == -1) {
      ret.setStatus(MultipartPieceStatus.PIECE_INVALID_PART);
      return ret;
    }

    String contentRange = String.format("bytes %s-%s/*", position, position + read - 1);
    String checksum = TreeHashGenerator.calculateTreeHash(new ByteArrayInputStream(buffer));

    ret.setCalculatedChecksum(checksum);

    UploadMultipartPartRequest request = new UploadMultipartPartRequest().withVaultName(vaultName)
        .withBody(new ByteArrayInputStream(buffer)).withChecksum(checksum).withRange(contentRange)
        .withUploadId(uploadId);

    if (null != listener) {
      request.withGeneralProgressListener(listener);
    }

    if (null != collector) {
      request.withRequestMetricCollector(collector);
    }

    UploadMultipartPartResult result = client.uploadMultipartPart(request);

    ret.setUploadedChecksum(result.getChecksum());

    if (ret.getCalculatedChecksum().equals(ret.getUploadedChecksum())) {
      ret.setStatus(MultipartPieceStatus.PIECE_COMPLETE);
    } else {
      ret.setStatus(MultipartPieceStatus.PIECE_CHECKSUM_MISMATCH);
    }

    return ret;
  }
}

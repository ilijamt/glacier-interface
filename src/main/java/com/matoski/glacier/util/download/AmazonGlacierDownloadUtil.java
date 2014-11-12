package com.matoski.glacier.util.download;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileAlreadyExistsException;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.metrics.RequestMetricCollector;
import com.amazonaws.regions.Region;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.TreeHashGenerator;
import com.amazonaws.services.glacier.model.DescribeJobResult;
import com.amazonaws.services.glacier.model.GetJobOutputRequest;
import com.amazonaws.services.glacier.model.GetJobOutputResult;
import com.amazonaws.services.glacier.model.InitiateJobRequest;
import com.amazonaws.services.glacier.model.InitiateJobResult;
import com.amazonaws.services.glacier.model.JobParameters;
import com.matoski.glacier.enums.MultipartStatus;
import com.matoski.glacier.errors.InvalidChecksumException;
import com.matoski.glacier.errors.RegionNotSupportedException;
import com.matoski.glacier.pojo.download.DownloadPiece;
import com.matoski.glacier.pojo.download.MultipartDownloadStatus;
import com.matoski.glacier.pojo.job.DownloadJob;
import com.matoski.glacier.util.AmazonGlacierBaseUtil;

/**
 * Amazon Glacier helper utilities
 * 
 * Contains utilities for download archives, initiation of multipart download,
 * canceling of multipart downloads
 * 
 * @author ilijamt
 */
public class AmazonGlacierDownloadUtil extends AmazonGlacierBaseUtil {

    /**
     * Constructor
     * 
     * @param credentials
     * @param client
     * @param region
     */
    public AmazonGlacierDownloadUtil(BasicAWSCredentials credentials, AmazonGlacierClient client, Region region) {
	super(credentials, client, region);
    }

    /**
     * Constructor
     * 
     * @param accessKey
     * @param secretKey
     * @param region
     * 
     * @throws RegionNotSupportedException
     */
    public AmazonGlacierDownloadUtil(String accessKey, String secretKey, String region) throws RegionNotSupportedException {
	super(accessKey, secretKey, region);
    }

    /**
     * Create an empty file, it's used so we can write the chunks to the file at
     * the correct positions
     * 
     * @param fileName
     * @param fileSize
     * @param overwrite
     * 
     * @throws FileAlreadyExistsException
     */
    public void CreateEmptyFile(String fileName, long fileSize, boolean overwrite) throws FileAlreadyExistsException {

	File file = new File(fileName);

	if (file.exists() && !overwrite) {
	    throw new FileAlreadyExistsException(fileName);
	}

	File parent = file.getParentFile();

	if (null != parent) {
	    parent.mkdirs();
	}

	RandomAccessFile accessFile = null;

	try {
	    accessFile = new RandomAccessFile(file, "rw");
	    accessFile.setLength(fileSize);
	    accessFile.close();
	} catch (Exception e) {}

    }

    /**
     * Download archive
     * 
     * @param job
     * @param partSize
     * @param overwrite
     * 
     * @return
     * 
     * @throws FileAlreadyExistsException
     * @throws InvalidChecksumException
     */
    public MultipartDownloadStatus DownloadArchive(DownloadJob job, long partSize, boolean overwrite)
	    throws FileAlreadyExistsException, InvalidChecksumException {

	// get the details about the job
	final DescribeJobResult jobResult = DescribeJob(job.getVaultName(), job.getJobId());
	final long archiveSize = jobResult.getArchiveSizeInBytes();
	final String treeHashOriginal = jobResult.getSHA256TreeHash();
	final MultipartDownloadStatus status = new MultipartDownloadStatus();
	final int pieces = (int) Math.ceil(archiveSize / (double) partSize);

	// create an empty archive
	CreateEmptyFile(job.getName(), archiveSize, overwrite);

	// get the threads and download them
	for (int i = 0; i < pieces; i++) {
	    System.out.println(String.format(FORMAT, i + 1, pieces, MultipartStatus.PIECE_START, job.getName(), "Download started"));
	}

	// verify
	System.out.println("Verifying download ...");
	final String treeHashDownloaded = TreeHashGenerator.calculateTreeHash(new File(job.getName()));

	if (!treeHashDownloaded.equals(treeHashOriginal)) {
	    throw new InvalidChecksumException();
	}

	return status;

    }

    /**
     * Download a chunk
     * 
     * @param file
     * @param vaultName
     * @param jobId
     * @param part
     * @param partSize
     * 
     * @return
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    public DownloadPiece DownloadAndWriteChunk(File file, String vaultName, String jobId, int part, long partSize)
	    throws FileNotFoundException, IOException {
	return DownloadAndWriteChunk(file, vaultName, jobId, part, partSize, null, null);
    }

    /**
     * Download a chunk
     * 
     * @param file
     * @param vaultName
     * @param jobId
     * @param part
     * @param partSize
     * @param listener
     * @param collector
     * 
     * @return
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    public DownloadPiece DownloadAndWriteChunk(File file, String vaultName, String jobId, int part, long partSize,
	    ProgressListener listener, RequestMetricCollector collector) throws FileNotFoundException, IOException {

	if (!file.exists() || !file.isFile()) {
	    throw new FileNotFoundException(file.getName());
	}

	final DownloadPiece downloadPiece = new DownloadPiece();
	downloadPiece.setPart(part);
	downloadPiece.setStatus(MultipartStatus.PIECE_START);
	downloadPiece.setId(jobId);

	long startRange = part * partSize;
	long endRange = startRange + partSize;

	GetJobOutputRequest request = new GetJobOutputRequest().withVaultName(vaultName)
		.withRange(String.format("bytes=%s-%s", startRange, endRange)).withJobId(jobId);

	if (null != listener) {
	    request.withGeneralProgressListener(listener);
	}

	if (null != collector) {
	    request.withRequestMetricCollector(collector);
	}

	GetJobOutputResult result = client.getJobOutput(request);

	RandomAccessFile accessFile = null;

	BufferedInputStream is = new BufferedInputStream(result.getBody());
	byte[] buffer = new byte[(int) (endRange - startRange + 1)];

	int totalRead = 0;
	while (totalRead < buffer.length) {
	    int bytesRemaining = buffer.length - totalRead;
	    int read = is.read(buffer, totalRead, bytesRemaining);
	    if (read > 0) {
		totalRead = totalRead + read;
	    } else {
		break;
	    }
	}

	downloadPiece.setUploadedChecksum(TreeHashGenerator.calculateTreeHash(new ByteArrayInputStream(buffer)));
	downloadPiece.setCalculatedChecksum(result.getChecksum());

	try {
	    accessFile = new RandomAccessFile(file, "rw");
	    accessFile.seek(startRange);
	    accessFile.write(buffer);
	    accessFile.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}

	if (downloadPiece.getCalculatedChecksum().equals(downloadPiece.getUploadedChecksum())) {
	    downloadPiece.setStatus(MultipartStatus.PIECE_COMPLETE);
	} else {
	    downloadPiece.setStatus(MultipartStatus.PIECE_CHECKSUM_MISMATCH);
	}

	return downloadPiece;
    }

    /**
     * Initiate a download request for an archive
     * 
     * @param vaultName
     * @param archiveId
     * @return
     */
    public InitiateJobResult InitiateDownloadRequest(String vaultName, String archiveId) {
	return InitiateDownloadRequest(vaultName, archiveId, null);
    }

    /**
     * Initiate a download request for an archive
     * 
     * @param vaultName
     * @param archiveId
     * @param snsTopicARN
     * @return
     */
    public InitiateJobResult InitiateDownloadRequest(String vaultName, String archiveId, String snsTopicARN) {

	JobParameters job = new JobParameters().withType("archive-retrieval").withArchiveId(archiveId);

	if (null != snsTopicARN) {
	    job.withSNSTopic(snsTopicARN);
	}

	InitiateJobRequest request = new InitiateJobRequest().withVaultName(vaultName).withJobParameters(job);

	return client.initiateJob(request);

    }
}

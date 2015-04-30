package com.matoski.glacier.pojo.upload;

import com.matoski.glacier.enums.MultipartPieceStatus;
import com.matoski.glacier.pojo.ArchiveState;

/**
 * A state file, used to store the state of the uploading file, we use this to store all the
 * information necessary to continue uploading the file
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class MultipartUploadStatus extends ArchiveState<MultipartPieceStatus> {
}

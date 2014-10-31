package com.matoski.glacier.interfaces;

import com.matoski.glacier.cli.CommandUploadArchive;
import com.matoski.glacier.pojo.UploadPiece;

public interface IUploadPieceHandler {

    /**
     * We have finished uploading the piece
     * 
     * @param part
     * @param piece
     */
    void end(int part, UploadPiece piece);

    /**
     * We have an exception, this will show use which exception happend and to
     * decide if we need to do something, or just cancel the thread
     * 
     * @param e
     */
    void exception(Exception e);

    /**
     * The thread has started, this happens when {@link Runnable#run()} is
     * executed
     */
    void start();

    /**
     * Upload has started, it also tells us which try it is to upload, this is
     * read from {@link CommandUploadArchive#retryFailedUpload}
     * 
     * @param time
     */
    void upload(int time);
}

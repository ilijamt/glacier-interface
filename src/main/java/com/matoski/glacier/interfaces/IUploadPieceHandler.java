package com.matoski.glacier.interfaces;

import com.matoski.glacier.cli.CommandUploadArchive;
import com.matoski.glacier.pojo.Piece;

/**
 * Upload piece handler, used to show the progress output of the archive.
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
@Deprecated
public interface IUploadPieceHandler {

    /**
     * We have finished uploading the piece.
     *
     * @param part  The part that was finished
     * @param piece The details about the uploaded piece
     */
    void end(int part, Piece piece);

    /**
     * We have an exception, this will show use which exception happend and to decide if we need to do
     * something, or just cancel the thread.
     *
     * @param exc There was an exception
     */
    void exception(Exception exc);

    /**
     * The thread has started, this happens when {@link Runnable#run()} is executed.
     */
    void start();

    /**
     * Upload has started, it also tells us which try it is to upload, this is read from
     * {@link CommandUploadArchive#retryFailedUpload}.
     *
     * @param time Which time we try to upload it.
     */
    void upload(int time);
}

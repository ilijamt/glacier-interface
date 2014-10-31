package com.matoski.glacier.util;

import java.io.File;
import java.util.concurrent.Callable;

import com.matoski.glacier.pojo.UploadPiece;

/**
 * A threaded dummy, used for completion
 * 
 * @author ilijamt
 */
public abstract class ThreadAmazonGlacierDummy implements Callable<UploadPiece> {

    protected UploadPiece piece;
    protected int pieces;
    protected File file;

    public ThreadAmazonGlacierDummy(int pieces, File file, UploadPiece piece) {
	this.piece = piece;
	this.file = file;
	this.pieces = pieces;
    }

    abstract public void __call();

    /**
     * {@inheritDoc}
     */
    @Override
    public UploadPiece call() throws Exception {
	__call();
	return piece;
    }

}

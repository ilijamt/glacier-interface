package com.matoski.glacier.util;

import java.io.File;

import com.matoski.glacier.pojo.UploadPiece;

/**
 * A threaded dummy, used for completion
 * 
 * @author ilijamt
 */
public class ThreadAmazonGlacierUploadDummy extends ThreadAmazonGlacierDummy {

    public ThreadAmazonGlacierUploadDummy(int pieces, File file,
	    UploadPiece piece) {
	super(pieces, file, piece);
    }

    @Override
    public void __call() {
	System.out.println(String.format("[%s] Piece already uploaded: %s/%s",
		file, piece.getPart() + 1, pieces));
    }

}

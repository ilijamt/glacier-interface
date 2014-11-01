package com.matoski.glacier.util.upload;

import java.io.File;

import com.matoski.glacier.pojo.UploadPiece;
import com.matoski.glacier.util.ThreadAmazonGlacierDummy;

/**
 * A threaded dummy, used for completion
 * 
 * @author ilijamt
 */
public class ThreadAmazonGlacierUploadDummy extends ThreadAmazonGlacierDummy<UploadPiece> {

    public ThreadAmazonGlacierUploadDummy(int pieces, File file, UploadPiece piece) {
	super(pieces, file, piece);
    }

    @Override
    public void __call() {
	System.out.println(String.format(AmazonGlacierUploadUtil.FORMAT, piece.getPart() + 1, pieces, piece.getStatus(), file,
		"Already uploaded"));
    }

}

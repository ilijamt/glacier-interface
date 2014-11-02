package com.matoski.glacier.util.download;

import java.io.File;

import com.matoski.glacier.pojo.download.DownloadPiece;
import com.matoski.glacier.util.ThreadAmazonGlacierDummy;

/**
 * A threaded dummy, used for completion
 * 
 * @author ilijamt
 */
public class ThreadAmazonGlacierDownloadDummy extends ThreadAmazonGlacierDummy<DownloadPiece> {

    public ThreadAmazonGlacierDownloadDummy(int pieces, File file, DownloadPiece piece) {
	super(pieces, file, piece);
    }

    @Override
    public void __call() {
	System.out.println(String.format(AmazonGlacierDownloadUtil.FORMAT, piece.getPart() + 1, pieces, piece.getStatus(), file,
		"Already downloaded"));
    }

}

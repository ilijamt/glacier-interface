package com.matoski.glacier.util.upload;

import java.io.File;

import com.matoski.glacier.pojo.Piece;
import com.matoski.glacier.util.ThreadAmazonGlacierDummy;

/**
 * A threaded dummy, used for completion
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class ThreadAmazonGlacierUploadDummy extends ThreadAmazonGlacierDummy<Piece> {

  /**
   * Constructor.
   * 
   * @param pieces
   *          How many pieces
   * @param file
   *          What is the filename
   * @param piece
   *          The upload piece data
   */
  public ThreadAmazonGlacierUploadDummy(int pieces, File file, Piece piece) {
    super(pieces, file, piece);
  }

  @Override
  public void process() {
    System.out.println(String.format(AmazonGlacierUploadUtil.FORMAT, piece.getPart() + 1, pieces,
        piece.getStatus(), file, "Already uploaded"));
  }

}

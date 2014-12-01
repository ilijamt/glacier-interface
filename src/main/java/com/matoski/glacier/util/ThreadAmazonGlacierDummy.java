package com.matoski.glacier.util;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * A threaded dummy, used for completion
 * 
 * @author ilijamt
 * @param <T>
 */
public abstract class ThreadAmazonGlacierDummy<T> implements Callable<T> {

  protected T piece;
  protected int pieces;
  protected File file;

  public ThreadAmazonGlacierDummy(int pieces, File file, T piece) {
    this.piece = piece;
    this.file = file;
    this.pieces = pieces;
  }

  abstract public void __call();

  /**
   * {@inheritDoc}
   */
  @Override
  public T call() throws Exception {
    __call();
    return piece;
  }

}

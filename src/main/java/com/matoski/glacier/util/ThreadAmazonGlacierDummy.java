package com.matoski.glacier.util;

import com.matoski.glacier.pojo.Piece;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * A threaded dummy, used for completion.
 *
 * @param <T> The class we use for the thread usually {@link Piece}
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public abstract class ThreadAmazonGlacierDummy<T> implements Callable<T> {

    protected T piece;
    protected int pieces;
    protected File file;

    /**
     * Constructor.
     *
     * @param pieces How many pieces we have
     * @param file   The filename to process
     * @param piece  The piece we process
     */
    public ThreadAmazonGlacierDummy(int pieces, File file, T piece) {
        this.piece = piece;
        this.file = file;
        this.pieces = pieces;
    }

    /**
     * Override call that is used to process the dummy.
     */
    public abstract void process();

    @Override
    public T call() throws Exception {
        process();
        return piece;
    }

}

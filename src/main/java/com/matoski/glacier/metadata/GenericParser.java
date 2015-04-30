package com.matoski.glacier.metadata;

import com.matoski.glacier.enums.Metadata;
import com.matoski.glacier.interfaces.IMetadata;

/**
 * Contains some generic abstraction of a metadata parser, all the metadata parsers should extend
 * from this one.
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public abstract class GenericParser implements IMetadata {

    /**
     * Metadata data to which the parser belongs to.
     */
    private transient Metadata metadata;

    /**
     * Constructor.
     *
     * @param metadata Which metadata is this parser for
     */
    public GenericParser(Metadata metadata) {
        this.metadata = metadata;
    }

    /**
     * Get the metadata.
     *
     * @return The metadata
     */
    public Metadata getMetadata() {
        return metadata;
    }

    /**
     * Is this the type of the parser.
     *
     * @param metadata Metadata to check for
     * @return true if this is the metadata for this parser
     */
    public boolean type(Metadata metadata) {
        return this.metadata == metadata;
    }

    @Override
    public boolean verify(String data) {
        return false;
    }

}

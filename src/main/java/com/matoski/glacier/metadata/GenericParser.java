package com.matoski.glacier.metadata;

import com.matoski.glacier.enums.Metadata;
import com.matoski.glacier.interfaces.IMetadata;

public abstract class GenericParser implements IMetadata {

    /**
     * Metadata data to which the parser belongs to,
     */
    private transient Metadata metadata;

    /**
     * Constructor
     * 
     * @param metadata
     */
    public GenericParser(Metadata metadata) {
	this.metadata = metadata;
    }

    /**
     * Get the metadata
     * 
     * @return
     */
    public Metadata getMetadata() {
	return metadata;
    }

    /**
     * Is this the type of the parser
     * 
     * @param metadata
     * @return
     */
    public boolean type(Metadata metadata) {
	return this.metadata == metadata;
    }

    @Override
    public boolean verify(String data) {
	return false;
    }

}

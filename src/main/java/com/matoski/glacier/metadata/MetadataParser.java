package com.matoski.glacier.metadata;

import com.matoski.glacier.interfaces.IMetadata;
import com.matoski.glacier.pojo.GlacierInventory;
import com.matoski.glacier.pojo.Journal;

public abstract class MetadataParser implements IMetadata {

    protected Journal journal;

    public MetadataParser() {
	parse();
    }

    public MetadataParser(GlacierInventory inventory) {
	parse();
    }

    @Override
    public void parse() {

    }

    @Override
    public void store(String filename) {

    }
}

package com.matoski.glacier.metadata;

import com.matoski.glacier.pojo.GlacierInventory;

public class MT_AWS_GLACIER_B extends MetadataParser {

    public MT_AWS_GLACIER_B() {
	super();
    };

    public MT_AWS_GLACIER_B(GlacierInventory inventory) {
	super(inventory);
    }

}
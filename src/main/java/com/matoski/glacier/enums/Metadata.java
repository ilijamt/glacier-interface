package com.matoski.glacier.enums;

public enum Metadata {

    FAST_GLACIER_V2("fgv2"), MT_AWS_GLACIER_B("mt2");

    private String propertyName;

    Metadata(String propName) {
	this.propertyName = propName;
    }

    public String getPropertyName() {
	return propertyName;
    }

    public static Metadata from(String x) {
	for (Metadata currentType : Metadata.values()) {
	    if (x.equals(currentType.getPropertyName())) {
		return currentType;
	    }
	}
	// default command
	return Metadata.MT_AWS_GLACIER_B;
    }

}

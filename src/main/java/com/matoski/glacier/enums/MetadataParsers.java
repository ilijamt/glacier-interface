package com.matoski.glacier.enums;

public enum MetadataParsers {

    MT_AWS_GLACIER_B("mt2");

    private String propertyName;

    MetadataParsers(String propName) {
	this.propertyName = propName;
    }

    public String getPropertyName() {
	return propertyName;
    }

    public static MetadataParsers from(String x) {
	for (MetadataParsers currentType : MetadataParsers.values()) {
	    if (x.equals(currentType.getPropertyName())) {
		return currentType;
	    }
	}
	// default command
	return MetadataParsers.MT_AWS_GLACIER_B;
    }

}

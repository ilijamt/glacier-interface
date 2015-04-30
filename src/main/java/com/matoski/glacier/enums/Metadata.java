package com.matoski.glacier.enums;

/**
 * Contains a list of metadatas available in the application.
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public enum Metadata {

    /**
     * Fast glacier metadata, parses the data from the application.
     *
     * @see <a href="http://fastglacier.com/">FastGlacier</a>
     */
    FAST_GLACIER_V2("fgv2"),

    /**
     * mt-aws-glacier type B metadata
     *
     * @see <a href="https://github.com/vsespb/mt-aws-glacier">mt-aws-glacier</a>
     */
    MT_AWS_GLACIER_B("mt2");

    /**
     * The string representation for the metadata.
     */
    private String propertyName;

    /**
     * Constructor.
     *
     * @param propName String representation for the metadata
     */
    Metadata(String propName) {
        this.propertyName = propName;
    }

    /**
     * Get the metadata based on the metadata string
     *
     * @param metadataString The metadata string.
     * @return The metadata associated with the string
     */
    public static Metadata from(String metadataString) {
        for (Metadata currentType : Metadata.values()) {
            if (metadataString.equals(currentType.getPropertyName())) {
                return currentType;
            }
        }
        // default command
        return Metadata.MT_AWS_GLACIER_B;
    }

    /**
     * Gets the string representation for metadata.
     *
     * @return {@link #propertyName}
     */
    public String getPropertyName() {
        return propertyName;
    }

}

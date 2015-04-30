package com.matoski.glacier.errors;

import com.matoski.glacier.util.AmazonGlacierBaseUtil;

/**
 * Thrown where there are too many parts to upload, amazon glacier is limited to
 * {@link AmazonGlacierBaseUtil#MAXIMUM_UPLOAD_PARTS}
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class UploadTooManyPartsException extends Exception {

    /**
     * Serial version ID.
     */
    private static final long serialVersionUID = -4095924865953917183L;

}

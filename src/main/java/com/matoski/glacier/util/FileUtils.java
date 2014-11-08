package com.matoski.glacier.util;

/**
 * Generic file utils
 * 
 * @author ilijamt
 *
 */
public class FileUtils {

    /**
     * Convert the bytes to human readable in SI format
     * 
     * @param bytes
     * @return
     */
    public static String humanReadableByteCount(long bytes) {
	return humanReadableByteCount(bytes, false);
    }

    /**
     * Convert the bytes to human readable
     * 
     * @param bytes
     * @param si
     * @return
     */
    public static String humanReadableByteCount(long bytes, boolean si) {
	int unit = si ? 1000 : 1024;
	if (bytes < unit) return bytes + " B";
	int exp = (int) (Math.log(bytes) / Math.log(unit));
	String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
	return String.format("%.2f %sB", bytes / Math.pow(unit, exp), pre);
    }

}

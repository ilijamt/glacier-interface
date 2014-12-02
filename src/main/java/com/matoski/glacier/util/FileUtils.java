package com.matoski.glacier.util;

import com.matoski.glacier.Constants;

/**
 * Generic file utils
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 *
 */
public class FileUtils {

  /**
   * Convert the bytes to human readable in SI format.
   * 
   * @param bytes
   *          Bytes to convert
   *          
   * @return Human readable output in SI format.
   */
  public static String humanReadableByteCount(long bytes) {
    return humanReadableByteCount(bytes, false, Constants.DEFAULT_HUMAN_READABLE_DIGITS);
  }

  /**
   * Convert the bytes to human readable.
   * 
   * @param bytes
   *          Bytes to convert
   * @param si
   *          Should I use SI unit
   * @param digits
   *          How many digits to round up to
   * 
   * @return Human readable output in the specified format
   */
  public static String humanReadableByteCount(long bytes, boolean si, int digits) {
    int unit = si ? 1000 : 1024;
    if (bytes < unit) {
      return bytes + " B";
    }
    int exp = (int) (Math.log(bytes) / Math.log(unit));
    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
    return String.format("%." + String.valueOf(digits) + "f %sB", bytes / Math.pow(unit, exp), pre);
  }

}

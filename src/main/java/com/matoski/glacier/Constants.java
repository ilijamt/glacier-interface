package com.matoski.glacier;

/**
 * Default constants for the application
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 *
 */
public class Constants {

  /** Application version. */
  public static final String VERSION = "0.3.4";

  /** Human readable digits, used when displaying data. */
  public static final Integer DEFAULT_HUMAN_READABLE_DIGITS = 2;

  /** How much time should pass in seconds between checking if we should continue. */
  public static final Integer WAIT_TIME_THREAD_CHECK = 1;

  /** How long to wait in milliseconds before timing out a fetch from the pool. */
  public static final Integer WAIT_FETCH_OBJECT_FROM_POOL = 10;

  /** How long is the upload valid for, this is dependent on Amazon glacier. */
  public static final String MAX_VALID_UPLOAD_TIME = "+1 day";

  /** Default part size, used for upload and download. */
  public static final Integer DEFAULT_PART_SIZE = 8;

  /** How many times to retry failed upload. */
  public static final Integer DEFAULT_RETRY_FAILED_UPLOAD = 2;

  /** How many threads to run at the same time, for upload and download. */
  public static final Integer DEFAULT_CONCURRENT_THREADS = 2;

  /** Parser: mt-aws-glacier. */
  public static final String PARSER_MT_AWS_GLACIER = "mt2";

  /** Parser: FastGlacier. */
  public static final String PARSER_FAST_GLACIER = "fgv2";

  /** Default parser. */
  public static final String DEFAULT_PARSER_METADATA = PARSER_MT_AWS_GLACIER;

}

package com.matoski.glacier;

public class Constants {

    final public static Integer WAIT_TIME_THREAD_CHECK = 1;
    final public static Integer WAIT_FETCH_OBJECT_FROM_POOL = 10;
    final public static String MAX_VALID_UPLOAD_TIME = "+1 day";
    final public static Integer DEFAULT_PART_SIZE = 1;

    final public static Integer DEFAULT_RETRY_FAILED_UPLOAD = 2;
    final public static Integer DEFAULT_CONCURRENT_THREADS = 1;

    final public static String PARSER_MT_AWS_GLACIER = "mt2";
    final public static String PARSER_FAST_GLACIER = "fgv2";

    final public static String DEFAULT_PARSER_METADATA = PARSER_MT_AWS_GLACIER;

}

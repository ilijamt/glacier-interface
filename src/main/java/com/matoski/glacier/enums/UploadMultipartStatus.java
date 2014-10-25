package com.matoski.glacier.enums;

import org.msgpack.annotation.Message;

@Message
public enum UploadMultipartStatus {
    
    PIECE_START,
    PIECE_COMPLETE,
    PIECE_CHECKSUM_MISMATCH,
    PIECE_ERROR,
    PIECE_INVALID_PART,
    
    START,
    COMPLETE,
    ERROR

}

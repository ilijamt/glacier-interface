package com.matoski.glacier.enums;

public enum UploadMultipartStatus {
    
    PIECE_START,
    PIECE_COMPLETE,
    PIECE_CHECKSUM_MISMATCH,
    PIECE_ERROR,
    PIECE_INVALID_PART,
    
    NOP,
    START,
    IN_PROGRESS,
    COMPLETE,
    ERROR

}

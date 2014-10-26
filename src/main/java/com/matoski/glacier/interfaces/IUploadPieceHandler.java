package com.matoski.glacier.interfaces;

import com.matoski.glacier.pojo.UploadPiece;

public interface IUploadPieceHandler {

    void start();

    void exception(Exception e);

    void end(int part, UploadPiece piece);
}

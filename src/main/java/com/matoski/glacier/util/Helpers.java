package com.matoski.glacier.util;

import java.io.File;
import java.io.IOException;

import com.google.gson.JsonSyntaxException;
import com.matoski.glacier.Constants;
import com.matoski.glacier.pojo.upload.MultipartUploadStatus;

public class Helpers {

  public static boolean verifyIsValidStateFile(String stateFile) {

    Boolean containsExtension = false;
    Boolean fileExists = false;
    String fileName = "";

    containsExtension = stateFile.endsWith(Constants.FILE_STATE_EXTENSION);

    if (!containsExtension) {
      return false;
    }

    fileName = stateFile.replace(Constants.FILE_STATE_EXTENSION, "");
    fileExists = new File(fileName).exists();

    if (fileExists) {
      try {
        MultipartUploadStatus.load(stateFile, MultipartUploadStatus.class);
      } catch (JsonSyntaxException | InstantiationException | IllegalAccessException | IOException e) {
        return false;
      }
    }

    return fileExists;
  }

}

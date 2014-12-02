package com.matoski.glacier.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.GsonBuilder;

/**
 * File writer utilities, used to write the object to a file.
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 *
 */
public class FileWriteUtils {

  /**
   * Store the file as a Json.
   * 
   * @param file
   *          Filename to store in
   * @param obj
   *          Object to convert to Json
   * 
   * @throws IOException
   *           Cannot write the file
   */
  public static void toJson(File file, Object obj) throws IOException {

    if (!file.exists()) {
      file.createNewFile();
    }

    FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    bufferedWriter.write(new GsonBuilder().setPrettyPrinting().create().toJson(obj));

    bufferedWriter.close();
    fileWriter.close();

  }

  /**
   * Store the file as a Json.
   * 
   * @param file
   *          Filename to store in
   * @param obj
   *          Object to convert to Json
   * 
   * @throws IOException
   *           Cannot write the file
   */
  public static void toJson(String file, Object obj) throws IOException {
    toJson(new File(file), obj);
  }

}

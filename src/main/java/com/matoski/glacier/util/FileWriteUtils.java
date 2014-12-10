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
   * @return true if we successfully wrote the JSON to file
   * 
   * @throws IOException
   *           Cannot write the file
   */
  public static boolean toJson(File file, Object obj) throws IOException {

    if (!file.exists() && !file.createNewFile()) {
      return false;
    }

    FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    bufferedWriter.write(new GsonBuilder().setPrettyPrinting().create().toJson(obj));

    bufferedWriter.close();
    fileWriter.close();

    return true;
  }

  /**
   * Store the file as a Json.
   * 
   * @param file
   *          Filename to store in
   * @param obj
   *          Object to convert to Json
   * 
   * @return true if we successfully wrote the JSON to file
   * 
   * @throws IOException
   *           Cannot write the file
   */
  public static boolean toJson(String file, Object obj) throws IOException {
    return toJson(new File(file), obj);
  }

}

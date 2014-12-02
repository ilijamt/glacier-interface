package com.matoski.glacier.base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.matoski.glacier.pojo.download.MultipartDownloadStatus;
import com.matoski.glacier.pojo.upload.MultipartUploadStatus;

/**
 * Abstract writable pojo, used to abstract some of the basic and repetable code for writing and
 * reading the POJO to disk into a single class, that can be extended.
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 * 
 * @param <T>
 *          It's either {@link MultipartUploadStatus} or {@link MultipartDownloadStatus}
 *
 */
public class AbstractWritablePojo<T> {

  /**
   * Do we have an upload status or not.
   * 
   * @param file
   *          The file we check if it exists and if it's a file
   * 
   * @return true if the file exists and it's a file
   */
  public static Boolean has(File file) {
    return file.exists() && file.isFile();
  }

  /**
   * Load the file.
   * 
   * @param <T>
   *          It's either {@link MultipartUploadStatus} or {@link MultipartDownloadStatus}
   * 
   * @param file
   *          The file to load
   * @param cls
   *          The POJO class that is in the file
   * 
   * 
   * @return The POJO class
   * 
   * @throws IOException
   *           If we cannot read from the file
   * @throws InstantiationException
   *           Invalid POJO class
   * @throws IllegalAccessException
   *           Invalid POJO class
   * @throws JsonSyntaxException
   *           Invalid JSON
   */
  @SuppressWarnings("unchecked")
  public static <T> T load(File file, Class<T> cls) throws IOException, InstantiationException,
      IllegalAccessException, JsonSyntaxException {

    if (!file.exists()) {
      // no journal, it's an empty one so we just return an empty Journal
      return cls.newInstance();
    }

    String json = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);

    T status = new Gson().fromJson(json, cls);
    ((AbstractWritablePojo<T>) status).setFile(file);

    return status;

  }

  /**
   * Load the file.
   * 
   * @param <T>
   *          It's either {@link MultipartUploadStatus} or {@link MultipartDownloadStatus}
   * 
   * @param file
   *          The file to load
   * @param cls
   *          The POJO class that is in the file
   * 
   * @return The POJO class
   * 
   * @throws IOException
   *           If we cannot read from the file
   * @throws InstantiationException
   *           Invalid POJO class
   * @throws IllegalAccessException
   *           Invalid POJO class
   * @throws JsonSyntaxException
   *           Invalid JSON
   */
  public static <T> T load(String file, Class<T> cls) throws IOException, InstantiationException,
      IllegalAccessException, JsonSyntaxException {
    return load(new File(file), cls);
  }

  /**
   * A dirty flag, used to know if we should write it or not.
   */
  protected transient boolean dirty = false;

  /**
   * The file for the upload state.
   */
  protected transient File file;

  /**
   * The object is no longer dirty.
   */
  public void clearDirty() {
    this.dirty = false;
  }

  /**
   * Set the file for writing.
   * 
   * @param file
   *          The file that we use for writing or reading
   */
  public void setFile(File file) {
    this.file = file;
  }

  /**
   * Get the file that we use for writing or reading.
   * 
   * @return {@link #file}
   */
  public File getFile() {
    return file;
  }

  /**
   * Checks if the object is dirty, everytime you write something the object toggles a flag that
   * says it's dirty, usefull for detecting when there are changes to the object.
   * 
   * @return {@link #dirty}
   */
  public boolean isDirty() {
    return dirty;
  }

  /**
   * Sets the object as dirty.
   */
  public void setDirty() {
    this.dirty = true;
  }

  /**
   * Write the status to file.
   * 
   * @return true if the write was succesufull, false otherwise
   * 
   * @throws IOException
   *           Cannot write to the file
   * @throws NullPointerException
   *           No file defined
   */
  public boolean write() throws NullPointerException, IOException {
    return write(getFile());
  }

  /**
   * Write the status to file.
   * 
   * @param file
   *          The file to which we write
   * 
   * @return true if the write was succesufull, false otherwise
   * 
   * @throws IOException
   *           Cannot write to the file
   * @throws NullPointerException
   *           No file defined
   */
  @SuppressWarnings("unchecked")
  protected boolean write(File file) throws NullPointerException, IOException {

    if (!isDirty()) {
      // no need to write the file as the data has not been updated
      return true;
    }

    if (null == file) {
      throw new NullPointerException();
    }

    if (!file.exists()) {
      file.createNewFile();
    }

    FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    bufferedWriter.write(new GsonBuilder().setPrettyPrinting().create().toJson((T) this));

    bufferedWriter.close();
    fileWriter.close();

    clearDirty();

    return true;
  }
}

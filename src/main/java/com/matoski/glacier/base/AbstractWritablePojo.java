package com.matoski.glacier.base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matoski.glacier.pojo.Config;

/**
 * Abstract writable pojo
 * 
 * @author ilijamt
 * @param <T>
 *
 */
public abstract class AbstractWritablePojo<T> {

    /**
     * Create a file based on the file, we use this to keep the state upload
     * 
     * @param file
     * @return
     */
    public static File generateFile(File file) {

	File status = new File(file.getAbsolutePath() + ".state");

	return status;

    }

    /**
     * Do we have an upload status or not ?
     * 
     * @param file
     * @return
     */
    public static Boolean has(File file) {
	file = generateFile(file);

	return file.exists() && file.isFile();
    }

    /**
     * Load the file
     * 
     * @param file
     * @param cls
     * 
     * @return
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    public static <T> T load(File file, Class<T> cls) throws IOException, InstantiationException, IllegalAccessException {

	file = generateFile(file);

	if (!file.exists()) {
	    // no journal, it's an empty one so we just return an empty Journal
	    return cls.newInstance();
	}

	String json = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);

	T status = new Gson().fromJson(json, cls);
	((AbstractWritablePojo<T>) status).setFile(file, true);

	return status;

    }

    /**
     * Load the file
     * 
     * @param <T>
     * 
     * @param file
     * 
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T load(String file, Class<T> cls) throws IOException, InstantiationException, IllegalAccessException {
	return load(new File(Config.getInstance().getDirectory(), file), cls);
    }

    /**
     * A dirty flag, used to know if we should write it or not?
     */
    protected transient boolean dirty = false;

    /**
     * The file for the upload state
     */
    protected transient File file;

    /**
     * The object is no longer dirty
     */
    public void clearDirty() {
	this.dirty = false;
    }

    /**
     * Get the file
     * 
     * @return
     */
    public File getFile() {
	return file;
    }

    /**
     * @return the dirty
     */
    public boolean isDirty() {
	return dirty;
    }

    /**
     * Sets the object as dirty
     */
    public void setDirty() {
	this.dirty = true;
    };

    /**
     * Set the file
     * 
     * @param file
     */
    public void setFile(File file) {
	setFile(file, false);
    }

    /**
     * Set the file
     * 
     * @param file
     * @param doNotGenerate
     */
    public void setFile(File file, Boolean doNotGenerate) {
	this.file = doNotGenerate ? file : generateFile(file);
    }

    /**
     * Write the status to file
     * 
     * @return
     * @throws IOException
     * @throws NullPointerException
     */
    public boolean write() throws NullPointerException, IOException {
	return write(getFile());
    }

    /**
     * Write the status to file
     * 
     * @param file
     * @return
     * @throws IOException
     * @throws NullPointerException
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

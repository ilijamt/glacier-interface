package com.matoski.glacier.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.GsonBuilder;

public class FileWriteUtils {

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

    public static void toJson(String file, Object obj) throws IOException {
	toJson(new File(file), obj);
    }

}

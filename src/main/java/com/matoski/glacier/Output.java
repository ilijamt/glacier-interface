package com.matoski.glacier;

import com.google.gson.GsonBuilder;

/**
 * Output class, used to write it to the console.
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 *
 */
public class Output {

  /**
   * Write the object as json to {@link System#out}
   * 
   * @param obj
   */
  public static void toJson(Object obj) {
    System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(obj));
  }

}

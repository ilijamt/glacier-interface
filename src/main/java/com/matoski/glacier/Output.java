package com.matoski.glacier;

import com.google.gson.GsonBuilder;

public class Output {

    public static void toJson(Object obj) {
	System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(obj));
    }

}

package com.test.earthquakemonitor.util;

import com.google.gson.Gson;

public class GsonUtil {

	public static String objectToGsonString(Object object) {
		Gson gson = new Gson();
		String json = gson.toJson(object);
		return json;
	}
	
	public static Object gsonStringToObject(String string, Object object){
		Gson gson = new Gson();
		Object obj = gson.fromJson(string, object.getClass());
		return obj;
	}
}

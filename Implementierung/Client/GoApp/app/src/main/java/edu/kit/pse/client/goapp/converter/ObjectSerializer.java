package edu.kit.pse.client.goapp.converter;

import com.google.gson.Gson;

public class ObjectSerializer<T> {

	
	public String serialize(T object) {
		Gson gson = new Gson();
		String jsonString = gson.toJson(object);
		return jsonString;
	}
	
}

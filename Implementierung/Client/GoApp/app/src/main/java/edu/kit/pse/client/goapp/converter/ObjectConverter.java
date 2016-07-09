package edu.kit.pse.client.goapp.converter;
  
import com.google.gson.Gson;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import java.lang.reflect.Type;
import java.util.List;

import edu.kit.pse.client.goapp.datamodels.Event;
import edu.kit.pse.client.goapp.datamodels.Meeting;
import edu.kit.pse.client.goapp.datamodels.Tour;

public class ObjectConverter<T> {

	
		public String serialize(T object, Class<T> classType) {
		RuntimeTypeAdapterFactory<Meeting> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory.of(Meeting.class)
				.registerSubtype(Event.class).registerSubtype(Tour.class);
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
		String json = gson.toJson(object, classType);
		return json;
	}

	public T deserialize(String json, Class<T> classType) {
		RuntimeTypeAdapterFactory<Meeting> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory.of(Meeting.class)
				.registerSubtype(Event.class).registerSubtype(Tour.class);
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();

		return gson.fromJson(json, classType);
	}
	
}

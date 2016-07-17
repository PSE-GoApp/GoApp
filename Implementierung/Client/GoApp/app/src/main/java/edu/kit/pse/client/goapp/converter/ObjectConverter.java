package edu.kit.pse.client.goapp.converter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import java.util.ArrayList;
import java.util.Arrays;
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
  private int cutFirst(String s)
  {
	  int index = 0;
	  boolean abort = false;
    int lb = 0;
	  int rb= 0;
while(index < s.length() && (!abort))
{
	if(s.charAt(index) == '{')
	{
	lb ++;
	}
	else if (s.charAt(index) == '}')
	{
		rb ++;
	}
   if(lb > 0 && rb == lb){
	   abort = true;
   }
	index ++;
}
	  return  index;
  }
	public <T> List<T> deserializeList(String json,Class<T> classType) {
		try {
		    List<T> list = new ArrayList<>();
			json = json.substring(1,json.length());
			json = json.substring(0,json.length()-1);
		   ObjectConverter<T> converter = new ObjectConverter<>();
			int index = 0;
			while(true)
			{
				index = cutFirst(json);
				if(index == 0) break;
				String part = json.substring(0,index);
				list.add(converter.deserialize(part,classType));
				if(index+1 >= json.length())
				break;
				json = json.substring(index+1,json.length());

			}

			return list;
		}
		catch (Exception e)
		{
			Log.e(e.getMessage(),e.getMessage());
			return null;
		}
	}
}

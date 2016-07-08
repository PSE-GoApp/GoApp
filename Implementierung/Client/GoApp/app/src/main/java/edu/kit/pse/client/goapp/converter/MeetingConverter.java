package edu.kit.pse.client.goapp.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import edu.kit.pse.client.goapp.datamodels.Meeting;

/**
 * Implements the Inetrface Converter<Meeting> and converts JSON-Strings to Meetingss
 * Created by paula on 29.06.16.
 */
public class MeetingConverter implements Converter<Meeting> {

    private Gson gson = new GsonBuilder().create();

    @Override
    public Meeting deserialize(String jsonString) {
		  RuntimeTypeAdapterFactory<Meeting> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
		    	    .of(Meeting.class, "type")
		    	    .registerSubtype(Event.class, "event")
		    	    .registerSubtype(Tour.class, "tour");
		    	     Gson gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create(); 
					 Type listType = new TypeToken<Meeting>(){}.getType();
        return gson.fromJson(jsonString, listType);
    }

    @Override
    public List<Meeting> deserializeList(String jsonString) {
			    RuntimeTypeAdapterFactory<Meeting> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
		    	    .of(Meeting.class, "type")
		    	    .registerSubtype(Event.class, "event")
		    	    .registerSubtype(Tour.class, "tour");
		    	     Gson gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create(); 
					 Type listType = new TypeToken<List<Meeting>>(){}.getType();
        return  gson.fromJson(jsonString, listType);
    }
}

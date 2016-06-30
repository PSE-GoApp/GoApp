package main.java.edu.kit.pse.client.goapp.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import main.java.edu.kit.pse.client.goapp.datamodels.Meeting;

/**
 * Implements the Inetrface Converter<Meeting> and converts JSON-Strings to Meetingss
 * Created by paula on 29.06.16.
 */
public class MeetingConverter implements Converter<Meeting> {

    private Gson gson = new GsonBuilder().create();

    @Override
    public Meeting deserialize(String jsonString) {
        return gson.fromJson(jsonString, Meeting.class);
    }

    @Override
    public List<Meeting> deserializeList(String jsonString) {
        return gson.fromJson(jsonString,new TypeToken<List<Meeting>>(){}.getType());
    }
}

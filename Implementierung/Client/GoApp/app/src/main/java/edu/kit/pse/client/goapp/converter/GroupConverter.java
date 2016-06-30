package main.java.edu.kit.pse.client.goapp.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import main.java.edu.kit.pse.client.goapp.datamodels.Group;

/**
 * Implements the Inetrface Converter<Group> and converts JSON-Strings to Groups
 * Created by paula on 29.06.16.
 */
public class GroupConverter implements Converter<Group> {

    private Gson gson = new GsonBuilder().create();

    @Override
    public Group deserialize(String jsonString) {
        return gson.fromJson(jsonString, Group.class);
    }

    @Override
    public List<Group> deserializeList(String jsonString) {
        return gson.fromJson(jsonString,new TypeToken<List<Group>>(){}.getType());
    }
}

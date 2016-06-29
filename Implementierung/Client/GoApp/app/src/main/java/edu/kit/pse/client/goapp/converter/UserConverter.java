package main.java.edu.kit.pse.client.goapp.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import main.java.edu.kit.pse.client.goapp.datamodels.User;

/**
 * Implements the Inetrface Converter<User> and converts JSON-Strings to Users
 * Created by paula on 29.06.16.
 */
public class UserConverter implements Converter<User> {

    private Gson gson = new GsonBuilder().create();

    @Override
    public User deserialize(String jsonString) {
        return gson.fromJson(jsonString, User.class);
    }

    @Override
    public List<User> deserializeList(String jsonString) {
        return gson.fromJson(jsonString,new TypeToken<List<User>>(){}.getType());
    }
}

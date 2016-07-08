package edu.kit.pse.client.goapp.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import edu.kit.pse.client.goapp.datamodels.GPS;

/**
 * Implements the Inetrface Converter<GPS> and converts JSON-Strings to GPS
 * Created by paula on 29.06.16.
 */
public class GPS_Converter implements Converter<GPS> {
    private Gson gson = new GsonBuilder().create();

    @Override
    public GPS deserialize(String jsonString) {
        return gson.fromJson(jsonString, GPS.class);
    }

    @Override
    public List<GPS> deserializeList(String jsonString) {
        return gson.fromJson(jsonString,new TypeToken<List<GPS>>(){}.getType());
    }
    

}

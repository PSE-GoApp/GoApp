package main.java.edu.kit.pse.client.goapp.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import main.java.edu.kit.pse.client.goapp.datamodels.Group;
import main.java.edu.kit.pse.client.goapp.datamodels.Participant;

/**
 * Implements the Inetrface Converter<Participant> and converts JSON-Strings to Participants
 * Created by paula on 29.06.16.
 */
public class ParticipantConverter implements Converter<Participant> {
    private Gson gson = new GsonBuilder().create();

    @Override
    public Participant deserialize(String jsonString) {
        return gson.fromJson(jsonString, Participant.class);
    }

    @Override
    public List<Participant> deserializeList(String jsonString) {
        return gson.fromJson(jsonString,new TypeToken<List<Participant>>(){}.getType());
    }
}

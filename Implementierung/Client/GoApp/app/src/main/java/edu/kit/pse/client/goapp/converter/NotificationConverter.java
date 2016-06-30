package edu.kit.pse.client.goapp.converter;

import java.util.List;
import edu.kit.pse.client.goapp.datamodels.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


/**
 * Implements the Inetrface Converter<Notification> and converts JSON-Strings to Notifications
 * Created by paula on 29.06.16.
 */
public class NotificationConverter implements Converter<Notification> {

    private Gson gson  = new GsonBuilder().create();

    @Override
    public Notification deserialize(String jsonString) {
        return gson.fromJson(jsonString,Notification.class);
    }

    @Override
    public List<Notification> deserializeList(String jsonString) {
        return gson.fromJson(jsonString,new TypeToken<List<Notification>>(){}.getType());
    }
}

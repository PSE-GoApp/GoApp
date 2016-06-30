package main.java.edu.kit.pse.client.goapp.converter;

import java.util.List;

import main.java.edu.kit.pse.client.goapp.datamodels.Group;
import main.java.edu.kit.pse.client.goapp.datamodels.User;

/**
 * Implements the Inetrface Converter<User> and converts JSON-Strings to Notifications
 * Created by paula on 29.06.16.
 */
public class GroupMemberConverter implements Converter<User>{
    @Override
    public User deserialize(String jsonString) {
        return null;
    }

    @Override
    public List<User> deserializeList(String jsonString) {
        return null;
    }
}

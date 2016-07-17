package edu.kit.pse.goapp.client.goapp.converterTest;

import org.junit.Assert.*;
import org.junit.Test;

import edu.kit.pse.client.goapp.converter.ObjectConverter;
import edu.kit.pse.client.goapp.datamodels.GPS;
import edu.kit.pse.client.goapp.datamodels.Meeting;
import edu.kit.pse.client.goapp.datamodels.MeetingConfirmation;
import edu.kit.pse.client.goapp.datamodels.Participant;
import edu.kit.pse.client.goapp.datamodels.Tour;
import edu.kit.pse.client.goapp.datamodels.User;

import static org.junit.Assert.*;

/**
 * Created by Ta on 16.07.2016.
 */
public class ConverterTest {
    @Test
    public void testObjectToJson() {
        User user = new User(1,"testUser");
        GPS gps = new GPS(5,6,7);
        Participant participant = new Participant(2,3,user, MeetingConfirmation.CONFIRMED);
        Meeting meeting = new Tour(4,"testTour",gps,8,9,participant);

        ObjectConverter<Meeting> converter = new ObjectConverter<>();
        String json = converter.serialize(meeting,Meeting.class);
        String expected = "{\"type\":\"Tour\",\"center\":{\""
                + "participants\":[],\"place\":{\"x\":5.0,\"y\":6.0,\"z\":7.0}},\"meetingId\""
                + ":4,\"name\":\"testTour\",\"place\":{\"x\":5.0,\"y\":6.0,\"z\":7.0},"
                + "\"timestamp\":8,\"duration\":9,\"creator"
                + "\":{\"participantId\":2,\"meetingId\":3,\"user\":{\"userId\":1,\"name\":\"testUser\",\"notificationEnabled\":false,\"meetings\":[],\"groups\":[]},\"confirmation\":"
                +"\"CONFIRMED\"},\"participants\":[]}";
        assertEquals(expected, json);
    }

    @Test
    public void testJsonToObject() {
        String json = "{\"type\":\"Tour\",\"center\":{\""
                + "participants\":[],\"place\":{\"x\":5.0,\"y\":6.0,\"z\":7.0}},\"meetingId\""
                + ":4,\"name\":\"testTour\",\"place\":{\"x\":5.0,\"y\":6.0,\"z\":7.0},"
                + "\"timestamp\":8,\"duration\":9,\"creator"
                + "\":{\"participantId\":2,\"meetingId\":3,\"user\":{\"userId\":1,\"name\":\"testUser\",\"notificationEnabled\":false,\"meetings\":[],\"groups\":[]},\"confirmation\":"
                +"\"CONFIRMED\"},\"participants\":[]}";
        User user = new User(1,"testUser");
        GPS gps = new GPS(5,6,7);
        Participant participant = new Participant(2,3,user, MeetingConfirmation.CONFIRMED);
        Meeting expected = new Tour(4,"testTour",gps,8,9,participant);

        ObjectConverter<Meeting> converter = new ObjectConverter<>();
        Object meeting = converter.deserialize(json,Meeting.class);
        assertEquals(expected, meeting);
    }

    @Test
    public void testDoubleDirection() {
        User user = new User(1,"testUser");
        GPS gps = new GPS(5,6,7);
        Participant participant = new Participant(2,3,user, MeetingConfirmation.CONFIRMED);
        Meeting expected = new Tour(4,"testTour",gps,8,9,participant);

        ObjectConverter<Meeting> converter = new ObjectConverter<>();
        String json = converter.serialize(expected,Meeting.class);
        Meeting meeting = converter.deserialize(json,Meeting.class);
        assertEquals(expected, meeting);
    }
}

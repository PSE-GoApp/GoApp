package edu.kit.pse.goapp.client.goapp.serviceTest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import org.junit.Before;
import org.junit.Test;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.ServiceResultReceiver;
import edu.kit.pse.client.goapp.converter.ObjectConverter;
import edu.kit.pse.client.goapp.datamodels.GPS;
import edu.kit.pse.client.goapp.datamodels.Group;
import edu.kit.pse.client.goapp.datamodels.Meeting;
import edu.kit.pse.client.goapp.datamodels.MeetingConfirmation;
import edu.kit.pse.client.goapp.datamodels.Participant;
import edu.kit.pse.client.goapp.datamodels.Tour;
import edu.kit.pse.client.goapp.datamodels.User;
import edu.kit.pse.client.goapp.service.MeetingService;

/**
 * Created by paula on 18.08.16.
 */
public class MeetingServiceTest extends AppCompatActivity implements ServiceResultReceiver.Receiver{

    Intent intent;
    int resultCode;
    Bundle resultBundle;
    ServiceResultReceiver receiver;



    @Before
    public void initializeTest() {
        intent = new Intent(this, MeetingService.class);
        receiver = new ServiceResultReceiver(new Handler());
        receiver.setReceiver(this);
        intent.putExtra(CommunicationKeys.RECEICER, receiver);
        resultCode = -1;
    }

    @Test
    public void putTest() {
        GPS testGPS = new GPS(1,1,1);
        User testUser = new User(1,"test");
        Participant part = new Participant(1,1,testUser, MeetingConfirmation.PENDING);
        Meeting testMeet = new Tour(1,"testTour",testGPS,1,1,part);
        ObjectConverter<Meeting> conv = new ObjectConverter<>();
        String jsonMeet = conv.serialize(testMeet,Meeting.class);
        intent.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.PUT);
        intent.putExtra(CommunicationKeys.MEETING,jsonMeet);
        startService(intent);
    }

    @Test
    public void getTest() {
        intent.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.GET);
        intent.putExtra(CommunicationKeys.MEETING_ID,1);
        startService(intent);
    }

    @Test
    public void deleteTest() {
        intent.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.DELETE);
        intent.putExtra(CommunicationKeys.MEETING_ID,1);
        startService(intent);
    }

    @Test
    public void postTest() {
        GPS testGPS = new GPS(1,1,1);
        User testUser = new User(1,"test");
        Participant part = new Participant(1,1,testUser, MeetingConfirmation.PENDING);
        Meeting testMeet = new Tour(1,"testTour",testGPS,1,1,part);
        ObjectConverter<Meeting> conv = new ObjectConverter<>();
        String jsonMeet = conv.serialize(testMeet,Meeting.class);
        intent.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.POST);
        intent.putExtra(CommunicationKeys.MEETING,jsonMeet);
        startService(intent);
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        this.resultCode = resultCode;
        this.resultBundle = resultData;
    }
}

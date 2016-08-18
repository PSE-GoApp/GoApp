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
import edu.kit.pse.client.goapp.datamodels.Group;
import edu.kit.pse.client.goapp.datamodels.MeetingConfirmation;
import edu.kit.pse.client.goapp.datamodels.Participant;
import edu.kit.pse.client.goapp.datamodels.User;
import edu.kit.pse.client.goapp.service.MeetingParticipantManagementService;

/**
 * Created by paula on 18.08.16.
 */
public class MeetingParticipantManagementServiceTest extends AppCompatActivity implements ServiceResultReceiver.Receiver{

    Intent intent;
    int resultCode;
    Bundle resultBundle;
    ServiceResultReceiver receiver;



    @Before
    public void initializeTest() {
        intent = new Intent(this, MeetingParticipantManagementService.class);
        receiver = new ServiceResultReceiver(new Handler());
        receiver.setReceiver(this);
        intent.putExtra(CommunicationKeys.RECEICER, receiver);
        resultCode = -1;
    }

    @Test
    public void getTest() {
        intent.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.GET);
        intent.putExtra(CommunicationKeys.MEETING_ID,1);
        startService(intent);
    }

    @Test
    public void putTest() {
        User testUser = new User(1,"test");
        Participant testP = new Participant(1,1,testUser, MeetingConfirmation.PENDING);
        ObjectConverter<Participant> conv = new ObjectConverter<>();
        String jsonPart = conv.serialize(testP,Participant.class);
        intent.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.PUT);
        intent.putExtra(CommunicationKeys.PARTICIPANT,jsonPart);
        startService(intent);
    }

    @Test
    public void postTest() {
        User testUser = new User(1,"test");
        Participant testP = new Participant(1,1,testUser, MeetingConfirmation.PENDING);
        ObjectConverter<Participant> conv = new ObjectConverter<>();
        String jsonPart = conv.serialize(testP,Participant.class);
        intent.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.POST);
        intent.putExtra(CommunicationKeys.PARTICIPANT,jsonPart);
        startService(intent);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        this.resultCode = resultCode;
        this.resultBundle = resultData;
    }


}

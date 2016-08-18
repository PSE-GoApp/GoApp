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
import edu.kit.pse.client.goapp.datamodels.User;
import edu.kit.pse.client.goapp.service.GPS_Service;

/**
 * Created by paula on 18.08.16.
 */
public class GroupUserManagementServiceTest extends AppCompatActivity implements ServiceResultReceiver.Receiver{

        Intent intent;
        int resultCode;
        Bundle resultBundle;
        ServiceResultReceiver receiver;

    @Before
    public void initializeTest() {
        intent = new Intent(this, GPS_Service.class);
        receiver = new ServiceResultReceiver(new Handler());
        receiver.setReceiver(this);
        intent.putExtra(CommunicationKeys.RECEICER, receiver);
        resultCode = -1;
    }

    @Test
    public void getTest() {
        intent.putExtra(CommunicationKeys.COMMAND,CommunicationKeys.GET);
        intent.putExtra(CommunicationKeys.GROUP_ID,1);
        startService(intent);
    }

    @Test
    public void postTest() {
        User testUser = new User(1,"test");
        ObjectConverter<User> conv = new ObjectConverter<>();
        String jsonUser = conv.serialize(testUser,User.class);
        intent.putExtra(CommunicationKeys.COMMAND,CommunicationKeys.POST);
        intent.putExtra(CommunicationKeys.USER,jsonUser);
        startService(intent);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        this.resultCode = resultCode;
        this.resultBundle = resultData;

    }

}

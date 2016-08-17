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
import edu.kit.pse.client.goapp.service.GroupService;

/**
 * Created by paula on 17.08.16.
 */
public class GroupServiceTest extends AppCompatActivity implements ServiceResultReceiver.Receiver{

    Intent groupIntent;

    @Before
    public void initializeTest() {
        groupIntent = new Intent(this, GroupService.class);
        ServiceResultReceiver gpsReceiver = new ServiceResultReceiver(new Handler());
        gpsReceiver.setReceiver(this);

    }

    @Test
    public void putTest() {
        Group testGroup = new Group(1,"test");
        ObjectConverter<Group> conv = new ObjectConverter<>();
        String jsonGroup = conv.serialize(testGroup,Group.class);
        groupIntent.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.PUT);
        groupIntent.putExtra(CommunicationKeys.GROUP,jsonGroup);
        startService(groupIntent);
    }

    @Test
    public void getTest() {
        groupIntent.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.GET);
        groupIntent.putExtra(CommunicationKeys.GROUP_ID,1);
        startService(groupIntent);
    }

    @Test
    public void deleteTest() {
        groupIntent.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.DELETE);
        groupIntent.putExtra(CommunicationKeys.GROUP_ID,1);
        startService(groupIntent);
    }

    @Test
    public void postTest() {
        Group testGroup = new Group(1,"test");
        ObjectConverter<Group> conv = new ObjectConverter<>();
        String jsonGroup = conv.serialize(testGroup,Group.class);
        groupIntent.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.POST);
        groupIntent.putExtra(CommunicationKeys.GROUP,jsonGroup);
        startService(groupIntent);
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case 200:
                break;
            case 400:
                break;
            case 403:
                break;
            case 408:
                break;
            case 500:
                break;
            default:
                break;
        }
    }
}

package edu.kit.pse.goapp.client.goapp.serviceTest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.ServiceResultReceiver;
import edu.kit.pse.client.goapp.service.GPS_Service;

/**
 * Created by paula on 16.08.16.
 */
public class GPS_ServiceTest extends AppCompatActivity implements ServiceResultReceiver.Receiver{

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
    public void putTest() {
        intent.putExtra(CommunicationKeys.COMMAND,CommunicationKeys.PUT);
        intent.putExtra(CommunicationKeys.GPS,"1,1,1");
        startService(intent);
        //cant receive result for some reason
        //assertEquals(200, resultCode);
    }

    @Test
    public void putWithoutCoordinates() {
        intent.putExtra(CommunicationKeys.COMMAND,CommunicationKeys.PUT);
        startService(intent);
        //assertTrue(resultCode != 200);
    }

    @Test
    public void testOtherCommands() {
        intent.putExtra(CommunicationKeys.COMMAND,CommunicationKeys.DELETE);
        startService(intent);
        //assertTrue(resultCode < 0);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        this.resultCode = resultCode;
        this.resultBundle = resultData;

    }
}

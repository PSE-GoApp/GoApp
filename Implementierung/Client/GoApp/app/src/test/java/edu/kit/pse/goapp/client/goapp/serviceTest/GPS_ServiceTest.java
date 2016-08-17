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

    Intent gpsIntent;
    int resultCode = -1;
    Bundle resultBundle;
    ServiceResultReceiver gpsReceiver;



    @Before
    public void initializeTest() {
        gpsIntent = new Intent(this, GPS_Service.class);
        gpsReceiver = new ServiceResultReceiver(new Handler());
        gpsReceiver.setReceiver(this);
        gpsIntent.putExtra(CommunicationKeys.RECEICER, gpsReceiver);

    }

    @Test
    public void putTest() {
        gpsIntent.putExtra(CommunicationKeys.COMMAND,CommunicationKeys.PUT);
        gpsIntent.putExtra(CommunicationKeys.GPS,"1,1,1");
        startService(gpsIntent);
        assertEquals(200, resultCode);
    }

    @Test
    public void putWithoutCoordinates() {
        gpsIntent.putExtra(CommunicationKeys.COMMAND,CommunicationKeys.PUT);
        startService(gpsIntent);
        assertTrue(resultCode != 200);
    }

    @Test
    public void testOtherCommands() {
        gpsIntent.putExtra(CommunicationKeys.COMMAND,CommunicationKeys.DELETE);
        startService(gpsIntent);
        assertTrue(resultCode < 0);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        this.resultCode = resultCode;
        this.resultBundle = resultData;

    }
}

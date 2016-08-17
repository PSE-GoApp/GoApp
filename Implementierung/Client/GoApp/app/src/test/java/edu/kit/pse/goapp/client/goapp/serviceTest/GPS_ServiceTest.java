package edu.kit.pse.goapp.client.goapp.serviceTest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.ServiceResultReceiver;
import edu.kit.pse.client.goapp.service.GPS_Service;

/**
 * Created by paula on 17.08.16.
 */
public class GPS_ServiceTest implements ServiceResultReceiver.Receiver{

    Intent gpsIntent;

    @Before
    public void initalizeTest() {
        gpsIntent = new Intent();
        ServiceResultReceiver gpsReceiver = new ServiceResultReceiver(new Handler());
        gpsReceiver.setReceiver(this);

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

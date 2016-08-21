package edu.kit.pse.client.goapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.ResultReceiver;
import android.util.Log;

import org.apache.http.HttpResponse;

import java.io.IOException;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientPut;
import edu.kit.pse.client.goapp.uri_builder.URI_GPS_Builder;

/**
 * Extends the abstract class IntentService and sends GPS-data.
 *
 * Created by Ta on 10.07.2016.
 */
public class GPS_Service extends IntentService {

    /**
     * Constructor. Sets a name of the service which is important for testing.
     */
    public GPS_Service() {
        super("GPS_Service");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GPS_Service(String name) {
        super(name);
    }

    /**
     * GPS_Service's logic intent contains all information about the GPS data. CommunicationKeys are String key values
     *
     * @param intent Intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        String command = intent.getStringExtra(CommunicationKeys.COMMAND);
        Log.d("GPS_Service", "Started");
        switch (command) {
            case CommunicationKeys.GET:
                break;
            case CommunicationKeys.DELETE:
                break;
            case CommunicationKeys.PUT:
                doPut(intent);
                break;
            case CommunicationKeys.POST:
                break;
            default:
                break;
            }
        }

    /**
     * Updates GPS coordinates
     *
     * @param intent Intent
     */
    private void doPut(Intent intent) {
        String GPSAsJsonString = null;
        HttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        GPSAsJsonString = intent.getStringExtra(CommunicationKeys.GPS);

        URI_GPS_Builder uri_gps_builder = new URI_GPS_Builder();

        HttpAppClientPut httpAppClientPut = new HttpAppClientPut();
        httpAppClientPut.setUri(uri_gps_builder.getURI());
        try {
            httpAppClientPut.setBody(GPSAsJsonString);
        } catch (IOException e) {
        }

        try {
            closeableHttpResponse = httpAppClientPut.executeRequest();
        } catch (IOException e) {
        }

        int resultCode = closeableHttpResponse.getStatusLine().getStatusCode();
        Log.e("GPS_Service", "ResultCode: " + resultCode);

        switch (resultCode) {
            case 200:
                break;
            case 400:
                // TODOstatusCode 400 => stopAlarmReceiver
                break;
        }

    }

    /*
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("android.provider.Telephony.SMS_RECEIVED")){
                //action for sms received
            }
            else if(action.equals(android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED)){
                //action for phone state changed
            }
        }
    };
    */
}

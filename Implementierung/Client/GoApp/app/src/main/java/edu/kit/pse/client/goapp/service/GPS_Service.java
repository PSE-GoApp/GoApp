package edu.kit.pse.client.goapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientPut;
import edu.kit.pse.client.goapp.uri_builder.URI_GPS_Builder;
import edu.kit.pse.client.goapp.uri_builder.URI_MeetingBuilder;

/**
 * Created by Ta on 10.07.2016.
 */
public class GPS_Service extends IntentService {

    //Konstruktor gibt den Service ein Namen, der fürs Testen wichtig ist.

    public GPS_Service() {
        super("MeetingService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GPS_Service(String name) {
        super(name);
    }

    //GPS_Service's Logik Intent enthält alle Informationen über die GPS-Daten. CommunicationKeys sind String-Key-Werte.
    @Override
    protected void onHandleIntent(Intent intent) {
        String command = intent.getStringExtra(CommunicationKeys.COMMAND);
        switch (command) {
            case "GET":
                break;
            case "DELETE":
                break;
            case "PUT":
                doPut(intent);
                break;
            case "POST":
                break;
            default:
                break;
            }
        }

    private void doPut(Intent intent) {
        String GPSAsJsonString = null;
        CloseableHttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.PUT);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.GPS);

        GPSAsJsonString = intent.getStringExtra(CommunicationKeys.GPS);

        URI_GPS_Builder uri_gps_builder = new URI_GPS_Builder();

        HttpAppClientPut httpAppClientPut = new HttpAppClientPut();
        httpAppClientPut.setUri(uri_gps_builder.getURI());
        try {
            httpAppClientPut.setBody(GPSAsJsonString);
        } catch (IOException e) {
            //Todo Handle Exception. Maybe the String Extra was null
        }

        try {
            // TODO catch 404 (No Internet and Request Time out)
            closeableHttpResponse = httpAppClientPut.executeRequest();
        } catch (IOException e) {
            // TODO handle Exception Toast? Alert Dialog? sent it to the Activity?
        }

        // send the Bundle and the Status Code from Response
        resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
    }
}

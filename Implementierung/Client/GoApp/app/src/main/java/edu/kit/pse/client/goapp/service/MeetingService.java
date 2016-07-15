package edu.kit.pse.client.goapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientDelete;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientGet;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientPut;
import edu.kit.pse.client.goapp.uri_builder.URI_MeetingBuilder;

/**
 * Extends the abstract class IntentService and manages a meeting.
 *
 * Created by kansei on 09.07.16.
 */
public class MeetingService  extends IntentService {

    /**
     * Constructor. Sets a name of the service which is important for testing.
     */
    public MeetingService() {
        super("MeetingService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MeetingService(String name) {
        super(name);
    }

    /**
     * MeetingService's logic intent contains all information about the Meetings. CommunicationKeys are String key values
     *
     * @param intent Intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        String command = intent.getStringExtra(CommunicationKeys.COMMAND);
        switch (command) {
            case CommunicationKeys.GET:
                doGet(intent);
                break;
            case CommunicationKeys.DELETE:
                doDelete(intent);
                break;
            case CommunicationKeys.PUT:
                doPut(intent);
                break;
            case CommunicationKeys.POST:
                doPost(intent);
                break;
            default:
                break;
        }
    }

    /**
     *Returns all information about a meeting.
     *
     * @param intent Intent
     */
    private void doGet (Intent intent) //throws  IOException
    {
        String jasonString = null;
        CloseableHttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        // den Bundle erhält die Acticity zum schluss und entält Information
        Bundle bundle = new Bundle();
        // hier steht die information was für ein Befehl das war GET,PUT... etc
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.GET);
        // hier weiß die activity von welchem Service das war. Bei UserService steht FROM_USER_SERVICE etc.
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_MEETING_SERVICE);

        // if there no Meeting Id in the Extra returns -1
        int meetingId = intent.getIntExtra(CommunicationKeys.MEETING_ID, -1);

        if (meetingId != -1) {
            URI_MeetingBuilder uri_meetingBuilder = new URI_MeetingBuilder();
            uri_meetingBuilder.addParameter(CommunicationKeys.MEETING_ID, Integer.toString(meetingId));

            HttpAppClientGet httpAppClientGet = new HttpAppClientGet();
            httpAppClientGet.setUri(uri_meetingBuilder.getURI());

            try {
                // TODO catch 404 (No Internet and Request Time out)
                closeableHttpResponse = httpAppClientGet.executeRequest();
            } catch (IOException e) {
                // TODO handle Exception Toast? Alert Dialog? sent it to the Activity?
            }

            // accepted
            try {
                jasonString = EntityUtils.toString(closeableHttpResponse.getEntity());
            } catch (Throwable e) {
                // TODO handle Exception "can not Convert EntitlyUtils to String"
            }

            bundle.putString(CommunicationKeys.MEETING, jasonString);

            // send the Bundle and the Status Code from Response
            resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
        } else {
            // Send a empty result with 500 as StatusCode

            // StatusCode 500 is an unexpected Error. Here no Meeting ID in Intent
            resultReceiver.send(500, bundle);
        }
    }

    /**
     * Deletes a meeting
     *
     * @param intent Intent
     */
    private void doDelete(Intent intent) {
        CloseableHttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.DELETE);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_MEETING_SERVICE);

        // if there no Meeting Id in the Extra returns -1
        int meetingId = intent.getIntExtra(CommunicationKeys.MEETING_ID, -1);

        if (meetingId != -1) {
            URI_MeetingBuilder uri_meetingBuilder = new URI_MeetingBuilder();
            uri_meetingBuilder.addParameter(CommunicationKeys.MEETING_ID, Integer.toString(meetingId));

            HttpAppClientDelete httpAppClientDelete = new HttpAppClientDelete();
            httpAppClientDelete.setUri(uri_meetingBuilder.getURI());

            try {
                // TODO catch 404 (No Internet and Request Time out)
                closeableHttpResponse = httpAppClientDelete.executeRequest();
            } catch (IOException e) {
                // TODO handle Exception Toast? Alert Dialog? sent it to the Activity?
            }

            // send the Bundle and the Status Code from Response
            resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
        } else {
            // Send a empty result with 500 as StatusCode

            // StatusCode 500 is an unexpected Error. Here no Meeting ID in Intent
            resultReceiver.send(500, bundle);
        }

    }

    /**
     * Updates information about the meeting.
     *
     * @param intent
     */
    private void doPut(Intent intent) {
        String meetingAsJsonString = null;
        CloseableHttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.PUT);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_MEETING_SERVICE);

        meetingAsJsonString = intent.getStringExtra(CommunicationKeys.MEETING);

            URI_MeetingBuilder uri_meetingBuilder = new URI_MeetingBuilder();

            HttpAppClientPut httpAppClientPut = new HttpAppClientPut();
            httpAppClientPut.setUri(uri_meetingBuilder.getURI());
        try {
            httpAppClientPut.setBody(meetingAsJsonString);
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

    /**
     * Creates a new meeting.
     *
     * @param intent Intent
     */
    private void doPost(Intent intent) {
        String meetingAsJsonString = null;
        CloseableHttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.POST);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_MEETING_SERVICE);

        meetingAsJsonString = intent.getStringExtra(CommunicationKeys.MEETING);
        /*
        URI_MeetingBuilder uri_meetingBuilder = new URI_MeetingBuilder();

        HttpAppClientPost httpAppClientPost = new HttpAppClientPost();
        httpAppClientPost.setUri(uri_meetingBuilder.getURI());
        try {
            httpAppClientPost.setBody(meetingAsJsonString);
        } catch (IOException e) {
            //Todo Handle Exception. Maybe the String Extra was null
        }

        try {
            // TODO catch 404 (No Internet and Request Time out)
            closeableHttpResponse = httpAppClientPost.executeRequest();
        } catch (IOException e) {
            // TODO handle Exception Toast? Alert Dialog? sent it to the Activity?
        }

        // send the Bundle and the Status Code from Response
        resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
        */

        resultReceiver.send(202, bundle);

    }

}

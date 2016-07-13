package edu.kit.pse.client.goapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientGet;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientPut;
import edu.kit.pse.client.goapp.uri_builder.URI_MeetingParticipantManagementBuilder;

/**
 * Extends the abstract class IntentService and manages the participants of a meeting.
 *
 * Created by Ta on 10.07.2016.
 */


public class MeetingParticipantManagementService extends IntentService {

    /**
     * Constructor. Sets a name of the service which is important for testing.
     */

    public MeetingParticipantManagementService() {
        super("MeetingParticipantManagementService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MeetingParticipantManagementService(String name) {
        super(name);
    }

    /**
     * MeetingParticipantManagementService's logic intent contains all information about the meetingParticipantManagement. CommunicationKeys are String key values
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
               break;
            case CommunicationKeys.PUT:
                // TODO grischa Fragen Enum oder Participant schicken ?
                doPut(intent);
              break;
            case CommunicationKeys.POST:
                break;
            default:
                break;
        }
    }

    /**
     * Updates the meetingParticipants status.
     *
     * @param intent Intent
     */
    private void doPut(Intent intent) {
        String meetingParticipantAsJsonString = null;
        CloseableHttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.PUT);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_MEETING_SERVICE);

        meetingParticipantAsJsonString = intent.getStringExtra(CommunicationKeys.PARTICIPANT);

        URI_MeetingParticipantManagementBuilder uri_meetingParticipantManagementBuilder = new URI_MeetingParticipantManagementBuilder();

        HttpAppClientPut httpAppClientPut = new HttpAppClientPut();
        httpAppClientPut.setUri(uri_meetingParticipantManagementBuilder.getURI());
        try {
            httpAppClientPut.setBody(meetingParticipantAsJsonString);
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
     *Returns a list of MeetingParticipants and their commitment status.
     *
     * @param intent Intent
     */
    private void doGet(Intent intent) {
        // Get Participants from Meeting
        String jasonString = null;
        CloseableHttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.GET);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_MEETING_PARTICIPANT_MANAGEMENT_SERVICE);

        // if there no Meeting Id in the Extra returns -1
        int meetingId = intent.getIntExtra(CommunicationKeys.MEETING_ID, -1);

        if (meetingId != -1) {
            URI_MeetingParticipantManagementBuilder uri_meetingParticipantManagementBuilder = new URI_MeetingParticipantManagementBuilder();
            uri_meetingParticipantManagementBuilder.addParameter(CommunicationKeys.MEETING_ID, Integer.toString(meetingId));

            HttpAppClientGet httpAppClientGet = new HttpAppClientGet();
            httpAppClientGet.setUri(uri_meetingParticipantManagementBuilder.getURI());

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

            bundle.putString(CommunicationKeys.MEETING_PARTICIPANTS, jasonString);

            // send the Bundle and the Status Code from Response
            resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
        } else {
            // Send a empty result with 500 as StatusCode

            // StatusCode 500 is an unexpected Error. Here no Meeting ID in Intent
            resultReceiver.send(500, bundle);
        }
    }
}

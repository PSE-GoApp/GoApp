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
import edu.kit.pse.client.goapp.uri_builder.URI_MeetingBuilder;
import edu.kit.pse.client.goapp.uri_builder.URI_NotificationsBuilder;

/**
 * Extends the abstract class IntentService and manages the attitudes of notifications.
 *
 * Created by Ta on 10.07.2016.
 */
public class NotificationService extends IntentService {

    /**
     * Constructor. Sets a name of the service which is important for testing.
     */

    public NotificationService() {
        super("NotificationService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NotificationService(String name) {
        super(name);
    }

    /**
     * NotificationService's logic intent contains all information about the Notifications. CommunicationKeys are String key values
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
                doPut(intent);
                break;
            case CommunicationKeys.POST:
                break;
            default:
                break;
        }
    }

    /**
     *Returns a list of Notifications.
     *
     * @param intent Intent
     */
    private void doGet(Intent intent) {
        String jasonString = null;
        CloseableHttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.GET);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_MEETING_SERVICE);

        // if there no Notification Id in the Extra returns -1
        int notificationID = intent.getIntExtra(CommunicationKeys.NOTIFICATION_ID, -1);

        if (notificationID != -1) {
            URI_NotificationsBuilder uri_notificationsBuilder = new URI_NotificationsBuilder();
            uri_notificationsBuilder.addParameter(CommunicationKeys.NOTIFICATION_ID, Integer.toString(notificationID));

            HttpAppClientGet httpAppClientGet = new HttpAppClientGet();
            httpAppClientGet.setUri(uri_notificationsBuilder.getURI());

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

            bundle.putString(CommunicationKeys.NOTIFICATION, jasonString);

            // send the Bundle and the Status Code from Response
            resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
        } else {
            // Send a empty result with 500 as StatusCode

            // StatusCode 500 is an unexpected Error. Here no Meeting ID in Intent
            resultReceiver.send(500, bundle);
        }
    }

    /**
     * Activates/deactivates notifications
     *
     * @param intent
     */
    private void doPut(Intent intent) {
        String notificationAsJsonString = null;
        CloseableHttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.PUT);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_MEETING_SERVICE);

        notificationAsJsonString = intent.getStringExtra(CommunicationKeys.NOTIFICATION);

        URI_NotificationsBuilder uri_notificationsBuilder = new URI_NotificationsBuilder();

        HttpAppClientPut httpAppClientPut = new HttpAppClientPut();
        httpAppClientPut.setUri(uri_notificationsBuilder.getURI());
        try {
            httpAppClientPut.setBody(notificationAsJsonString);
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

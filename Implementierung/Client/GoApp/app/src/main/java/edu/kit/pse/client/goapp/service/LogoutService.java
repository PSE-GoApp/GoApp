package edu.kit.pse.client.goapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientDelete;
import edu.kit.pse.client.goapp.uri_builder.URI_LogoutBuilder;

/**
 * Extends the abstract class IntentService and manages the user logout.
 *
 * Created by Ta on 10.07.2016.
 */
public class LogoutService extends IntentService {

    /**
     * Constructor. Sets a name of the service which is important for testing.
     */
    public LogoutService() { super("LogoutService"); }

    /**
     * Creates an IntentService. Invoked by your subclass's constructor.
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LogoutService(String name) { super(name); }

    /**
     * LogoutService's logic intent contains all information about a user logout. CommunicationKeys are String key values
     *
     * @param intent Intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        String command = intent.getStringExtra(CommunicationKeys.COMMAND);
        switch (command) {
            case CommunicationKeys.GET:
                break;
            case CommunicationKeys.DELETE:
                doDelete(intent);
                break;
            case CommunicationKeys.PUT:
                break;
            case CommunicationKeys.POST:
                break;
            default:
                break;
        }
    }

    /**
     * Logout of a user.
     * @param intent
     */
    private void doDelete(Intent intent) {
        HttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.DELETE);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_LOGOUT_SERVICE);

        // if there no Meeting Id in the Extra returns -1
        int userId = intent.getIntExtra(CommunicationKeys.USER_ID, -1);

        if (userId != -1) {
            URI_LogoutBuilder uri_logoutBuilder = new URI_LogoutBuilder();
            uri_logoutBuilder.addParameter(CommunicationKeys.USER_ID, Integer.toString(userId));

            HttpAppClientDelete httpAppClientDelete = new HttpAppClientDelete();
            httpAppClientDelete.setUri(uri_logoutBuilder.getURI());

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
}

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
import edu.kit.pse.client.goapp.uri_builder.URI_GroupUserManagementBuilder;

/**
 * Created by e6420 on 4.7.2016 г..
 */
public class GroupUserManagementService extends IntentService {

    public GroupUserManagementService() {
        super("GroupUserManagementService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GroupUserManagementService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String command = intent.getStringExtra(CommunicationKeys.COMMAND);
        switch (command) {
            case CommunicationKeys.GET:
                doGet(intent); // wird eventuell nicht gebraucht @rumen
                break;
            case CommunicationKeys.DELETE:
                // TODO PRIO B ?
                break;
            case CommunicationKeys.PUT:
                doPut(intent);
                break;
            case CommunicationKeys.POST: // Add a User in the Group
                // TODO nicht löschen Grischa Fragen ob man da UserId und GroupId mitgibt, wenn ja wie und welche reinfolge
                // TODO Benutzer zu einer Gruppe hinzufügen
                break;
            default:
                break;
        }
    }

    private void doGet(Intent intent) {
        String jasonString = null;
        CloseableHttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.GET);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_GROUP_USER_MANAGEMENT);

        // if there no GroupId in the Extra returns -1
        int groupId = intent.getIntExtra(CommunicationKeys.GROUP_ID, -1);

        if (groupId != -1) {
            URI_GroupUserManagementBuilder uri_groupUserManagementBuilder = new URI_GroupUserManagementBuilder();
            uri_groupUserManagementBuilder.addParameter(CommunicationKeys.GROUP_ID, Integer.toString(groupId));

            HttpAppClientGet httpAppClientGet = new HttpAppClientGet();
            httpAppClientGet.setUri(uri_groupUserManagementBuilder.getURI());

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

            bundle.putString(CommunicationKeys.GROUP_USER_MANAGEMENT, jasonString);

            // send the Bundle and the Status Code from Response
            resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
        } else {
            // Send a empty result with 500 as StatusCode

            // StatusCode 500 is an unexpected Error. Here no Meeting ID in Intent
            resultReceiver.send(500, bundle);
        }
    }

    private void doPut(Intent intent) {
        // TODO groupUser oder komplettes Group ?
        String groupUserAsJsonString_or_groupAsJsonString = null;
        CloseableHttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.PUT);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_GROUP_USER_MANAGEMENT);

        groupUserAsJsonString_or_groupAsJsonString = intent.getStringExtra(CommunicationKeys.GROUP);

        URI_GroupUserManagementBuilder uri_groupUserManagementBuilder = new URI_GroupUserManagementBuilder();

        HttpAppClientPut httpAppClientPut = new HttpAppClientPut();
        httpAppClientPut.setUri(uri_groupUserManagementBuilder.getURI());
        try {
            httpAppClientPut.setBody(groupUserAsJsonString_or_groupAsJsonString);
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

package edu.kit.pse.client.goapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientDelete;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientGet;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientPost;
import edu.kit.pse.client.goapp.uri_builder.URI_GroupUserManagementBuilder;

/**
 * Extends the abstract class IntentService and manages the group members.
 *
 * Created by e6420 on 4.7.2016 г..
 */
public class GroupUserManagementService extends IntentService {

    /**
     * Constructor. Sets a name of the service which is important for testing.
     */
    public GroupUserManagementService() {
        super("GroupUserManagementService");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
                /* Register this SensorEventListener with Android sensor service */
        return START_STICKY;
    }
    /**
     * Creates an IntentService. Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GroupUserManagementService(String name) {
        super(name);
    }

    /**
     * GroupUserManagementService's logic intent contains all information about the groupUserManagement. CommunicationKeys are String key values
     *
     * @param intent Intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("Started ","Service");
        String command = intent.getStringExtra(CommunicationKeys.COMMAND);
        switch (command) {
            case CommunicationKeys.GET:
                doGet(intent); // wird eventuell nicht gebraucht @rumen
                break;
            case CommunicationKeys.DELETE:
                doDelete(intent);
                break;
            case CommunicationKeys.PUT:
               //  doPut(intent);
                break;
            case CommunicationKeys.POST: // Add a User in the Group
                doPost(intent);
                break;
            default:
                break;
        }
    }

    /**
     * Returns a list of all group members.
     *
     * @param intent Intent
     */
    private void doGet(Intent intent) {
        String jasonString = null;
        HttpResponse closeableHttpResponse = null;

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

    /**
     * Add the group members.
     *
     * @param intent Intent
     */
    private void doPost(Intent intent) {
        Boolean noError = true;
        Boolean result = true;
        String groupUserAsJsonString = null;
        HttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.POST);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_GROUP_USER_MANAGEMENT);

        groupUserAsJsonString = intent.getStringExtra(CommunicationKeys.USER);

        URI_GroupUserManagementBuilder uri_groupUserManagementBuilder = new URI_GroupUserManagementBuilder();


        HttpAppClientPost httpAppClientPost = new HttpAppClientPost();
        httpAppClientPost.setUri(uri_groupUserManagementBuilder.getURI());
        try {
            httpAppClientPost.setBody(groupUserAsJsonString);
        } catch (IOException e) {
            //Todo Handle Exception. Maybe the String Extra was null
            noError = false;
        }

        Log.e("GroupUserManagement", "bevor starting execute ");
        try {
            // TODO catch 404 (No Internet and Request Time out)
            closeableHttpResponse = httpAppClientPost.executeRequest();
            Log.e("GroupUserManagement", "Got result from Server");
        } catch (Exception e) {
            // TODO handle Exception Toast? Alert Dialog? sent it to the Activity?
            result = false;
        }
        Log.e("GroupUserManagement", "middlediddle");

        if (result && noError) {
            // send the Bundle and  the Status Code from Response
            resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
        } else {
            resultReceiver.send(500, bundle);
        }
    }

    private void doDelete(Intent intent) {

        boolean noError = true;
        int groupId = -1;
        int userId = -1;

        HttpResponse closeableHttpResponse = null;

        groupId = intent.getIntExtra(CommunicationKeys.GROUP_ID, -1);
        userId = intent.getIntExtra(CommunicationKeys.USER_ID, -1);


        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.DELETE);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_GROUP_USER_MANAGEMENT);

        // if there no GroupUserManagement Id in the Extra returns -1
        // String groupUseMember = intent.getStringExtra(CommunicationKeys.USER);

            URI_GroupUserManagementBuilder uri_groupUserManagementBuilder = new URI_GroupUserManagementBuilder();
        uri_groupUserManagementBuilder.addParameter("userId", userId + "");
        uri_groupUserManagementBuilder.addParameter("groudId", groupId + "");

        /*
            HttpAppClientPost httpAppClientPost = new HttpAppClientPost();
            httpAppClientPost.setUri(uri_groupUserManagementBuilder.getURI());
            try {
                httpAppClientPost.setBody(groupUseMember);
            } catch (IOException e) {
                //Todo Handle Exception. Maybe the String Extra was null
                noError = false;
            }
            */

            // uri_groupUserManagementBuilder.addParameter(CommunicationKeys.USER, Integer.toString(groupUserManagementId));

            HttpAppClientDelete httpAppClientDelete = new HttpAppClientDelete();
            httpAppClientDelete.setUri(uri_groupUserManagementBuilder.getURI());

            try {
                // TODO catch 404 (No Internet and Request Time out)
                closeableHttpResponse = httpAppClientDelete.executeRequest();
            } catch (IOException e) {
                // TODO handle Exception Toast? Alert Dialog? sent it to the Activity?
            }

            // send the Bundle and the Status Code from Response
            resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
    }

}

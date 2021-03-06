package edu.kit.pse.client.goapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientDelete;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientGet;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientPost;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientPut;
import edu.kit.pse.client.goapp.uri_builder.URI_GroupBuilder;

/**
 * Extends the abstract class IntentService and manages a group.
 *
 * Created by e6420 on 3.7.2016 г..
 */
public class GroupService extends IntentService {

    /**
     * Constructor. Sets a name of the service which is important for testing.
     */
    public GroupService() {
        super("GroupService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GroupService(String name) {
        super(name);
    }

    /**
     * GroupService's logic intent contains all information about the group. CommunicationKeys are String key values
     *
     * @param intent Intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        String command = intent.getStringExtra(CommunicationKeys.COMMAND);
        switch (command) {
            case CommunicationKeys.GET:
                doGet(intent); // get Group Info we dont neet it
                break;
            case CommunicationKeys.DELETE:
                doDelete(intent);
                break;
            case CommunicationKeys.PUT:
                doPut(intent);
                break;
            case CommunicationKeys.POST:
                doPost(intent); // Create a new Group
                break;
            default:
                break;
        }

    }

    /**
     * Returns information about the group.
     *
     * @param intent Intent
     */
    private void doGet (Intent intent) //throws  IOException
    {
        Boolean noError = true;
        Boolean result = true;
        String jasonString = null;
        HttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.GET);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_GROUP_SERVICE);

        // if there no Group Id in the Extra returns -1
        int groupId = intent.getIntExtra(CommunicationKeys.GROUP_ID, -1);

        if (groupId != -1) {
            URI_GroupBuilder uri_groupBuilder = new URI_GroupBuilder();
            uri_groupBuilder.addParameter(CommunicationKeys.GROUP_ID, Integer.toString(groupId));

            HttpAppClientGet httpAppClientGet = new HttpAppClientGet();
            httpAppClientGet.setUri(uri_groupBuilder.getURI());

            try {
                // TODO catch 404 (No Internet and Request Time out)
                closeableHttpResponse = httpAppClientGet.executeRequest();
            } catch (IOException e) {
                // TODO handle Exception Toast? Alert Dialog? sent it to the Activity?
                result = false;
            }

            // accepted
            try {
                jasonString = EntityUtils.toString(closeableHttpResponse.getEntity());
            } catch (Throwable e) {
                // TODO handle Exception "can not Convert EntitlyUtils to String"
                noError = false;
            }

            if (result && noError) {

                bundle.putString(CommunicationKeys.GROUP, jasonString);

                // send the Bundle and the Status Code from Response
                resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
            } else {
                resultReceiver.send(500, bundle);
            }
        } else {
            // Send a empty result with 500 as StatusCode

            // StatusCode 500 is an unexpected Error. Here no Meeting ID in Intent
            resultReceiver.send(500, bundle);
        }
    }

    /**
     * Deletes a group.
     *
     * @param intent Intent
     */
    private void doDelete(Intent intent) {
        Boolean result = true;
        HttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.DELETE);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_GROUP_SERVICE);

        // if there no Meeting Id in the Extra returns -1
        int groupId = intent.getIntExtra(CommunicationKeys.GROUP_ID, -1);

        if (groupId != -1) {
            URI_GroupBuilder uri_groupBuilder = new URI_GroupBuilder();
            uri_groupBuilder.addParameter(CommunicationKeys.GROUP_ID, Integer.toString(groupId));

            HttpAppClientDelete httpAppClientDelete = new HttpAppClientDelete();
            httpAppClientDelete.setUri(uri_groupBuilder.getURI());

            try {
                // TODO catch 404 (No Internet and Request Time out)
                closeableHttpResponse = httpAppClientDelete.executeRequest();
            } catch (IOException e) {
                // TODO handle Exception Toast? Alert Dialog? sent it to the Activity?
                result = false;
            }

            if (result) {
                // send the Bundle and the Status Code from Response
                resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
            } else {
                // StatusCode 500 is an unexpected Error. Here no Meeting ID in Intent
                resultReceiver.send(500, bundle);
            }
        } else {
            // Send a empty result with 500 as StatusCode

            // StatusCode 500 is an unexpected Error. Here no Meeting ID in Intent
            resultReceiver.send(500, bundle);
        }

    }

    /**
     * Updates group information.
     *
     * @param intent Intent
     */
    private void doPut(Intent intent) {

        Boolean noError = true;
        Boolean result = true;
        String groupAsJsonString = null;
        HttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.PUT);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_GROUP_SERVICE);

        groupAsJsonString = intent.getStringExtra(CommunicationKeys.GROUP);

        URI_GroupBuilder uri_groupBuilder = new URI_GroupBuilder();

        HttpAppClientPut httpAppClientPut = new HttpAppClientPut();
        httpAppClientPut.setUri(uri_groupBuilder.getURI());
        try {
            httpAppClientPut.setBody(groupAsJsonString);
        } catch (IOException e) {
            //Todo Handle Exception. Maybe the String Extra was null
            result = false;
        }

        try {
            // TODO catch 404 (No Internet and Request Time out)
            closeableHttpResponse = httpAppClientPut.executeRequest();
        } catch (IOException e) {
            // TODO handle Exception Toast? Alert Dialog? sent it to the Activity?
            noError = false;
        }

        // send the Bundle and the Status Code from Response
        resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
    }

    /**
     * Creates a new group.
     *
     * @param intent Intent
     */
    private void doPost(Intent intent) {
        Boolean noError = true;
        Boolean result = true;
        String jsonString = null ;
        String groupAsJsonString = null;
        HttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.POST);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_GROUP_SERVICE);

        groupAsJsonString = intent.getStringExtra(CommunicationKeys.GROUP);

        URI_GroupBuilder uri_groupBuilder = new URI_GroupBuilder();

        HttpAppClientPost httpAppClientPost = new HttpAppClientPost();
        httpAppClientPost.setUri(uri_groupBuilder.getURI());
        try {
            httpAppClientPost.setBody(groupAsJsonString);
        } catch (IOException e) {
            //Todo Handle Exception. Maybe the String Extra was null
            result = false;
        }

        try {
            // TODO catch 404 (No Internet and Request Time out)
            closeableHttpResponse = httpAppClientPost.executeRequest();
        } catch (IOException e) {
            // TODO handle Exception Toast? Alert Dialog? sent it to the Activity?
            noError = false;
        }

        // accepted
        try {
            jsonString = EntityUtils.toString(closeableHttpResponse.getEntity());
        } catch (Throwable e) {
            // TODO handle Exception "can not Convert EntitlyUtils to String"
            noError = false;
        }

        if (result && noError) {

            bundle.putString(CommunicationKeys.GROUP, jsonString);
            // send the Bundle and the Status Code from Response
            resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
        } else {
            resultReceiver.send(500, bundle);

        }

    }

}


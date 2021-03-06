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
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientPut;
import edu.kit.pse.client.goapp.uri_builder.URI_UserBuilder;

/**
 * Extends the abstract class IntentService and manages a user
 *
 * Created by paula on 10.07.16.
 */
public class UserService extends IntentService{

    /**
     * Constructor. Sets a name of the service which is important for testing.
     */
    public UserService() {
        super("UserService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UserService(String name) {
        super(name);
    }

    /**
     * UserService's logic intent contains all information about the user. CommunicationKeys are String key values
     *
     * @param intent Intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        String command = intent.getStringExtra(edu.kit.pse.client.goapp.CommunicationKeys.COMMAND);
        switch (command) {
            case "GET":
                doGet(intent);
                break;
            case "DELETE":
                doDelete(intent);
                break;
            case "PUT":
                doPut(intent);
                break;
            case "POST":
                doPost(intent);
                break;
            default:
                break;
        }
    }

    /**
     * Returns information about the user.
     *
     * @param intent Intent
     */
    private void doGet (Intent intent) {
        Boolean noError = true;
        Boolean result = true;

        String jasonString = null;
        HttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.GET);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_USER_SERVICE);

        // if there no User Id in the Extra returns -1
        int userId = intent.getIntExtra(CommunicationKeys.USER_ID, -1);

        if (userId != -1) {
            URI_UserBuilder uri_userBuilder = new URI_UserBuilder();
            uri_userBuilder.addParameter(CommunicationKeys.USER_ID, Integer.toString(userId));

            HttpAppClientGet httpAppClientGet = new HttpAppClientGet();
            httpAppClientGet.setUri(uri_userBuilder.getURI());

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
                result = false;
            }

            if (result) {

                bundle.putString(edu.kit.pse.client.goapp.CommunicationKeys.USER, jasonString);

                // send the Bundle and the Status Code from Response
                resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
            }
        } else {
            // Send a empty result with 500 as StatusCode

            // StatusCode 500 is an unexpected Error. Here no Meeting ID in Intent
            resultReceiver.send(500, bundle);
        }
    }

    /**
     * Deletes a user.
     *
     * @param intent Intent
     */
    private void doDelete(Intent intent) {
        HttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.DELETE);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_USER_SERVICE);

        // if there no User Id in the Extra returns -1
        int userId = intent.getIntExtra(CommunicationKeys.USER_ID, -1);

        if (userId != -1) {
            URI_UserBuilder uri_userBuilder = new URI_UserBuilder();
            uri_userBuilder.addParameter(CommunicationKeys.USER_ID, Integer.toString(userId));

            HttpAppClientDelete httpAppClientDelete = new HttpAppClientDelete();
            httpAppClientDelete.setUri(uri_userBuilder.getURI());

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
     * Updates information about a user.
     *
     * @param intent Intent
     */
    private void doPut(Intent intent) {
        String userAsJsonString = null;
        HttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.PUT);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_USER_SERVICE);

        userAsJsonString = intent.getStringExtra(CommunicationKeys.USER);

        URI_UserBuilder uri_userBuilder = new URI_UserBuilder();

        HttpAppClientPut httpAppClientPut = new HttpAppClientPut();
        httpAppClientPut.setUri(uri_userBuilder.getURI());
        try {
            httpAppClientPut.setBody(userAsJsonString);
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
     * Creates a new user.
     *
     * @param intent Intent
     */
    private void doPost(Intent intent) { // Create a new User

        String jUser = null;
        String googleId = null;
        Boolean noError = true;
        Boolean result = true;

        HttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.POST);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_USER_SERVICE);

        jUser = intent.getStringExtra(CommunicationKeys.USER);

        URI_UserBuilder uri_userBuilder = new URI_UserBuilder();
      // uri_userBuilder.addParameter("token",);
        HttpAppClientPost httpAppClientPost = new HttpAppClientPost();
        httpAppClientPost.setUri(uri_userBuilder.getURI());


        try {
            httpAppClientPost.setBody(jUser);
        } catch (IOException e) {
            //Todo Handle Exception. Maybe the String Extra was null
            noError = false;
        }

        try {
            // TODO catch 404 (No Internet and Request Time out)
            closeableHttpResponse = httpAppClientPost.executeRequest();
        } catch (IOException e) {
            // TODO handle Exception Toast? Alert Dialog? sent it to the Activity?
            result = false;
        }

        String resultJsonString = null;
        // accepted
        try {
            resultJsonString = EntityUtils.toString(closeableHttpResponse.getEntity());
        } catch (Throwable e) {
            noError = false;
            Log.e("error",e.getMessage());
        }
        Log.e("e", resultJsonString);

        if (noError && result) {

            // send the Bundle and the Status Code from Response
            resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
        } else {
            resultReceiver.send(500 , bundle);
        }
    }

}

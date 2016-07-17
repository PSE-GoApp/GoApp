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
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientGet;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientPost;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientPut;
import edu.kit.pse.client.goapp.uri_builder.URI_LoginBuilder;

/**
 * Extends the abstract class IntentService and manages the user authorisation.
 *
 * Created by Ta on 10.07.2016.
 */
public class LoginService extends IntentService {

    //Konstruktor gibt dem Service einen Namen, der fürs Testen wichtig ist.
    public LoginService() {super("LoginService");}

    /**
     * Creates an IntentService. Invoked by your subclass's constructor.
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LoginService(String name) { super(name);}

    /**
     * LoginService's logic intent contains all information about the user login. CommunicationKeys are String key values
     *
     * @param intent Intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        String command = intent.getStringExtra(CommunicationKeys.COMMAND);
        switch (command) {
            case CommunicationKeys.GET:
                // TODO Server Leute fragen ob wir das Brauchen/verwenden ? oder nur für sie ist
                doGet(intent);
                break;
            case CommunicationKeys.DELETE:
                break;
            case CommunicationKeys.PUT:
                doPut(intent);
                break;
            case CommunicationKeys.POST: // register Client
                doPost(intent);
                break;
            default:
                break;
        }
    }

    /**
     * Returns if the user is registered.
     *
     * @param intent Intent
     */
    public void doGet(Intent intent) {
        String jasonString = null;
        HttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.GET);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_LOGIN_SERVICE);

        // if there no Meeting Id in the Extra returns -1
        int userId = intent.getIntExtra(CommunicationKeys.USER_ID, -1);

        if (userId != -1) {
            URI_LoginBuilder uri_loginBuilder = new URI_LoginBuilder();
            uri_loginBuilder.addParameter(CommunicationKeys.USER_ID, Integer.toString(userId));

            HttpAppClientGet httpAppClientGet = new HttpAppClientGet();
            httpAppClientGet.setUri(uri_loginBuilder.getURI());

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

            bundle.putString(CommunicationKeys.USERS, jasonString);

            // send the Bundle and the Status Code from Response
            resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
        } else {
            // Send a empty result with 500 as StatusCode

            // StatusCode 500 is an unexpected Error. Here no Meeting ID in Intent
            resultReceiver.send(500, bundle);
        }
    }

    /**
     *
     * @param intent Intent
     */
    private void doPut(Intent intent) { // Login
        String resultJsonString = null;
        String googleIdToken = null;
        Boolean noError = true;
        Boolean result = true;


        HttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.PUT);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_LOGIN_SERVICE);

        googleIdToken = intent.getStringExtra(CommunicationKeys.USER_ID_TOKEN);


        URI_LoginBuilder uri_loginBuilder = new URI_LoginBuilder();
        uri_loginBuilder.addParameter("token", googleIdToken);

        HttpAppClientPut httpAppClientPut = new HttpAppClientPut();
         httpAppClientPut.setUri(uri_loginBuilder.getURI());

        /*
        try {
            httpAppClientPut.setBody(googleIdToken);
        } catch (IOException e) {
            noError = false;
        }
        */


        try {
            // TODO catch 404 (No Internet and Request Time out)
            closeableHttpResponse = httpAppClientPut.executeRequest();
        } catch (IOException e) {
            //TODO Handle Exception Toast? Alert Dialog? Sent it to the Activity?
            result = false;
        }

        // accepted
        try {
            resultJsonString = EntityUtils.toString(closeableHttpResponse.getEntity());
        } catch (Throwable e) {
            noError = false;
        }

        if (noError && result) {

            bundle.putString(CommunicationKeys.USER, resultJsonString);

            // send the Bundle and the Status Code from Response
            resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
        } else {
            // Internal Error
            resultReceiver.send(500, bundle);
        }


       /*

        // Todo test -----------------------------------------------------------------------------------

        User testUser = new User(42424269, "KANSE'S DICK");
        ObjectConverter<User> userObjectConverter = new ObjectConverter<>();
        String json = userObjectConverter.serialize(testUser, User.class);

        bundle.putString(CommunicationKeys.USER, json);

        resultReceiver.send(202, bundle);
        */
    }

    /**
     *
     * @param intent Intent
     */
    private void doPost(Intent intent) {  // Register Client
        Boolean noError = true;
        Boolean result = true;

        String googeleIdToken = null;
        String resultJsonString = null;

        HttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.POST);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_LOGIN_SERVICE);

        googeleIdToken = intent.getStringExtra(CommunicationKeys.USER_ID_TOKEN);


        URI_LoginBuilder uri_loginBuilder = new URI_LoginBuilder();
        // uri_loginBuilder.addParameter("token", googeleIdToken);
        uri_loginBuilder.addParameter("token", "test");

        HttpAppClientPost httpAppClientPost = new HttpAppClientPost();

        httpAppClientPost.setUri(uri_loginBuilder.getURI());
        /*
        try {
            httpAppClientPost.setBody(googeleIdToken);
        } catch (IOException e) {
            //TODO Handle Exception. Maybe the String Extra was null
            noError = false;
        }
        */


        try {
            //TODO catch 404 (No Intent and Request Time out)
            closeableHttpResponse = httpAppClientPost.executeRequest();
        } catch (IOException e) {
            // TODO handle Exception Toast? Alert Dialog? sent it to the Activicy?
            result = false;
        }

        if (result && noError) {

            resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
        }
        else {
            resultReceiver.send(500, bundle);
        }

        /*

        TODO löschen
        resultReceiver.send(202, bundle);
        */

    }

}

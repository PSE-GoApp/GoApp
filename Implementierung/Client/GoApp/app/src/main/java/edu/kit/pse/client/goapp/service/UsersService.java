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
import edu.kit.pse.client.goapp.uri_builder.URI_MeetingBuilder;
import edu.kit.pse.client.goapp.uri_builder.URI_UsersBuilder;

/**
 * Created by paula on 10.07.16.
 */
public class UsersService extends IntentService {

    //Konstruktor gibt den Service ein Namen, der fürs Testen wichtig ist.

    public UsersService() {
        super("UsersService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UsersService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String command = intent.getStringExtra(CommunicationKeys.COMMAND);
        switch (command) {
            case "GET":
                doGet(intent);
                break;
            default:
                break;
        }
    }

    private void doGet (Intent intent) //throws  IOException
    {
        String jasonString = null;
        CloseableHttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.GET);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_USERS_SERVICE);

        URI_UsersBuilder uri_usersBuilder = new URI_UsersBuilder();

        HttpAppClientGet httpAppClientGet = new HttpAppClientGet();
        httpAppClientGet.setUri(uri_usersBuilder.getURI());

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
    }
}

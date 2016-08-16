package edu.kit.pse.client.goapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientGet;
import edu.kit.pse.client.goapp.uri_builder.URI_UsersBuilder;

/**
 * Extends the abstract class IntentService and manages a list of all users.
 *
 * Created by paula on 10.07.16.
 */
public class UsersService extends IntentService {

    /**
     * Constructor. Sets a name of the service which is important for testing.
     */
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

    /**
     * UsersService's logic intent contains all information about the Users. CommunicationKeys are String key values
     *
     * @param intent Intent
     */
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

    /**
     * Returns a list of all users.
     *
     * @param intent Intent
     */
    private void doGet (Intent intent) //throws  IOException
    {
        Boolean result = true;
        Boolean gotJString = false;
        String jasonString = null;
        HttpResponse closeableHttpResponse = null;

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
            result = false;
        }

        // accepted
        try {
            jasonString = EntityUtils.toString(closeableHttpResponse.getEntity());
            gotJString = true;
        } catch (Throwable e) {
            // TODO handle Exception "can not Convert EntitlyUtils to String"
            gotJString = false;
        }

        if (result) {
            if (gotJString) {
                bundle.putString(CommunicationKeys.USERS, jasonString);
            }
            // send the Bundle and the Status Code from Response
            resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
        } else {
            resultReceiver.send(500, bundle);

        }
    }
}

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
import edu.kit.pse.client.goapp.uri_builder.URI_GroupsBuilder;

/**
 * Extends the abstract class IntentService and manages al list of all groups.
 *
 * Created by e6420 on 28.6.2016 г..
 */
public class GroupsService extends IntentService {

    /**
     * Constructor. Sets a name of the service which is important for testing.
     */
    public GroupsService() {
        super("GroupsService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GroupsService(String name) {
        super(name);
    }

    /**
     * GroupsService's logic intent contains all information about the groups. CommunicationKeys are String key values
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
            default:
                break;
        }

    }

    /**
     * Returns a list of all groups
     * @param intent
     */
    private void doGet(Intent intent) {
        Boolean gotJString = false;
        Boolean result = true;
        String jasonString = null;
        HttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.GET);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_GROUPS_SERVICE);

        URI_GroupsBuilder uri_groupsBuilder = new URI_GroupsBuilder();

        HttpAppClientGet httpAppClientGet = new HttpAppClientGet();
        httpAppClientGet.setUri(uri_groupsBuilder.getURI());

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
            gotJString = false;
            // TODO handle Exception "can not Convert EntitlyUtils to String"
        }

        if (result) {
            if (gotJString) {
                bundle.putString(CommunicationKeys.GROUPS, jasonString);
            }
            // send the Bundle and the Status Code from Response
            resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
        } else {
            resultReceiver.send(500, bundle);
        }

    }

}

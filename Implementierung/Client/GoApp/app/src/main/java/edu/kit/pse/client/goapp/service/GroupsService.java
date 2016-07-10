package edu.kit.pse.client.goapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientGet;
import edu.kit.pse.client.goapp.parcelableAdapters.ParcelableGroup;
import edu.kit.pse.client.goapp.datamodels.Group;
import edu.kit.pse.client.goapp.uri_builder.URI_GroupsBuilder;
import edu.kit.pse.client.goapp.uri_builder.URI_UsersBuilder;

/**
 * Created by e6420 on 28.6.2016 г..
 */
public class GroupsService extends IntentService {

    //Konstruktor gibt den Service ein Namen, der fürs Testen wichtig ist

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

    private void doGet(Intent intent) //throws  IOException
    {
        String jasonString = null;
        CloseableHttpResponse closeableHttpResponse = null;

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
        }

        // accepted
        try {
            jasonString = EntityUtils.toString(closeableHttpResponse.getEntity());
        } catch (Throwable e) {
            // TODO handle Exception "can not Convert EntitlyUtils to String"
        }

        bundle.putString(CommunicationKeys.GROUPS, jasonString);

        // send the Bundle and the Status Code from Response
        resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
    }

}

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
import edu.kit.pse.client.goapp.uri_builder.URI_MeetingsBuilder;

/**
 * Extends the abstract class IntentService and manages a list of all meetings.
 *
 * Created by paula on 10.07.16.
 */
public class MeetingsService extends IntentService{

    /**
     * Constructor. Sets a name of the service which is important for testing.
     */
    public MeetingsService() {
        super("MeetingsService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MeetingsService(String name) {
        super(name);
    }

    /**
     * MeetingsService's logic intent contains all information about the meetings. CommunicationKeys are String key values
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
     *Returns a list of Meetings.
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
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_MEETINGS_SERVICE);

        URI_MeetingsBuilder uri_meetingsBuilder = new URI_MeetingsBuilder();


        HttpAppClientGet httpAppClientGet = new HttpAppClientGet();

        httpAppClientGet.setUri(uri_meetingsBuilder.getURI());


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

        /*
        if (noError && result) {

            bundle.putString(CommunicationKeys.MEETINGS, jasonString);

            // send the Bundle and the Status Code from Response
            resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
        } else {
            // the Try catch went wrong: send the Bundle and a Status Code 500
            resultReceiver.send(500, bundle);
        }
        */
        // todo Delet its a Test
        resultReceiver.send(200, bundle);

    }
}

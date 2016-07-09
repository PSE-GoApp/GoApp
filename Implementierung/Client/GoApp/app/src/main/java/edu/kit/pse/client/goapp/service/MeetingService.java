package edu.kit.pse.client.goapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.converter.MeetingConverter;
import edu.kit.pse.client.goapp.datamodels.Group;
import edu.kit.pse.client.goapp.datamodels.Meeting;
import edu.kit.pse.client.goapp.httpappclient.HttpAppClientGet;
import edu.kit.pse.client.goapp.httpappclient.HttpResponse;
import edu.kit.pse.client.goapp.parcelableAdapters.ParcelableGroup;
import edu.kit.pse.client.goapp.uri_builder.URI_MeetingBuilder;

/**
 * Created by kansei on 09.07.16.
 */
public class MeetingService  extends IntentService {

    //Konstruktor gibt den Service ein Namen, der fürs Testen wichtig ist.

    public MeetingService() {
        super("MeetingService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MeetingService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String command = intent.getStringExtra(CommunicationKeys.COMMAND);
        switch (command) {
            case "GET":
                doGet(intent);
                break;
            case "DELETE":
                doDelet(intent);
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


    private void doGet (Intent intent) //throws  IOException
    {
        String jasonString = null;
        HttpResponse httpResponse;
        HttpEntity httpEntity = null;
        CloseableHttpResponse closeableHttpResponse = null;
        // if there no Meeting Id in the Extra returns -1
        int meetingId = intent.getIntExtra(CommunicationKeys.MEETING_ID, -1);
        URI_MeetingBuilder uri_meetingBuilder = new URI_MeetingBuilder();

        //TODO ich weiß nicht was als key rein kommt!
        uri_meetingBuilder.addParameter("MEETING ID KEY HERE!" , Integer.toString(meetingId));

        HttpAppClientGet httpAppClientGet = new  HttpAppClientGet();
        httpAppClientGet.setUri(uri_meetingBuilder.getURI());

        try {

            closeableHttpResponse = httpAppClientGet.executeRequest();
        } catch (IOException e) {
            // TODO handle Exception Toast? Alert Dialog? sent it to the Activity?
        }

        httpResponse = new HttpResponse(closeableHttpResponse);

        if (httpResponse.getStatusCode() == 202) {

            // TODO auskommentieren!! wenn die methode getEntitiy fertig ist!
            //httpEntity = httpResponse.getEntity;

            try {
                jasonString = EntityUtils.toString(httpEntity);
            } catch (Throwable e) {
                // TODO handle Exception "can not Convert EntitlyUtils to String"
            }

            MeetingConverter meetingConverter = new MeetingConverter();
            Meeting meeting = meetingConverter.deserialize(jasonString);




            // TODO ResultReceiver

            //this sends the group list VON RUMEN
            List<Group> list = listMe();
            ArrayList<ParcelableGroup> parcelableGroups = new ArrayList<ParcelableGroup>();
            for (Group group: list) {
                parcelableGroups.add(new ParcelableGroup(group));
            }
            final ResultReceiver receiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);
            Bundle b = new Bundle();
            b.putParcelableArrayList(CommunicationKeys.GROUPS,parcelableGroups);
            b.putString(CommunicationKeys.COMMAND, "GET");
            b.putString(CommunicationKeys.SERVICE, "GroupsService");
            receiver.send(202, b);
        }
        else {
            switch (httpResponse.getStatusCode()) {
                case 400:
                    // bad request
                    break;

                case 403:
                    // forbitten
                    break;

                case 404:
                    // not found
                    break;

                case 408:
                    // request Time out
                    break;

                case 500:
                    // interna Server error
                    break;
            }
        }


    }

    private void doDelet(Intent intent) {

    }

    private void doPut(Intent intent) {

    }

    private void doPost(Intent intent) {

    }

    // creates a list for testing
    private List<Group> listMe() {
        List<Group> list = new ArrayList<>();
        list.add(new Group(1,"fucker"));
        list.add(new Group(2, "sucker"));
        return list;
    }
}

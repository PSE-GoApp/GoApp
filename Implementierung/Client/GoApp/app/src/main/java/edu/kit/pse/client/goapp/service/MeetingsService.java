package edu.kit.pse.client.goapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.util.ArrayList;
import java.util.List;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.converter.ObjectConverter;
import edu.kit.pse.client.goapp.datamodels.Event;
import edu.kit.pse.client.goapp.datamodels.GPS;
import edu.kit.pse.client.goapp.datamodels.Meeting;
import edu.kit.pse.client.goapp.datamodels.MeetingConfirmation;
import edu.kit.pse.client.goapp.datamodels.Participant;
import edu.kit.pse.client.goapp.datamodels.Tour;
import edu.kit.pse.client.goapp.datamodels.User;
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
        String jasonString = null;
        HttpResponse closeableHttpResponse = null;

        final ResultReceiver resultReceiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);

        Bundle bundle = new Bundle();
        bundle.putString(CommunicationKeys.COMMAND, CommunicationKeys.GET);
        bundle.putString(CommunicationKeys.SERVICE, CommunicationKeys.FROM_MEETINGS_SERVICE);

        // TODO entcommitten ------------------------------------------------------------------------------------------------
        URI_MeetingsBuilder uri_meetingsBuilder = new URI_MeetingsBuilder();


        HttpAppClientGet httpAppClientGet = new HttpAppClientGet();
        /*
        httpAppClientGet.setUri(uri_meetingsBuilder.getURI());

        /*try {
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

        bundle.putString(CommunicationKeys.MEETINGS, jasonString);

        // send the Bundle and the Status Code from Response
        resultReceiver.send(closeableHttpResponse.getStatusLine().getStatusCode(), bundle);
        */

        // TOdo DElete after testing--------------------------------------------------------------------------------

        ObjectConverter<List<Meeting>> mConverter = new ObjectConverter<>();
        List<Meeting> dummy = new ArrayList<Meeting>();
        String json = mConverter.serialize(meetings, (Class<List<Meeting>>) dummy.getClass());

        bundle.putString(CommunicationKeys.MEETINGS, json);

        // send the Bundle and the Status Code from Response
        resultReceiver.send(202, bundle);
        // todo delete ------------------------------------------------------------------------------------
    }


    // Todo delete it after Testing -------------------------------------------------------------------------
    User me = new User(42424269, "KANSEi'S DICK");
    User asshole = new User(13, "KEVIN!!");
    // private int[] imageId = {R.drawable.checked, R.drawable.cancel, R.drawable.somemap, R.drawable.participant};


    Participant keinKevin = new Participant(0,2,  asshole, MeetingConfirmation.REJECTED);
    Participant kevin = new Participant(0,2,  asshole, MeetingConfirmation.CONFIRMED);
    Participant itsMeConfirmed = new Participant(0,2,  me, MeetingConfirmation.CONFIRMED);
    Participant itsMePending = new Participant(0,3, me, MeetingConfirmation.PENDING);
    Participant imParticipant = new Participant(0,3, me, MeetingConfirmation.REJECTED);

    private List<Meeting> meetings = new ArrayList<Meeting>() {
        {
            new Event(0, "Mensa", new GPS(1,1,1), 1475953024180L, 2, imParticipant) {{
                addParticipant(itsMeConfirmed);
                addParticipant(keinKevin);
            }};
            add(new Event(1, "Ago", new GPS(1,1,1), 1475953024000L, 2, imParticipant) {{
                addParticipant(itsMePending);
                addParticipant(kevin);
            }});
            add(new Event(2, "PSE Treffen", new GPS(1,1,1), 1475953021200L, 2, imParticipant) {{
                addParticipant(itsMePending);
                addParticipant(kevin);
                addParticipant(keinKevin);
                addParticipant(kevin);

            }});
            add(new Tour(3, "Iris Füttern", new GPS(1,1,1), 1476953024000L, 2, imParticipant) {{
                addParticipant(itsMePending);
                addParticipant(kevin);
                addParticipant(keinKevin);
                addParticipant(kevin);
                addParticipant(keinKevin);
                addParticipant(kevin);
            }});
            add(new Tour(4, "Schloss Park", new GPS(1,1,1), 1475953021200L, 2, imParticipant) {{
                addParticipant(itsMeConfirmed);
            }});
            add(new Event(5, "Klettern", new GPS(1,1,1), 147595302489L, 2, imParticipant) {{
                addParticipant(itsMePending);
                addParticipant(kevin);
                addParticipant(keinKevin);
                addParticipant(kevin);
                addParticipant(keinKevin);
                addParticipant(kevin);
                addParticipant(kevin);
            }});
            add(new Tour(6, "Bar Tour", new GPS(1,1,1), 14759530243560L, 2, imParticipant) {{
                addParticipant(itsMeConfirmed);
                addParticipant(kevin);
                addParticipant(keinKevin);
                addParticipant(kevin);
                addParticipant(keinKevin);
                addParticipant(kevin);
                addParticipant(kevin);
                addParticipant(kevin);
            }});
            add(new Tour(7, "Bar mit Tour", new GPS(1,1,1), 14759530243560L, 2, kevin) {{
                addParticipant(imParticipant);
                addParticipant(kevin);
                addParticipant(kevin);
                addParticipant(kevin);
                addParticipant(kevin);
                addParticipant(kevin);
                addParticipant(kevin);
                addParticipant(kevin);
                addParticipant(kevin);
            }});
        }
    };
    //------------------------------------------------------------------------------------------------------------------------------------------------

}

package edu.kit.pse.client.goapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.ServiceResultReceiver;
import edu.kit.pse.client.goapp.converter.ObjectConverter;
import edu.kit.pse.client.goapp.datamodels.Event;
import edu.kit.pse.client.goapp.datamodels.GPS;
import edu.kit.pse.client.goapp.datamodels.Meeting;
import edu.kit.pse.client.goapp.datamodels.MeetingConfirmation;
import edu.kit.pse.client.goapp.datamodels.Participant;
import edu.kit.pse.client.goapp.datamodels.Tour;
import edu.kit.pse.client.goapp.datamodels.User;
import edu.kit.pse.client.goapp.service.MeetingParticipantManagementService;
import edu.kit.pse.client.goapp.service.MeetingsService;
import edu.kit.pse.goapp.client.goapp.R;

public class MeetingListActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ServiceResultReceiver.Receiver {

    private static final String TAG = "MeetingList";
    private ServiceResultReceiver activityServiceResultReceiver;

    private ObjectConverter<List<Meeting>> meetingListConverter;
    private ObjectConverter<Participant> participantConverter;

    ImageButton menu_button;
    private Context context = this;
    private ProgressDialog mProgressDialog;
    private ListView list;
    private User myUser;
    private List<Meeting> meetings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);
        menu_button = (ImageButton) findViewById(R.id.menu_termine);
        menu_button.setOnClickListener(this);
        meetingListConverter = new ObjectConverter<>();
        participantConverter = new ObjectConverter<>();

        list = (ListView) findViewById(R.id.meeting_ListView);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "");
        int userId = sharedPreferences.getInt("userId", -1);
        if (userId == -1) {
            // Todo close App :D
            Log.e(TAG, "No User Information");
        }
        myUser = new User(userId, userName);

        Intent i = new Intent(this, MeetingsService.class);
        activityServiceResultReceiver = new ServiceResultReceiver(new Handler());
        activityServiceResultReceiver.setReceiver(this);
        i.putExtra(CommunicationKeys.RECEICER, activityServiceResultReceiver);
        i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.GET);
        startService(i);
    }


    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MeetingListActivity.class);
        activity.startActivity(intent);
    }

    private void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(MeetingListActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_meeting_list, popup.getMenu());
        popup.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu_termine) {
            showPopUp(v);
        }
    }

    public void meetingList_FirstButtonOnClickHandler(View view) {

        RelativeLayout meetingRow = (RelativeLayout) view.getParent();

        // should be in Try catch ? Nope because of the Unique Key R.id.TAG_MEETING
        Meeting meeting = (Meeting) meetingRow.getTag(R.id.TAG_MEETING);

        // Todo remove this Toast Test --------------------------------------------------------------------------------------------------------------------------------
        Toast.makeText(getApplicationContext(), meeting.getName() + " Button 1 was clicked", Toast.LENGTH_SHORT).show();


        ImageButton firstButton;
        firstButton = (ImageButton) meetingRow.getChildAt(2);
        int imageDirection = (int) firstButton.getTag(R.id.TAG_IMAGE_DIRECTION);

        ImageButton secondButton;

        if (imageDirection == R.drawable.checked) {
            showProgressDialog();

            //create a MeetingService, that send a meeting conformation change (Accepted)
            List<Participant> participants = meeting.getParticipants();
            Participant meAsParticipant = null;

            // Find me as Participant
            for(Participant p : participants) {

                if (p.getUser().getId() == myUser.getId()) {
                    meAsParticipant = p;
                }
            }

            meAsParticipant.setConfirmation(MeetingConfirmation.CONFIRMED);

            String jparticipant = participantConverter.serialize(meAsParticipant, Participant.class);

            Intent i = new Intent(this, MeetingParticipantManagementService.class);
            activityServiceResultReceiver = new ServiceResultReceiver(new Handler());
            activityServiceResultReceiver.setReceiver(this);
            i.putExtra(CommunicationKeys.RECEICER, activityServiceResultReceiver);
            i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.PUT);
            i.putExtra(CommunicationKeys.PARTICIPANT, jparticipant);
            startService(i);


            // change buttons to a accepted Meeting.
            firstButton.setImageResource(R.drawable.somemap);
            firstButton.setTag(R.id.TAG_IMAGE_DIRECTION, R.drawable.somemap);

            secondButton = (ImageButton) meetingRow.getChildAt(3);
            secondButton.setImageResource(R.drawable.participant);
            secondButton.setTag(R.id.TAG_IMAGE_DIRECTION, R.drawable.participant);

            // TODO aktuallisiere Alarm receiver

        } else {
            if (imageDirection == R.drawable.somemap) {

                // Todo: open MapActivity from Meeting with startActivity with a Intent that has a Extra Integer with the key word MEETING_ID form Meeting class
                /*
                Intent mapIntent = new Intent(this, MapActivity.class);
                mapIntent.putExtra(Meeting.MEETING_ID_KEY, meeting.getId());
                startActivity(mapIntent);
                */

            } else {
                // should not be Called
                Toast.makeText(getApplicationContext(), "Error: Something unexpected happened", Toast.LENGTH_SHORT).show();

                //Todo remove this Toast TEST-----------------------------------------------------------------------------------------------------------------------------------------
                Toast.makeText(getApplicationContext(), "Wrong Button Image", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void meetingList_SecondButtonClickHandler(View view) {

        RelativeLayout meetingRow = (RelativeLayout) view.getParent();
        // should be in Try catch ? Nope because of the Unique Key R.id.TAG_MEETING
        final Meeting meeting = (Meeting) meetingRow.getTag(R.id.TAG_MEETING);

        //Todo remove this Toast TEST-----------------------------------------------------------------------------------------------------------------------------------------
        Toast.makeText(getApplicationContext(), meeting.getName() + " Button 2 was clicked", Toast.LENGTH_SHORT).show();

        ImageButton secondButton = (ImageButton) meetingRow.getChildAt(3);
        int imageDirection = (int) secondButton.getTag(R.id.TAG_IMAGE_DIRECTION);

        if (imageDirection == R.drawable.cancel) {
            // TODO AlertDialog doesn' work BufferOverflow!

            android.support.v7.app.AlertDialog alertDialog = buildCancelAlertDialog(meeting);
            alertDialog.show();

        } else {
            if (imageDirection == R.drawable.participant) {

                // Start MeetingParticipant with a extra (Meeting)
                MeetingParticipantActivity.start(this, meeting);
            } else {
                // should not be Called
                Toast.makeText(getApplicationContext(), "Something unexpected happened", Toast.LENGTH_SHORT).show();
                //Todo app schließen
            }
        }
    }

    private android.support.v7.app.AlertDialog buildCancelAlertDialog(Meeting m) {
        final Meeting meeting = m;
        android.support.v7.app.AlertDialog.Builder cancelADB = new android.support.v7.app.AlertDialog.Builder(context);
        cancelADB
                .setTitle(meeting.getName() + "")
                .setMessage("Wirklich " + meeting.getName() + " ablehnen?")
                .setCancelable(false)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Todo reason on BufferOverflow are this (LiveTime cant delete Meeting from List)
                        rejectMeeting(meeting);
                    }
                })
                .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                );
        return cancelADB.create();
    }

    public void rejectMeeting(Meeting meeting) {
        showProgressDialog();

        List<Participant> participants = meeting.getParticipants();
        Participant meAsParticipant = null;

        // Find me as Participant
        for (Participant p : participants) {

            if (p.getUser().getId() == myUser.getId()) {
                meAsParticipant = p;
            }
        }

        meAsParticipant.setConfirmation(MeetingConfirmation.REJECTED);

        String jparticipant = participantConverter.serialize(meAsParticipant, Participant.class);

        //create a MeetingService, that send a meeting conformation change (Cancel)
        Intent i = new Intent(this, MeetingParticipantManagementService.class);
        activityServiceResultReceiver = new ServiceResultReceiver(new Handler());
        activityServiceResultReceiver.setReceiver(this);
        i.putExtra(CommunicationKeys.RECEICER, activityServiceResultReceiver);
        i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.PUT);
        i.putExtra(CommunicationKeys.PARTICIPANT, jparticipant);
        startService(i);
    }


    public void showMeetingInfo(View v) {
        RelativeLayout meetingRow = (RelativeLayout) v.getParent();
        Meeting meeting = (Meeting) meetingRow.getTag(R.id.TAG_MEETING);

        Toast.makeText(this, "TODO create a MeetingService and put the Id: " + meeting.getMeetingId(), Toast.LENGTH_LONG);

        // get information_apoitment.xml as Java view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View infoApoitment = inflater.inflate(R.layout.information_apoitment, null, false);

        // set the Meeting information in information_apoitment xml
        TextView time = (TextView) infoApoitment.findViewById(R.id.meeting_info_time);
        TextView name = (TextView) infoApoitment.findViewById(R.id.meeting_info_name);
        TextView place = (TextView) infoApoitment.findViewById(R.id.meeting_info_adress);
        TextView creator = (TextView) infoApoitment.findViewById(R.id.info_Meeting_creator);

        // Convert TimeStampt with a Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(meeting.getTimestamp());

        time.setText("Am " + calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH) + "."
                + calendar.get(Calendar.YEAR) + " um " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));

        name.setText(meeting.getName());
        //Todo  GPS getPlace as String!
        place.setText("TODO Set place here !!!");
        creator.setText("Ersteller: " + meeting.getCreator().getUser().getName());

       /*   (Priority B)
        TextView memo = (TextView) infoApoitment.findViewById(R.id.info_MeetingMemo);
        memo.setText(meeting.getMemo);
        */

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MeetingListActivity.this);
        alertDialogBuilder.setView(infoApoitment);

        alertDialogBuilder.setCancelable(false)
                .setNegativeButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog and show it
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_meeting_meetingList:
                CreateNewMeetingActivity.start(this);
                return true;
            case R.id.groups_meetingList:
                GroupsActivity.start(this);
                return true;
            case R.id.settings_MeetingList:
                SettingsActivity.start(this);
                return true;
            case R.id.about_MeetingList:
                AboutActivity.start(this);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        // TODO i-eine schöne lösung finden für zwei Services oder String umwandeln

        switch (resultData.getString(CommunicationKeys.SERVICE)) {
            case CommunicationKeys.FROM_MEETINGS_SERVICE:
                switch (resultCode) {
                    case 200:
                        meetingsResultReceiverHandler(resultData);
                        break;
                    case 400:
                        Toast.makeText(this, "Error 400: bad request", Toast.LENGTH_LONG).show();
                        break;
                    case 403:
                        Toast.makeText(this, "Error 403: forbidden", Toast.LENGTH_LONG).show();
                        break;
                    case 404:
                        Toast.makeText(this, "Error 404: not found", Toast.LENGTH_LONG).show();

                    case 408:
                        Toast.makeText(this, "Error 408: request time out", Toast.LENGTH_LONG).show();
                        break;
                    case 500:
                        Toast.makeText(this, "Error 500: unexpected Error", Toast.LENGTH_LONG).show();
                        break;
                }
                hideProgressDialog();
                break;
            case CommunicationKeys.FROM_MEETING_PARTICIPANT_MANAGEMENT_SERVICE:
                switch (resultCode) {
                    case 202:
                        managementResultReceiverHandler(resultData);
                        hideProgressDialog();
                        break;
                    case 400:
                        Toast.makeText(this, "Error 400: bad request", Toast.LENGTH_LONG).show();
                    case 403:
                        Toast.makeText(this, "Error 403: forbidden", Toast.LENGTH_LONG).show();
                    case 404:
                        Toast.makeText(this, "Error 404: not found", Toast.LENGTH_LONG).show();
                    case 408:
                        Toast.makeText(this, "Error 408: request time out", Toast.LENGTH_LONG).show();
                    case 500:
                        Toast.makeText(this, "Error 500: unexpected Error", Toast.LENGTH_LONG).show();
                    default:
                        // it was not 202 Status Code!
                        // todo start MeetinsServer to actualize
                        startGetMeetingsService();
                        Log.d(TAG, "Wrong Status Code. Start MeetingsService");
                }
                break;
            default:
                hideProgressDialog();
                // TODO wrong Service
                startGetMeetingsService();
                Toast.makeText(this, "Error 500: wrong Service", Toast.LENGTH_LONG).show();
        }
    }

    private void managementResultReceiverHandler(Bundle resultData) {
        switch (resultData.getString(CommunicationKeys.COMMAND)) {
            case CommunicationKeys.GET:
                // don't need it here (Get all Meeting Info from MeetingsService)
                Log.d(TAG, "Catch GET Form MeetingParticipantManagent");
                break;
            case CommunicationKeys.DELETE:
                Log.d(TAG, "Catch DELETE Form MeetingParticipantManagent");
                // Delete a Participant (Prio B)
                break;
            case CommunicationKeys.PUT:
                // Confirmation changed

                Log.d(TAG, "Catch PUT Form MeetingParticipantManagent");
                Toast.makeText(this, "Zustimmung zum Termin geändert\nAktualisieren Sie bitte", Toast.LENGTH_LONG).show();
                break;
            case CommunicationKeys.POST:
                Log.d(TAG, "Catch POST Form MeetingParticipantManagent");
                // add a Listed Participants (Prio B)
                break;

            default:
                Log.d(TAG, "Wrong Command MeetingParticipantManagent");

                // TODO ERROR wrong Command from Service

        }
    }

    private void startGetMeetingsService() {
        showProgressDialog();
        Intent i = new Intent(this, MeetingsService.class);
        activityServiceResultReceiver = new ServiceResultReceiver(new Handler());
        activityServiceResultReceiver.setReceiver(this);
        i.putExtra(CommunicationKeys.RECEICER, activityServiceResultReceiver);
        i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.GET);
        startService(i);


        // TODO Delete this TOast
        Toast.makeText(this, "MeetinsService Started", Toast.LENGTH_LONG).show();
    }

    private void meetingsResultReceiverHandler(Bundle resultData) {
        switch (resultData.getString(CommunicationKeys.COMMAND)) {
            case CommunicationKeys.GET:
                meetings = new ArrayList<>();
                String jsonString = resultData.getString(CommunicationKeys.MEETINGS);

                // Todo Test----------------------
               //  List<Meeting> fullMeetingList = meetingListConverter.deserializeList(jsonString, Meeting[].class);
                // this is a test
                List<Meeting> fullMeetingList = fullMeeting;
                // TODo TEST-----------------------------------

                // TODO FILTER meetings if they are Rejected from User-------------------------------------------------------------------------------

                for (Meeting m : fullMeetingList) {

                    List<Participant> participants = m.getParticipants();
                    for (Participant p : participants) {
                        if (p.getUser().getId() == myUser.getId()) {
                            if (p.getConfirmation() != MeetingConfirmation.REJECTED) {
                                meetings.add(m);
                            }
                        }
                    }
                }
                MeetingListAdapter adapter = new MeetingListAdapter(this, meetings);
                list.setAdapter(adapter);
                // TODO Test ?? list.invalidateViews();
                Log.d(TAG, "Catch GET Form MeetinsService");

                break;
            default:
                Log.d(TAG, "Catch Wrong Command Form MeetinsService");
                Toast.makeText(this, "Error: 500. Wrong Command From MeetingService", Toast.LENGTH_SHORT).show();
                // TODO ERROR wrong Command from Service
        }
        hideProgressDialog();
    }


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Lädt...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
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

    private List<Meeting> fullMeeting = new ArrayList<Meeting>() {
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



class MeetingListAdapter extends ArrayAdapter<Meeting> {
    Context context;

    List<Meeting> meetings;

    private final int checkedImageId = R.drawable.checked;
    private final int cancelImageId = R.drawable.cancel;
    private final int mapImageId = R.drawable.somemap;
    private final int participantImageId = R.drawable.participant;


    MeetingListAdapter(Context c, List<Meeting> meetings) {
        super(c, R.layout.meeting_row, R.id.meetingName,meetings);

        this.context = c;

        this.meetings = meetings;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //make the xml layout into a java object
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View meetingRow = inflater.inflate(R.layout.meeting_row, parent, false);

        Meeting m = meetings.get(position);
        meetingRow.setTag(R.id.TAG_MEETING, m);


        //deputy the buttons and TextViews as a java objects
        ImageButton button1 = (ImageButton) meetingRow.findViewById(R.id.button1);
        ImageButton button2 = (ImageButton) meetingRow.findViewById(R.id.button2);
        TextView name = (TextView) meetingRow.findViewById(R.id.meetingName);
        TextView time = (TextView) meetingRow.findViewById(R.id.meetingTime);


        name.setText(m.getName());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(m.getTimestamp());
        time.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));

        // TODO the date------------------------------------------------------------------------------------------------------------------


        //set the images from the Buttons for each Meeting Confirmation
        // TODO search for each Meeting the Youselfe as Partcipant

        Participant participant = m.getParticipants().get(0);

        // Confirmed
        if (participant.getConfirmation() == MeetingConfirmation.CONFIRMED) {

            button1.setImageResource(mapImageId);
            button1.setTag(R.id.TAG_IMAGE_DIRECTION, mapImageId);

            button2.setImageResource(participantImageId);
            button2.setTag(R.id.TAG_IMAGE_DIRECTION, participantImageId);

        } else {
            // Pending
            if (participant.getConfirmation() == MeetingConfirmation.PENDING) {
                button1.setImageResource(checkedImageId);
                button1.setTag(R.id.TAG_IMAGE_DIRECTION, checkedImageId);

                button2.setImageResource(cancelImageId);
                button2.setTag(R.id.TAG_IMAGE_DIRECTION, cancelImageId);
            } else {
                /* Todo REJECTED Meeting should not be here or
Todo                             delete alle Rejected Meetings bevor calling the adapteritsMeConfirmed
                */
                return null;
            }
        }

        return meetingRow;
    }
}


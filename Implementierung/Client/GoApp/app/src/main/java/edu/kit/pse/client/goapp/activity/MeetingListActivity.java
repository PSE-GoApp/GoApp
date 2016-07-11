package edu.kit.pse.client.goapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
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
import edu.kit.pse.client.goapp.datamodels.Meeting;
import edu.kit.pse.client.goapp.datamodels.MeetingConfirmation;
import edu.kit.pse.client.goapp.datamodels.Participant;
import edu.kit.pse.client.goapp.datamodels.User;
import edu.kit.pse.client.goapp.service.MeetingParticipantManagementService;
import edu.kit.pse.goapp.client.goapp.R;

public class MeetingListActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ServiceResultReceiver.Receiver {

    private ServiceResultReceiver meetingListReceiver;
    private ObjectConverter<List<Meeting>> meetingListConverter;
    private ObjectConverter<Meeting> meetingConverter;
    ImageButton menu_button;
    private Context context = this;
    private ListView list;

    // Example ---------------------------------------------------------------------------------------------------------------------------------------------
    // Todo delete it after Testing
    User me = new User(42, "GO-App Admin");
    // private int[] imageId = {R.drawable.checked, R.drawable.cancel, R.drawable.somemap, R.drawable.participant};

    Participant itsMeConfirmed = new Participant(0,2,  me, MeetingConfirmation.CONFIRMED);
    Participant itsMePending = new Participant(0,3, me, MeetingConfirmation.PENDING);
    Participant imParticipant = new Participant(0,3, me, MeetingConfirmation.REJECTED);

    private List<Meeting> meetings = new ArrayList<Meeting>() {
        {
            new Meeting(0, "Mensa", null, 1475953024180L, 2, imParticipant) {{
                addParticipant(itsMeConfirmed);
            }};
            add(new Meeting(1, "Ago", null, 1475953024000L, 2, imParticipant) {{
                addParticipant(itsMePending);
            }});
            add(new Meeting(2, "PSE Treffen", null, 1475953021200L, 2, imParticipant) {{
                addParticipant(itsMePending);
            }});
            add(new Meeting(3, "Iris Füttern", null, 1476953024000L, 2, imParticipant) {{
                addParticipant(itsMePending);
            }});
            add(new Meeting(4, "Schloss Park", null, 1475953021200L, 2, imParticipant) {{
                addParticipant(itsMeConfirmed);
            }});
            add(new Meeting(5, "Klettern", null, 147595302489L, 2, imParticipant) {{
                addParticipant(itsMePending);
            }});
            add(new Meeting(6, "Bar Tour", null, 14759530243560L, 2, imParticipant) {{
                addParticipant(itsMeConfirmed);
            }});
        }
    };

    //------------------------------------------------------------------------------------------------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);
        menu_button = (ImageButton) findViewById(R.id.menu_termine);
        menu_button.setOnClickListener(this);
        meetingListConverter = new ObjectConverter<>();

        // TODO aus auklammern

        // Only For the Test Todo delete this after Testing
        if (meetings != null) {
            list = (ListView) findViewById(R.id.meeting_ListView);

            MeetingListAdapter adapter = new MeetingListAdapter(this, meetings);
            list.setAdapter(adapter);
        }
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

        /*
        else if (v.getId() == R.id.teilnehmer) {
           // TeilnehmerActivity.start(this);
        }else if (v.getId() == R.id.map) {
           // MapActivity.start(this);
        }else if (v.getId() == R.id.textView2){
            // get prompts.xml view
            LayoutInflater layoutInflater = LayoutInflater.from(MeetingListActivity.this);
            View promptView = layoutInflater.inflate(R.layout.information_apoitment, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MeetingListActivity.this);
            alertDialogBuilder.setView(promptView);

            alertDialogBuilder.setCancelable(false)
                    .setNegativeButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            // create an alert dialog
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
        */
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

            //Todo: create a MeetingService, that send a meeting conformation change (Accepted)
            Intent i = new Intent(this, MeetingParticipantManagementService.class);
            meetingListReceiver = new ServiceResultReceiver(new Handler());
            meetingListReceiver.setReceiver(this);
            i.putExtra(CommunicationKeys.RECEICER, meetingListReceiver);
            // TODO Klären was mitgegeben wird-------------------------------------------------------------------------------------------------------
            i.putExtra(CommunicationKeys.COMMAND, "");
            i.putExtra(CommunicationKeys.MEETING_ID, "");
            startService(i);

            //Todo        if the Request was successful change the Buttons
            //Todo        or change the ArrayList confirmation and go list.invalidateViews();
            //TOdo        or make a new MeetingsService.

            // change buttons to a accepted Meeting.
            firstButton.setImageResource(R.drawable.somemap);
            firstButton.setTag(R.id.TAG_IMAGE_DIRECTION, R.drawable.somemap);

            secondButton = (ImageButton) meetingRow.getChildAt(3);
            secondButton.setImageResource(R.drawable.participant);
            secondButton.setTag(R.id.TAG_IMAGE_DIRECTION, R.drawable.participant);

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

            // create a new AlertDialog.Builder
            android.support.v7.app.AlertDialog alertDialog = buildCancelAlertDialog(meeting);
            alertDialog.show();


        } else {
            if (imageDirection == R.drawable.participant) {

                // Start MeetingParticipant with a extra (Meeting ID)
                Intent mParticipantIntent = new Intent(view.getContext(), MeetingParticipantActivity.class);

                Bundle bundle = new Bundle();

                // TODO ERROR ! Exception !
                String meetingJson = meetingConverter.serialize(meeting,Meeting.class);
                bundle.putInt(CommunicationKeys.MEETING_ID, meeting.getId());
                bundle.putString(CommunicationKeys.MEETING, meetingJson);

                mParticipantIntent.putExtras(bundle);

                startActivity(mParticipantIntent);
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
                        cancelMeeting(meeting);
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

    private void cancelMeeting(Meeting meeting) {

        //Todo: create a MeetingService, that send a meeting conformation change (Cancel)
        int meetingId = meeting.getId();

        Intent i = new Intent(this, MeetingParticipantManagementService.class);
        meetingListReceiver = new ServiceResultReceiver(new Handler());
        meetingListReceiver.setReceiver(this);
        i.putExtra(CommunicationKeys.RECEICER, meetingListReceiver);
        // TODO Klären was mitgegeben wird-------------------------------------------------------------------------------------------------------
        i.putExtra(CommunicationKeys.COMMAND, "GET");
        // i.putExtra(CommunicationKeys.MEETING_ID, meetingId);
        startService(i);

        // Todo remove this Toast Test ----------------------------------------------------------------------------------------------------------------------------------------
        Toast.makeText(getApplicationContext(), " Service delete Meeting with ID: " + meeting.getId(), Toast.LENGTH_SHORT).show();

        // Todo Button click lock, untill the request is finished. Than remove it from the list.
        //Remove Item from the List
        if (!meetings.remove(meeting)) {
            Toast.makeText(getApplicationContext(), "Error: Meeting are not existing.\nPlease restart the app", Toast.LENGTH_SHORT).show();
        }
        list.invalidateViews();

        //Todo remove this Toast Test ---------------------------------------------------------------------------------------------------------------------------------
        Toast.makeText(getApplicationContext(), "Meeting was removed from List", Toast.LENGTH_SHORT).show();
    }


    public void showMeetingInfo(View v) {
        RelativeLayout meetingRow = (RelativeLayout) v.getParent();
        Meeting meeting = (Meeting) meetingRow.getTag(R.id.TAG_MEETING);

        Toast.makeText(this, "TODO create a MeetingService and put the Id: " + meeting.getId(), Toast.LENGTH_LONG);

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
                //AboutActivity.start(this);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        // TODO i-eine schöne lösung finden für zwei Services oder String umwandeln

        switch (resultCode) {
            case 202:
                switch (resultData.getString(CommunicationKeys.SERVICE)) {
                    case CommunicationKeys.FROM_MEETINGS_SERVICE:
                        meetingsResultReceiverHandler(resultData);
                        break;
                    case CommunicationKeys.FROM_MEETING_PARTICIPANT_MANAGEMENT_SERVICE:
                        managementResultReceiverHandler(resultData);
                        break;
                    case CommunicationKeys.FROM_MEETING_SERVICE:
                        meetingResultReceiverHandler(resultData);
                        break;
                    default:
                        // TODO wrong Service
                }
                break;
            case 400:
                Toast.makeText(this, "Error 400: Missing Informations", Toast.LENGTH_LONG).show();
                break;
            case 403:
                Toast.makeText(this, "Error 403: Missing Informations", Toast.LENGTH_LONG).show();
                break;
            case 408:
                Toast.makeText(this, "Error 408: Missing Informations", Toast.LENGTH_LONG).show();
                break;
            case 500:
                Toast.makeText(this, "Error 500: unexpected Error", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void managementResultReceiverHandler(Bundle resultData) {
        switch (resultData.getString(CommunicationKeys.COMMAND)) {
            case CommunicationKeys.GET:
                // don't need it here (Get all Meeting Info from MeetingsService)
                break;
            case CommunicationKeys.DELETE:
                // Delete a Participant (Prio B)
                break;
            case CommunicationKeys.PUT:
                // Confirmation changed
                // Todo create a Toast or AlertBuilder
                Toast.makeText(this, "Zustimmung zum Termin geändert", Toast.LENGTH_LONG).show();
                break;
            case CommunicationKeys.POST:
                // add a Listed Participants (Prio B)
                break;

            default:
                // TODO ERROR wrong Command from Service

        }
    }

    private void meetingResultReceiverHandler(Bundle resultData) {
        switch (resultData.getString(CommunicationKeys.COMMAND)) {
            case CommunicationKeys.GET:
                // don't need it here (Get Meeting Info)
                break;
            case CommunicationKeys.DELETE:
                // Delete Meeting
                // Todo create a Toast or AlertBuilder
                break;
            case CommunicationKeys.PUT:
                // Meeting changed
                // Todo create a Toast or AlertBuilder
                break;
            case CommunicationKeys.POST:
                // create a new meeting needn' that
                break;

            default:
                // TODO ERROR wrong Command from Service

        }
    }

    private void meetingsResultReceiverHandler(Bundle resultData) {
        switch (resultData.getString(CommunicationKeys.COMMAND)) {
            case CommunicationKeys.GET:
                String jsonString = resultData.getString(CommunicationKeys.MEETINGS);

                List<Meeting> mdump = new ArrayList<>();
                //ArrayList<Meeting> mList = new ArrayList<>();
                meetings = meetingListConverter.deserialize(jsonString, (Class<List<Meeting>>) mdump.getClass());
                /*
                for (int i = 0; i < meetings.size(); i++) {
                    List<Participant> par = meetings.get(i).getParticipants()
                    for (int i = 0; i < par.size(); i++) {

                    }
                }
                */
                break;
            default:
                // TODO ERROR wrong Command from Service

        }
    }


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


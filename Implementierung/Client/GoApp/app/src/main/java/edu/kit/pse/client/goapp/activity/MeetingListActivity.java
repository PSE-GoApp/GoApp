package edu.kit.pse.client.goapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.Arrays;
import java.util.List;

import edu.kit.pse.client.goapp.datamodels.Meeting;
import edu.kit.pse.client.goapp.datamodels.MeetingConfirmation;
import edu.kit.pse.client.goapp.datamodels.Participant;
import edu.kit.pse.client.goapp.datamodels.User;
import edu.kit.pse.goapp.client.goapp.R;

public class MeetingListActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    //something
    ImageButton menu_button;

    private Context context = this;
    private ListView list;
    // private int[] imageId = {R.drawable.checked, R.drawable.cancel, R.drawable.somemap, R.drawable.participant};


    // Example ---------------------------------------------------------------------------------------------------------------------------------------------
    // Todo delete it after Testing
    private long timestampExample = 2000;

    User me = new User(42, "GO-App Admin");
    Participant itsMeConfirmed = new Participant(0, me,MeetingConfirmation.CONFIRMED);
    Participant itsMePending = new Participant(0, me,MeetingConfirmation.PENDING);
    Participant imParticipant = new Participant(0, me,MeetingConfirmation.REJECTED);

    private ArrayList<Meeting> meetings = new ArrayList<Meeting>(Arrays.asList(new Meeting[]{
            new Meeting(0, "Mensa", null, timestampExample,  2 , imParticipant) {{addParticipant(itsMeConfirmed);}},
            new Meeting(1, "Ago", null, timestampExample,  2 , imParticipant) {{addParticipant(itsMePending);}},
            new Meeting(2, "PSE Treffen", null, timestampExample,  2 , imParticipant) {{addParticipant(itsMePending);}},
            new Meeting(3, "Iris Füttern", null, timestampExample,  2 , imParticipant) {{addParticipant(itsMePending);}},
            new Meeting(4, "Schloss Park", null, timestampExample,  2 , imParticipant) {{addParticipant(itsMeConfirmed);}},
            new Meeting(5, "Klettern", null, timestampExample,  2 , imParticipant) {{addParticipant(itsMePending);}},
            new Meeting(6, "Bar Tour", null, timestampExample,  2 , imParticipant) {{addParticipant(itsMeConfirmed);}}
    }));

    //------------------------------------------------------------------------------------------------------------------------------------------------



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);
        menu_button = (ImageButton) findViewById(R.id.menu_termine);
        menu_button.setOnClickListener(this);

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

            // change buttons to a accepted Meeting.
            firstButton.setImageResource(R.drawable.somemap);

            secondButton = (ImageButton) meetingRow.getChildAt(3);
            secondButton.setImageResource(R.drawable.participant);

        } else {
            if (imageDirection == R.drawable.somemap) {

                // Todo: open MapActivity from Meeting
                //     Toast.makeText(getApplicationContext(), meetings.get(meetingID).getMeetingName() + " map starten", Toast.LENGTH_SHORT).show();
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
                // Todo: open ParticipantList Popup/Activity
                Toast.makeText(getApplicationContext(), "TODO: show ParticipantList", Toast.LENGTH_SHORT).show();

            }
            else {
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

        // Todo remove this Toast Test ----------------------------------------------------------------------------------------------------------------------------------------
        Toast.makeText(getApplicationContext()," Service delete Meeting with ID: " + meeting.getId(), Toast.LENGTH_SHORT).show();

        // Todo Button click lock, untill the request is finished. Than remove it from the list.
        //Remove Item from the List
        if (!meetings.remove(meeting)) {
            Toast.makeText(getApplicationContext(),"Error: Meeting are not existing.\nPlease restart the app", Toast.LENGTH_SHORT).show();
        }
        list.invalidateViews();

        //Todo remove this Toast Test ---------------------------------------------------------------------------------------------------------------------------------
        Toast.makeText(getApplicationContext(), "Meeting was removed from List", Toast.LENGTH_SHORT).show();
    }


    public void showMeetingInfo(View v) {
        // Todo Test this!

        RelativeLayout meetingRow = (RelativeLayout) v.getParent();

        // Todo: do it as a MeetingService
        final Meeting meeting = (Meeting) meetingRow.getTag(R.id.TAG_MEETING);
        // final Meeting meeting = MeetingService.blablabla


        // set the Meeting information in information_apoitment xml
        TextView time = (TextView) findViewById(R.id.meeting_info_time);
        // Todo TimeStamp!
        time.setText(Long.toString(meeting.getTimespamp()));


        TextView name = (TextView) findViewById(R.id.meeting_info_name);
        name.setText(meeting.getName());
        TextView place = (TextView) findViewById(R.id.meeting_info_adress);

        //Todo  GPS getPlace as String!
        place.setText( "EXAMPLE PLACE TODO THIS !!!" );

        TextView creator = (TextView) findViewById(R.id.info_Meeting_creator);
        creator.setText("Ersteller: " + meeting.getCreator().getUser().getName());

       /*   (Priority B)
        TextView memo = (TextView) findViewById(R.id.info_MeetingMemo);
        memo.setText(meeting.getMemo);
        */


        // get information_apoitment.xml as Java view
        LayoutInflater layoutInflater = LayoutInflater.from(MeetingListActivity.this);
        View information_apoitment = layoutInflater.inflate(R.layout.information_apoitment, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MeetingListActivity.this);
        alertDialogBuilder.setView(information_apoitment);

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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.neuer_termin_main:
                //TerminActivity.start(this);
                return true;
            case R.id.groups_main:
                GroupsActivity.start(this);
                return true;
            case R.id.settings_main:
                //SettingsActivity.start(this);
                return true;
            case R.id.about_main:
                //AboutActivity.start(this);
                return true;
            default:
                return false;
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
        super(c, R.layout.meeting_row, R.id.meetingName, meetings);

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
        time.setText(Long.toString(m.getTimespamp()));

        //set the images from the Buttons for each Meeting Confirmation
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
            }
        }

        return meetingRow;
    }
}

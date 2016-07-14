package edu.kit.pse.client.goapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.ServiceResultReceiver;
import edu.kit.pse.client.goapp.converter.ObjectConverter;
import edu.kit.pse.client.goapp.datamodels.Meeting;
import edu.kit.pse.client.goapp.datamodels.Participant;
import edu.kit.pse.client.goapp.datamodels.User;
import edu.kit.pse.goapp.client.goapp.R;

/**
 * This Activity shows a List of MeetingParticipants who have confirmed the meeting
 * Created by kansei on 07.07.16.
 */
public class MeetingParticipantActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ServiceResultReceiver.Receiver {

    public ServiceResultReceiver meetingParticipantReceiver;
    ImageButton menu_button;
    Button cancelButton;
    ObjectConverter<Meeting> meetingConverter;

    Bundle bundleMeeting;
    private static Meeting meeting;

    ListView list;

    // Todo Delete it after Testing

    List<Participant> participants = new ArrayList<Participant>() {{
            add(new Participant(0,2, new User(42, "ICH BIN DAS"), null));
            add(new Participant(0,2, new User(0, "Super Heroooo Ohhh"), null));
            add(new Participant(0,2, new User(1, "Niemand"), null));
        }};

    public static void start(Activity activity, Meeting m) {
        Intent intent = new Intent(activity, MeetingParticipantActivity.class);
        meeting = m;
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_participant);
        menu_button = (ImageButton) findViewById(R.id.menu_meeting_participant);
        menu_button.setOnClickListener(this);
        cancelButton = (Button) findViewById(R.id.meeting_participant_cancel);
        meetingConverter = new ObjectConverter<>();


        Toast.makeText(MeetingParticipantActivity.this, "Meeting ID is: "
                + meeting.getMeetingId() , Toast.LENGTH_SHORT).show();


        participants = meeting.getParticipants();
        if (participants != null) {
            list = (ListView) findViewById(R.id.participant_listview);

            ParticipantListAdapter adapter = new ParticipantListAdapter(this, participants);
            list.setAdapter(adapter);
        }
        if (meeting.getTimestamp() >= System.currentTimeMillis()) {
            cancelButton.setTag("Termin verlassen");
        }


    }



    public void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(MeetingParticipantActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_meeting_participant, popup.getMenu());
        popup.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu_meeting_participant) {
            showPopUp(v);
        }
        if (v.getId() == R.id.meeting_participant_cancel) {


            //create a MeetingService, that send a meeting conformation change (Accepted)
          /* TODO GOOGLE TOKEN == participant ----------------------------------------------------------------------------
            List<Participant> participants = meeting.getParticipants();
            Participant meAsParticipant = null;
            GOOGLETOKEN itsMe;
            for (Participant p : participants) {
                if (p "Vergleichen" itsMe) {
                    p.setConfirmation(MeetingConfirmation.CONFIRMDE);
                    meAsParticipant = p;
                } else {
                    Log.d("Error", "Error: Filtering Participants or unexpected has happen");
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
            // TODO change AlarmReceiver and SQL Data -----------------------------------------------------------------------
            */

            // TODO warning if they are sure to leave the Meeting (AlertDialog)
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.meetingList_participant:
                MeetingListActivity.start(this);
                return true;
            case R.id.new_meeting_participant:
                CreateNewMeetingActivity.start(this);
                return true;
            case R.id.groups_participant:
                GroupsActivity.start(this);
                return true;
            case R.id.settings_participant:
                SettingsActivity.start(this);
                return true;
            case R.id.about_participant:
                // AboutActivity.start(this);
                return true;
            default:
                return false;
        }
    }

    // TODO TEST THIS
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultData.getString(CommunicationKeys.SERVICE)) {
            case CommunicationKeys.FROM_MEETING_PARTICIPANT_MANAGEMENT_SERVICE:
                meetingParticipanManagemantResultHandler(resultCode, resultData);
                break;
            case CommunicationKeys.FROM_MEETING_SERVICE:
                // not in use
                break;
            default:
                Toast.makeText(this, "Error 500 wrong Service", Toast.LENGTH_LONG).show();

        }
    }

    private void meetingParticipanManagemantResultHandler(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case 202:
                Toast.makeText(this,"202", Toast.LENGTH_SHORT);
                String jsonString = resultData.getString(CommunicationKeys.MEETING);
                meeting = meetingConverter.deserialize(jsonString,Meeting.class);
                participants = meeting.getParticipants();
                if (participants != null) {
                    list = (ListView) findViewById(R.id.participant_listview);

                    ParticipantListAdapter adapter = new ParticipantListAdapter(this, participants);
                    list.setAdapter(adapter);
                    if (meeting.getTimestamp() >= System.currentTimeMillis()) {
                        cancelButton.setTag("Termin verlassen");
                    }
                } else {
                    Toast.makeText(this, "Error 500: Missing Informations", Toast.LENGTH_LONG).show();
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


}

class ParticipantListAdapter extends ArrayAdapter<Participant> {
    Context context;

    List<Participant> participants;


    ParticipantListAdapter(Context c, List<Participant> participants) {
        super(c, R.layout.participant_row, R.id.participant_row_name,participants);

        this.context = c;

        this.participants = participants;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //make the xml layout into a java object
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View participantRow = inflater.inflate(R.layout.participant_row, parent, false);

        Participant partici = participants.get(position);
        participantRow.setTag(R.id.TAG_PARTICIPANT_ID, partici.getParticipantId());

        //deputy the buttons and TextViews as a java objects
        TextView name = (TextView) participantRow.findViewById(R.id.participant_row_name);

        name.setText(partici.getUser().getName());

        return participantRow;
    }
}



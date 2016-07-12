package edu.kit.pse.client.goapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import edu.kit.pse.client.goapp.service.MeetingParticipantManagementService;
import edu.kit.pse.goapp.client.goapp.R;

/**
 * Created by kansei on 07.07.16.
 */
public class MeetingParticipantActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ServiceResultReceiver.Receiver {

    public ServiceResultReceiver meetingParticipantReceiver;
    ImageButton menu_button;
    Button cancelButton;
    ObjectConverter<Meeting> meetingConverter;

    Bundle bundleMeeting;
    Meeting meeting;

    ListView list;

    // Todo Delete it after Testing

    List<Participant> participants = new ArrayList<Participant>() {{
            add(new Participant(0,2, new User(42, "ICH BIN DAS"), null));
            add(new Participant(0,2, new User(0, "Super Heroooo Ohhh"), null));
            add(new Participant(0,2, new User(1, "Niemand"), null));
        }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_participant);
        menu_button = (ImageButton) findViewById(R.id.menu_meeting_participant);
        menu_button.setOnClickListener(this);
        cancelButton = (Button) findViewById(R.id.meeting_participant_cancel);
        meetingConverter = new ObjectConverter<>();

        bundleMeeting = getIntent().getExtras();
        String json = bundleMeeting.getString(CommunicationKeys.MEETING);
        meeting  = meetingConverter.deserialize(json, Meeting.class);

        /*
        // Start MeetingServices for the Meeting Information
        Intent i = new Intent(this, MeetingService.class);
        meetingParticipantReceiver = new ServiceResultReceiver(new Handler());
        meetingParticipantReceiver.setReceiver(this);
        i.putExtra(CommunicationKeys.RECEICER, meetingParticipantReceiver);
        i.putExtra(CommunicationKeys.COMMAND, "GET");
        i.putExtra(CommunicationKeys.MEETING_ID, bundleMeetingId.getInt(CommunicationKeys.MEETING_ID));
        startService(i);
        */

        Toast.makeText(MeetingParticipantActivity.this, "Meeting ID is: "
                + meeting.getId() , Toast.LENGTH_SHORT).show();


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


            // Todo Start MeetingManagementServices for leave the Meeting (delete)
            Intent i = new Intent(this, MeetingParticipantManagementService.class);
            meetingParticipantReceiver = new ServiceResultReceiver(new Handler());
            meetingParticipantReceiver.setReceiver(this);
            i.putExtra(CommunicationKeys.RECEICER, meetingParticipantReceiver);
            i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.DELETE);
            i.putExtra(CommunicationKeys.MEETING_ID, meeting.getId());
            startService(i);

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

    // TODO TEST THIS SHHHIEEEEEEEEEEEAAAAAAAAT
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



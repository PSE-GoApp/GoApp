package edu.kit.pse.client.goapp.activity;

import android.content.Context;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.kit.pse.client.goapp.datamodels.Participant;
import edu.kit.pse.client.goapp.datamodels.User;
import edu.kit.pse.goapp.client.goapp.R;

/**
 * Created by kansei on 07.07.16.
 */
public class MeetingParticipantActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    public static final String MEETING_ID_KEY = "MEETING_ID";
    ImageButton menu_button;

    Bundle extra;

    ListView list;

    // Todo Delete it after Testing



    ArrayList<Participant> participants = new ArrayList<Participant>(Arrays.asList(new Participant[] {
            new Participant(0, new User(42, "ICH BIN DAS"), null),
            new Participant(0, new User(0, "Super Heroooo Ohhh"), null),
            new Participant(0, new User(1, "Niemand"), null)} ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_participant);
        menu_button = (ImageButton) findViewById(R.id.menu_meeting_participant);
        menu_button.setOnClickListener(this);

        if (participants != null) {
            list = (ListView) findViewById(R.id.participant_listview);

            ParticipantListAdapter adapter = new ParticipantListAdapter(this, participants);
            list.setAdapter(adapter);
        }

        extra = getIntent().getExtras();
        Toast.makeText(MeetingParticipantActivity.this, "Meeting ID is: " + Integer.toString(extra.getInt(MEETING_ID_KEY)) , Toast.LENGTH_SHORT).show();

        // Todo create a service
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
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.meetingList_participant:
                MeetingListActivity.start(this);
                return true;
            case R.id.new_meeting_participant:
                NewMeetingActivity.start(this);
                return true;
            case R.id.groups_participant:
                GroupsActivity.start(this);
                return true;
            case R.id.settings_participant:
                // SettingsActivity.start(this);
                return true;
            case R.id.about_participant:
                // AboutActivity.start(this);
                return true;
            default:
                return false;
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



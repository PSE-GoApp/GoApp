package edu.kit.pse.client.goapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
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
import edu.kit.pse.client.goapp.databaseadapter.DataBaseAdapter;
import edu.kit.pse.client.goapp.datamodels.Meeting;
import edu.kit.pse.client.goapp.datamodels.MeetingConfirmation;
import edu.kit.pse.client.goapp.datamodels.Participant;
import edu.kit.pse.client.goapp.service.MeetingParticipantManagementService;
import edu.kit.pse.goapp.client.goapp.R;

/**
 * This Activity shows a List of MeetingParticipants who have confirmed the meeting
 * Created by kansei on 07.07.16.
 */
public class MeetingParticipantActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ServiceResultReceiver.Receiver {

    public ServiceResultReceiver activityServiceResultReceiver;
    ImageButton menu_button;
    Button cancelButton;

    private final static String TAG = "MeetingParticipant";

    private static Meeting meeting;

    private ListView list;

    private ObjectConverter<Participant> participantConverter;

    private List<Participant> participants = new ArrayList<Participant>();

    private ProgressDialog mProgressDialog;

    private int myUserId;

    private Participant meAsParticipant;

    private Context context = this;

    private DataBaseAdapter dbAdapter;


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

        participantConverter = new ObjectConverter<>();

        Toast.makeText(MeetingParticipantActivity.this, "Meeting ID is: "
                + meeting.getMeetingId() , Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        myUserId = sharedPreferences.getInt("userId", -1);

        if (myUserId == -1) {
            // todo app schließen oder neu laden / AlertBuilder
            Toast.makeText(this, "No Client User Id found",Toast.LENGTH_LONG).show();
            Log.e(TAG, "No Client User Id found");
        }



        List<Participant> tempParticipant = meeting.getParticipants();
        if (tempParticipant != null) {
            for (Participant p : tempParticipant) {

                if (p.getConfirmation() == MeetingConfirmation.CONFIRMED ) {
                    participants.add(p);
                    if (p.getUser().getId() == myUserId) {
                        meAsParticipant = p;
                    }
                }
            }
            list = (ListView) findViewById(R.id.participant_listview);

            ParticipantListAdapter adapter = new ParticipantListAdapter(this, participants);
            list.setAdapter(adapter);
        }
        if (meeting.getTimestamp() >= System.currentTimeMillis()) {
            cancelButton.setTag("Termin verlassen");
        }

        dbAdapter = new DataBaseAdapter(context);
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
            showProgressDialog();
            startCancelService();


            // TODO warning if they are sure to leave the Meeting (AlertDialog)
        }
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

    private void startCancelService() {
        //create a MeetingService, that send a meeting conformation change (Accepted)

            // StarteService MeetingPartic.Manager put
            meAsParticipant.setConfirmation(MeetingConfirmation.REJECTED);
            String jparticipant = participantConverter.serialize(meAsParticipant, Participant.class);

            Intent i = new Intent(this, MeetingParticipantManagementService.class);
            activityServiceResultReceiver = new ServiceResultReceiver(new Handler());
            activityServiceResultReceiver.setReceiver(this);
            i.putExtra(CommunicationKeys.RECEICER, activityServiceResultReceiver);
            i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.PUT);
            i.putExtra(CommunicationKeys.PARTICIPANT, jparticipant);
            startService(i);

        // TODO change AlarmReceiver and SQL Data -------------------------------------------------------------------------------------------------

        dbAdapter.deleteRow(meeting.getMeetingId());
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
                AboutActivity.start(this);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {
            case 200:
                switch (resultData.getString(CommunicationKeys.SERVICE)) {
                    case CommunicationKeys.FROM_MEETING_PARTICIPANT_MANAGEMENT_SERVICE:
                        meetingParticipanManagemantResultHandler(resultData);
                        break;
                    default:
                        hideProgressDialog();
                        Toast.makeText(this, "Error 500: wrong Service", Toast.LENGTH_LONG).show();

                }
                break;
            case 400:
                hideProgressDialog();
                Toast.makeText(this, "Error 400: bad request", Toast.LENGTH_LONG).show();
                break;
            case 403:
                hideProgressDialog();
                Toast.makeText(this, "Error 403: forbidden", Toast.LENGTH_LONG).show();
                break;
            case 404:
                hideProgressDialog();
                Toast.makeText(this, "Error 404: not found", Toast.LENGTH_LONG).show();

            case 408:
                hideProgressDialog();
                Toast.makeText(this, "Error 408: request time out", Toast.LENGTH_LONG).show();
                break;
            case 500:
                hideProgressDialog();
                Toast.makeText(this, "Error 500: unexpected Error", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(this, "Error "+ resultCode +": unexpected Error", Toast.LENGTH_LONG).show();

        }
    }

    private void meetingParticipanManagemantResultHandler(Bundle resultData) {

        switch (resultData.getString(CommunicationKeys.COMMAND)) {
            case CommunicationKeys.PUT: // Leave the Meeting

                Toast.makeText(this,meeting.getName() + " Termin abgesagt", Toast.LENGTH_LONG).show();

                MeetingListActivity.start(this);

                Log.d(TAG,"PUT catch");
                break;

            case CommunicationKeys.GET:
                Log.d(TAG,"GET catch (wrong Command)");
                Toast.makeText(this, "Error 500: no GET started", Toast.LENGTH_LONG).show();
                break;

            case CommunicationKeys.POST:
                Toast.makeText(this, "Error 500: no Post started", Toast.LENGTH_LONG).show();
                Log.d(TAG,"POST catch (wrong Command)");
                break;

            case CommunicationKeys.DELETE:
                Toast.makeText(this, "Error 403: no Delete started", Toast.LENGTH_LONG).show();
                Log.d(TAG,"Delete catch (wrong Command)");
                break;

            default:
                Log.d(TAG,"ERROR 500: no Command catch");
        }
        hideProgressDialog();
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



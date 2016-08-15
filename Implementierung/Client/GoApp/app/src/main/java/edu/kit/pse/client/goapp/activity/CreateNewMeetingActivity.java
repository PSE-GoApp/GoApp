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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import edu.kit.pse.client.goapp.datamodels.Group;
import edu.kit.pse.client.goapp.datamodels.Meeting;
import edu.kit.pse.client.goapp.datamodels.MeetingConfirmation;
import edu.kit.pse.client.goapp.datamodels.Participant;
import edu.kit.pse.client.goapp.datamodels.Tour;
import edu.kit.pse.client.goapp.datamodels.User;
import edu.kit.pse.client.goapp.service.GroupsService;
import edu.kit.pse.client.goapp.service.MeetingParticipantManagementService;
import edu.kit.pse.client.goapp.service.MeetingService;
import edu.kit.pse.goapp.client.goapp.R;

/**
 *
 */
public class CreateNewMeetingActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ServiceResultReceiver.Receiver {

    private static final String TAG = "CreateNewMeeting";

    private String userName;
    private int userId;
    private User myUser;


    private ImageButton menu_button;

    private ServiceResultReceiver activityServiceResultReceiver;
    private ObjectConverter<List<Group>> groupsConverter;
    private ObjectConverter<Meeting> meetingConverter;
    private ObjectConverter<Participant> participantConverter;


    private Meeting newMeeting;
    private AutoCompleteTextView meetingNameText;
    private AutoCompleteTextView durationText;
    private Button createButton;
    private ProgressDialog mProgressDialog;


    private AutoCompleteTextView time;
    private RadioButton radioButtonEvent;
    private RadioButton radioButtonTour;
    private Spinner spinnerGroup;
    private ArrayAdapter<String> spinnerAdapter;
    private int selectedGroupPosition = 0;

    private AutoCompleteTextView day;
    private AutoCompleteTextView month;
    private AutoCompleteTextView year;

    private AutoCompleteTextView hour;
    private AutoCompleteTextView minute;

    private AutoCompleteTextView lat;
    private AutoCompleteTextView lng;




    List<Participant> participants = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_meeting);
        menu_button = (ImageButton) findViewById(R.id.menu_new_meeting);
        menu_button.setOnClickListener(this);

        groupsConverter = new ObjectConverter<>();
        meetingConverter = new ObjectConverter<>();
        participantConverter = new ObjectConverter<>();

        meetingNameText = (AutoCompleteTextView) findViewById(R.id.tipMeetingName);
        radioButtonEvent = (RadioButton) findViewById(R.id.buttonEvent);
        radioButtonTour = (RadioButton) findViewById(R.id.buttonTour);
        durationText = (AutoCompleteTextView) findViewById(R.id.tipDuration);
        spinnerGroup = (Spinner) findViewById(R.id.spinnerGroup);
        createButton = (Button) findViewById(R.id.buttonCreate);

        lat = (AutoCompleteTextView) findViewById(R.id.tipLat);
        lng = (AutoCompleteTextView) findViewById(R.id.tipLng);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        day = (AutoCompleteTextView) findViewById(R.id.tipDay);
        day.setText(Integer.toString(c.get(Calendar.DAY_OF_MONTH)));
        month = (AutoCompleteTextView) findViewById(R.id.tipMonth);
        month.setText(Integer.toString(c.get(Calendar.MONTH)));
        year = (AutoCompleteTextView) findViewById(R.id.tipYear);
        year.setText(Integer.toString(c.get(Calendar.YEAR)));

        hour = (AutoCompleteTextView) findViewById(R.id.tipHour);
        hour.setText(Integer.toString(c.get(Calendar.HOUR_OF_DAY)));
        minute = (AutoCompleteTextView) findViewById(R.id.tipMinute);
        minute.setText(Integer.toString(c.get(Calendar.MINUTE)));

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        userName = sharedPreferences.getString("userName", "");
        userId = sharedPreferences.getInt("userId", -1);
        myUser = new User(userId, userName);

        showProgressDialog();
        // create a GroupsService
        Intent i = new Intent(this, GroupsService.class);
        activityServiceResultReceiver = new ServiceResultReceiver(new Handler());
        activityServiceResultReceiver.setReceiver(this);
        i.putExtra(CommunicationKeys.RECEICER, activityServiceResultReceiver);
        i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.GET);
        startService(i);


        /*
        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        */
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, CreateNewMeetingActivity.class);
        activity.startActivity(intent);
    }

    public void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(CreateNewMeetingActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_new_meeting, popup.getMenu());
        popup.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu_new_meeting) {
            showPopUp(v);
        }
        if (v.getId() == R.id.buttonEvent || v.getId() == R.id.buttonTour) {
            // Todo change duration time
            if (v.getId() == R.id.buttonEvent) {
                // Set durantion = 1h
                durationText.setText(1 + "");
            } else {
                // set durantion = 2h
                durationText.setText(2 + "");
            }
        }

        if (v.getId() == R.id.buttonCreate) {
            newMeeting = null;
            if (setNewMeeting()) {

                showProgressDialog();

                Intent i = new Intent(this, MeetingService.class);
                activityServiceResultReceiver = new ServiceResultReceiver(new Handler());
                activityServiceResultReceiver.setReceiver(this);
                i.putExtra(CommunicationKeys.RECEICER, activityServiceResultReceiver);
                i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.POST);
                String jsonObj = meetingConverter.serialize(newMeeting, Meeting.class);
                i.putExtra(CommunicationKeys.MEETING, jsonObj);
                startService(i);

                Toast.makeText(this,"Todo: MeetingServcie erstellt",
                        Toast.LENGTH_LONG).show();
                // MeetingListActivity.start(this);
            } else {
                Toast.makeText(this,"Ihre Eingaben waren inkorrekt",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    // if successfull set the New Meeting in meeting returns true, otherwise false
    private Boolean setNewMeeting() {
        int timeDay = Integer.parseInt(day.getText().toString()); //Fehler
        int timeMonth = Integer.parseInt(month.getText().toString());
        int timeYear = Integer.parseInt(year.getText().toString());
        int timeHour = Integer.parseInt(hour.getText().toString());
        int timeMinute = Integer.parseInt(minute.getText().toString());


        String name = meetingNameText.getText().toString();

        // Calende has over time overflow
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, timeDay);
        calendar.set(Calendar.MONTH, timeMonth);
        calendar.set(Calendar.YEAR, timeYear);

        calendar.set(Calendar.HOUR_OF_DAY, timeHour);
        calendar.set(Calendar.MINUTE, timeMinute);

        // has an time overflow occurrend?
        if (calendar.get(Calendar.DAY_OF_MONTH) == timeDay
                && calendar.get(Calendar.MONTH) == timeMonth
                && calendar.get(Calendar.YEAR) == timeYear
                && calendar.get(Calendar.HOUR_OF_DAY) == timeHour
                && calendar.get(Calendar.MINUTE) ==timeMinute) {

            if (groups != null) {

                int duration = Integer.parseInt(durationText.getText().toString());

                Group selectedGroup = groups.get(selectedGroupPosition);

                List<User> users = selectedGroup.getGroupMembers();

                // get my user and Id
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                userName = sharedPreferences.getString("userName", "");
                userId = sharedPreferences.getInt("userId", -1);

                // convert Users to participants for the new meeting
                for (User u: users) {
                    if (u.getId() != userId)

                    participants.add(new Participant(-1, -1, u, MeetingConfirmation.PENDING));
                }

                Participant creator = null;

                long latitude = Long.parseLong(lat.getText().toString());

                long longitude = Long.parseLong(lng.getText().toString());

                GPS place = new GPS( latitude, longitude, 0);
                if (radioButtonEvent.isChecked()) {
                    newMeeting = new Event(-1, name, place, calendar.getTimeInMillis(), duration, creator);
                    /* the server cant manage this yet
                            for (Participant p : participants) {
                                newMeeting.addParticipant(p);
                            }
                            */

                } else {
                    newMeeting = new Tour(-1, name, place, calendar.getTimeInMillis(), duration, creator);
                    /* the server cant manage this yet
                        for (Participant p : participants) {
                            newMeeting.addParticipant(p);
                        }
                        */
                }
                return true;
            }

        }
        // out of Calender
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.meetingList_new_new_meeting:
                MeetingListActivity.start(this);
                return true;
            case R.id.groups_new_meeting:
                GroupsActivity.start(this);
                return true;
            case R.id.settings_new_meeting:
                SettingsActivity.start(this);
                return true;
            case R.id.about_new_meeting:
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

                    case CommunicationKeys.FROM_GROUPS_SERVICE:
                        groupsServiceHandler(resultData);
                        break;

                    case CommunicationKeys.FROM_MEETING_SERVICE:
                        meetingServiceHandler(resultData);
                        break;
                    case CommunicationKeys.FROM_MEETING_PARTICIPANT_MANAGEMENT_SERVICE:
                        meetingPartManageHandler(resultData);
                        break;
                    default:
                        Log.d(TAG, "Wrong Service");
                        hideProgressDialog();
                }
                break;
            default:
                hideProgressDialog();
                Toast.makeText(this, ""+resultCode, Toast.LENGTH_SHORT).show();
        }
    }

    private void groupsServiceHandler (Bundle resultData) {
        switch (resultData.getString(CommunicationKeys.COMMAND)) {
            case CommunicationKeys.PUT: // Leave the Meeting

                Toast.makeText(this,"Error 500: no PUT started", Toast.LENGTH_LONG).show();
                Log.d(TAG,"PUT caught (wrong Command)");
                break;

            case CommunicationKeys.GET:
                Log.d(TAG,"GET caught");
                Toast.makeText(this, "Group List caught", Toast.LENGTH_LONG).show();


                String jGroupsList = resultData.getString(CommunicationKeys.GROUPS);

                groups = groupsConverter.deserializeList(jGroupsList, Group.class);

                setGroupsSpinner();
                break;

            case CommunicationKeys.POST:
                Toast.makeText(this, "Error 500: no Post started", Toast.LENGTH_LONG).show();
                Log.d(TAG,"POST caught (wrong Command)");
                break;

            case CommunicationKeys.DELETE:
                Toast.makeText(this, "Error 403: no Delete started", Toast.LENGTH_LONG).show();
                Log.d(TAG,"Delete caught (wrong Command)");
                break;

            default:
                Log.d(TAG,"ERROR 500: no Command caught");
        }
        hideProgressDialog();
    }

    private void setGroupsSpinner() {

        ArrayList<String> groupNames = new ArrayList<>();
        for (Group g : groups) {
            groupNames.add(g.getName());
        }

        spinnerAdapter = new ArrayAdapter<String> (CreateNewMeetingActivity.this, android.R.layout.simple_spinner_dropdown_item, groupNames);
        spinnerGroup.setAdapter(spinnerAdapter);
        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGroupPosition = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void meetingServiceHandler (Bundle resultData) {

        switch (resultData.getString(CommunicationKeys.COMMAND)) {
            case CommunicationKeys.PUT: // Leave the Meeting

                hideProgressDialog();

                Toast.makeText(this,"Error 500: no PUT started", Toast.LENGTH_LONG).show();
                Log.d(TAG,"PUT caught (wrong Command)");
                break;

            case CommunicationKeys.GET:
                hideProgressDialog();

                Log.d(TAG,"GET caught (wrong Command)");
                Toast.makeText(this, "Error 500: no GET started", Toast.LENGTH_LONG).show();
                break;

            case CommunicationKeys.POST: // New Meeting created
                Toast.makeText(this, "Termin ohne Mitglieder erstellt", Toast.LENGTH_LONG).show();

                // TOdo start to add all participants
                addParticipants();
                // MeetingListActivity.start(this);

                Log.d(TAG,"POST caught");
                break;

            case CommunicationKeys.DELETE:
                hideProgressDialog();

                Toast.makeText(this, "Error 403: no Delete started", Toast.LENGTH_LONG).show();
                Log.d(TAG,"Delete caught (wrong Command)");
                break;

            default:
                hideProgressDialog();

                Log.d(TAG,"ERROR 500: no Command caught");
        }
    }

    public void addParticipants() {
        if (participants.isEmpty()) {
            hideProgressDialog();
            Toast.makeText(this, "Termin erstellt", Toast.LENGTH_LONG).show();
        } else {
            Participant p = new Participant(-1, -1, null, null);
            p = participants.get(0);

            // start ParticipantsManagementService

            Intent i = new Intent(this, MeetingParticipantManagementService.class);
            activityServiceResultReceiver = new ServiceResultReceiver(new Handler());
            activityServiceResultReceiver.setReceiver(this);
            i.putExtra(CommunicationKeys.RECEICER, activityServiceResultReceiver);
            i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.POST);
            String jsonObj = participantConverter.serialize(p, Participant.class);
            i.putExtra(CommunicationKeys.MEETING, jsonObj);
            startService(i);

            Toast.makeText(this, "Versuche " + p.getUser().getName() + " hinzu zufügen", Toast.LENGTH_SHORT).show();


            // Object time Life error ? -> check Service, is json = null!!
            participants.remove(0);

        }
    }

    public void meetingPartManageHandler(Bundle resultData) {
        switch (resultData.getString(CommunicationKeys.COMMAND)) {
            case CommunicationKeys.POST: // new Pariticipant added
                Toast.makeText(this, "Erfolgreich hinzugefügt", Toast.LENGTH_SHORT).show();

                // TOdo start to add all participants
                addParticipants();
                // MeetingListActivity.start(this);

                Log.d(TAG,"POST caught");
                break;

            default:
                // TODO delet the Meeting from DB!
                hideProgressDialog();
                Log.d(TAG,"ERROR 500: no Command caught");
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


    class GroupSpinnerAdapter extends ArrayAdapter<Group> {
        Context context;

        List<Group> groups;


        GroupSpinnerAdapter(Context c, List<Group> groups) {
            super(c, R.layout.create_new_meeting_group_row, R.id.c_n_m_group_name,groups);

            this.context = c;
            this.groups = groups;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //make the xml layout into a java object
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View groupRow = inflater.inflate(R.layout.create_new_meeting_group_row, parent, false);

            Group group = groups.get(position);
            groupRow.setTag(R.id.TAG_GROUP_ID, group.getId());

               //deputy the buttons and TextViews as a java objects
            TextView name = (TextView) groupRow.findViewById(R.id.c_n_m_group_name);

            name.setText(group.getName());

            return groupRow;
        }
    }
}
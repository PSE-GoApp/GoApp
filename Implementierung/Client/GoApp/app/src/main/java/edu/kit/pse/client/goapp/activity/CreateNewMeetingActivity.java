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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import edu.kit.pse.client.goapp.datamodels.Event;
import edu.kit.pse.client.goapp.datamodels.GPS;
import edu.kit.pse.client.goapp.datamodels.Group;
import edu.kit.pse.client.goapp.datamodels.Meeting;
import edu.kit.pse.client.goapp.datamodels.Participant;
import edu.kit.pse.client.goapp.datamodels.Tour;
import edu.kit.pse.goapp.client.goapp.R;

/**
 *
 */
public class CreateNewMeetingActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    ImageButton menu_button;

    Meeting newMeeting;
    AutoCompleteTextView meetingNameText;
    AutoCompleteTextView durationText;
    Button createButton;
    //TODO with a time Spinner ?
    AutoCompleteTextView time;
    RadioButton radioButtonEvent;
    RadioButton radioButtonTour;
    Spinner spinnerGroup;

    AutoCompleteTextView day;
    AutoCompleteTextView month;
    AutoCompleteTextView year;

    AutoCompleteTextView hour;
    AutoCompleteTextView minute;



    ArrayList groups = new ArrayList<Group>(Arrays.asList(new Group[]{
            new Group(0, "PSE GRUPPE"),
            new Group(1, "Kommilitionen"),
            new Group(2, "Lern Gruppe")}));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_meeting);
        menu_button = (ImageButton) findViewById(R.id.menu_new_meeting);
        menu_button.setOnClickListener(this);

        meetingNameText = (AutoCompleteTextView) findViewById(R.id.tipMeetingName);
        radioButtonEvent = (RadioButton) findViewById(R.id.buttonEvent);
        radioButtonTour = (RadioButton) findViewById(R.id.buttonTour);
        durationText = (AutoCompleteTextView) findViewById(R.id.tipDuration);
        spinnerGroup = (Spinner) findViewById(R.id.spinnerGroup);
        createButton = (Button) findViewById(R.id.buttonCreate);

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


        // TODO create a GroupsService----------------------------------------------------------------------------------

        // TODO ERROR Fix it!-------------------------------------------------------------------------------------------
        spinnerGroup = (Spinner) findViewById(R.id.spinnerGroup);
        GroupSpinnerAdapter groupSpinnerAdapter = new GroupSpinnerAdapter(this, groups);
        spinnerGroup.setAdapter(groupSpinnerAdapter);
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
        }

        if (v.getId() == R.id.buttonCreate) {
            if (setNewMeeting()) {
                // TODO create an MeetinService and block Everything---------------------------------------------------------------------------------
                // newMeeting;
                Toast.makeText(this,"Todo: Servcie erstellt\n"+ newMeeting.getName() +
                        "\n" + newMeeting.getTimestamp() + "\n" + newMeeting.getDuration(),
                        Toast.LENGTH_LONG).show();
                MeetingListActivity.start(this);
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

        int duration = Integer.parseInt(durationText.getText().toString());

            // TODO his null-----------------------------------------------------------------------------------------------
        Participant creator = null;
        GPS place = null;
        if (radioButtonEvent.isChecked()) {
            newMeeting = new Event(-1, name, place, calendar.getTimeInMillis(), duration, creator);
        } else {
            newMeeting = new Tour(-1, name, place, calendar.getTimeInMillis(), duration, creator);
        }
        return true;
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
                // AboutActivity.start(this);
                return true;
            default:
                return false;
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
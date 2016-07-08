package edu.kit.pse.client.goapp.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import edu.kit.pse.goapp.client.goapp.R;

public class NewMeetingActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    ImageButton menu_button;
    RadioButton radioButtonEvent;
    RadioButton radioButtonTour;
    Spinner spinnerGroup;
    String[] auswahlGruppen = {"PSE Gruppe", "Kommilitionen", "Lern Gruppe"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);
        menu_button = (ImageButton) findViewById(R.id.menu_new_meeting);
        menu_button.setOnClickListener(this);
        radioButtonEvent = (RadioButton) findViewById(R.id.buttonEvent);
        radioButtonTour = (RadioButton) findViewById(R.id.buttonTour);

        spinnerGroup = (Spinner) findViewById(R.id.spinnerGroup);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String> (NewMeetingActivity.this, android.R.layout.simple_spinner_dropdown_item, auswahlGruppen);
        spinnerGroup.setAdapter(spinnerAdapter);
        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, NewMeetingActivity.class);
        activity.startActivity(intent);
    }

    public void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(NewMeetingActivity.this);
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
            meetingType(v);
        }

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
                // SettingsActivity.start(this);
                return true;
            case R.id.about_new_meeting:
                // AboutActivity.start(this);
                return true;
            default:
                return false;
        }
    }

    // TODO: methode terminType schreiben. Man darf nur Veranstaltung oder Tour "checken" (ausgewählt sein)

    private void meetingType (View view) {

        if (view.getId() == radioButtonEvent.getId()) {

            if (radioButtonTour.isChecked()) {

            }
            // checked von TourButton entfernen und buttonVeranstaltung Zustand als checked setzen.
            // radioButtonEvent.setClickDIngs
            // radioButtonTour.removeClickdings

        }
        else {
            if (view.getId() == radioButtonTour.getId()) {
                // checked von buttonVeranstaltung entfernen und TourButton Zustand als checked  setzen
                // radioButtonTour.setClickdings
                // radioButtonEvent.removeClickDIngs

            }
        }
    }


}
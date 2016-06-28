package edu.kit.pse.client.goapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import edu.kit.pse.goapp.client.goapp.R;

public class MeetingListActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    //something
    ImageButton menu_button;
    ImageButton teilnehmer;
    ImageButton map;
    TextView agostea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);
        menu_button = (ImageButton) findViewById(R.id.menu_termine);
        menu_button.setOnClickListener(this);
        teilnehmer = (ImageButton) findViewById(R.id.teilnehmer);
        teilnehmer.setOnClickListener(this);
        map = (ImageButton) findViewById(R.id.map);
        map.setOnClickListener(this);
        agostea = (TextView) findViewById(R.id.textView2);
        agostea.setOnClickListener(this);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MeetingListActivity.class);
        activity.startActivity(intent);
    }

    private void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(MeetingListActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu_termine) {
            showPopUp(v);
        }else if (v.getId() == R.id.teilnehmer) {
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

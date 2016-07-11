package edu.kit.pse.client.goapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import edu.kit.pse.goapp.client.goapp.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{

    private ImageButton menu_button;
    private Switch switchGps;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    public final static String GPSENABLED = "sucker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        menu_button = (ImageButton) findViewById(R.id.menu_termine_settings);
        menu_button.setOnClickListener(this);
        switchGps = (Switch) findViewById(R.id.switchStandort);

        sharedpreferences = getSharedPreferences("sucker", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        int i = sharedpreferences.getInt(GPSENABLED, 3);
        if (i == 0){
            switchGps.setChecked(false);
        }else{
            switchGps.setChecked(true);
        }
        switchGps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    editor.putInt(GPSENABLED, 1);
                    editor.commit();
                } else {
                    editor.putInt(GPSENABLED, 0);
                    editor.commit();
                }
            }
        });
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }

    public void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(SettingsActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_settings, popup.getMenu());
        popup.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu_termine_settings) {
            showPopUp(v);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_settings:
                MeetingListActivity.start(this);
                return true;
            case R.id.neuer_termin_settings:
                //TerminActivity.start(this);
                return true;
            case R.id.groups_settings:
                GroupsActivity.start(this);
                return true;
            case R.id.about_settings:
                // AboutActivity.start(this);
                return true;
            default:
                return false;
        }
    }
}

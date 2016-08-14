package edu.kit.pse.client.goapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import edu.kit.pse.goapp.client.goapp.R;

/**
 * AboutActivity shows information about the app.
 * Extends AppCompatActivity.
 * Implements View.OnClickListener und PopupMenu.OnMenuItemClickListener.
 */
public class AboutActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{

    ImageButton menu_button;

    /**
     * Creates an AboutActivity.
     * @param savedInstanceState Bundle state of instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        menu_button = (ImageButton) findViewById(R.id.menu_termine_about);
        menu_button.setOnClickListener(this);
    }

    /**
     * Starts the AboutActivity
     * @param activity Activity
     */
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivity(intent);

    }

    /**
     * Shows a popup window with app information on the display
     * @param v View
     */
    public void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(AboutActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_about, popup.getMenu());
        popup.show();
    }

    /**
     * Calls the showPopUp method in consequence of a click
     * @param v View
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu_termine_about) {
            showPopUp(v);
        }
    }

    /**
     * Opens another activity from the menu
     * @param item MenuItem
     * @return true if activity started successfully, else false
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_about:
                MeetingListActivity.start(this);;
                return true;
            case R.id.neuer_termin_about:
                CreateNewMeetingActivity.start(this);
                return true;
            case R.id.groups_about:
                GroupsActivity.start(this);
                return true;
            case R.id.settings_about:
                SettingsActivity.start(this);
                return true;
            default:
                return false;
        }
    }
}

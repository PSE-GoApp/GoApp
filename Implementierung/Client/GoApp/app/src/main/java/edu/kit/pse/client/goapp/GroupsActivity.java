package edu.kit.pse.client.goapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.kit.pse.client.goapp.datamodels.Group;
import edu.kit.pse.client.goapp.service.GroupsService;
import edu.kit.pse.goapp.client.goapp.R;

public class GroupsActivity extends AppCompatActivity  implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private List<Group> groups = new ArrayList<Group>();
    ImageButton menu_button;
    ImageButton buttonCreateGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        menu_button = (ImageButton) findViewById(R.id.menu_termine_groups);
        menu_button.setOnClickListener(this);

        buttonCreateGroup = (ImageButton) findViewById(R.id.createGroup);
        buttonCreateGroup.setOnClickListener(this);
        populateListView();
        registerClickCallback();
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, GroupsActivity.class);
        activity.startActivity(intent);
    }

    public void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(GroupsActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_groups, popup.getMenu());
        popup.show();
    }

    private void populateListView() {
// Create list of items
        Intent i = new Intent(this, GroupsService.class);
        startService(i);
        ArrayAdapter<Group> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.groupsList);
        list.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu_termine_groups) {
            showPopUp(v);
        }
        if (v.getId() == R.id.createGroup) {
           // CreateNewGroupActivity.start(this);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_groups:
                MeetingListActivity.start(this);
                return true;
            case R.id.neuer_termin_groups:
               // TerminActivity.start(this);
                return true;
            case R.id.settings_groups:
               // SettingsActivity.start(this);
                return true;
            case R.id.about_groups:
               // AboutActivity.start(this);
                return true;
            default:
                return false;
        }
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.groupsList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {

                Group clickedGroup = groups.get(position);
                String message = clickedGroup.getName();
               // GroupSetActivity.start(GroupsActivity.this, message);
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<Group> {
        public MyListAdapter() {
            super(GroupsActivity.this, R.layout.groups_item, groups);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.groups_item, parent, false);
            }

            Group currentGroup = groups.get(position);

            TextView makeText = (TextView) itemView.findViewById(R.id.item_textView);
            makeText.setText(currentGroup.getName());

            return itemView;
        }
    }
}

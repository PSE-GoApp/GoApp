package edu.kit.pse.client.goapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.ServiceResultReceiver;
import edu.kit.pse.client.goapp.converter.ObjectConverter;
import edu.kit.pse.client.goapp.datamodels.Group;
import edu.kit.pse.client.goapp.service.GroupService;
import edu.kit.pse.client.goapp.service.GroupsService;
import edu.kit.pse.goapp.client.goapp.R;

/**
 * This Activity shows a List of Groups and receives the interactions for deleting a Group.
 */
public class GroupsActivity extends AppCompatActivity  implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ServiceResultReceiver.Receiver {

    public ServiceResultReceiver mReceiver;
    private List<Group> groups = new ArrayList<>();
    private ImageButton menu_button;
    private ImageButton buttonCreateGroup;
    private int positionClicked;
    private ProgressDialog progressDialog;
    private ObjectConverter<List<Group>> groupsConverter = new ObjectConverter<>();
    //private ListView list;
    ArrayAdapter<Group> adapter;

    /**
     * creates the activity and sets up the listeners
     * @param savedInstanceState: If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        //list = (ListView) findViewById(R.id.groupsList);
        menu_button = (ImageButton) findViewById(R.id.menu_termine_groups);
        menu_button.setOnClickListener(this);
        buttonCreateGroup = (ImageButton) findViewById(R.id.createGroup);
        buttonCreateGroup.setOnClickListener(this);
        populateListView();
        registerClickCallback();
    }

    /**
     * Starts the Activity
     * @param activity: the activity that is calling it
     */
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, GroupsActivity.class);
        activity.startActivity(intent);
    }

    /**
     * method that shows up the popUpMenu
     * @param v the view
     */
    public void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(GroupsActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_groups, popup.getMenu());
        popup.show();
    }

    /**
     * creates the listView
     */
    private void populateListView() {
        Intent i = new Intent(this, GroupsService.class);
        mReceiver = new ServiceResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        i.putExtra(CommunicationKeys.RECEICER, mReceiver);
        i.putExtra(CommunicationKeys.COMMAND, "GET");
        startService(i);
    }

    /**
     * onClick listener for the buttons
     * @param v the view
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu_termine_groups) {
            showPopUp(v);
        }
        if (v.getId() == R.id.createGroup) {
           CreateNewGroupActivity.start(this);
        }
       /* if (v.getId() == R.id.delete_group) {
            Intent i = new Intent(this, GroupService.class);
            mReceiver1 = new ServiceResultReceiver(new Handler());
            mReceiver1.setReceiver(this);
            i.putExtra(CommunicationKeys.RECEICER, mReceiver1);
            i.putExtra(CommunicationKeys.COMMAND, "DELETE");
            //i.putExtra("group", new ParcelableGroup(groups.get(positionClicked)));
            startService(i);
            //progressDialog = ProgressDialog.show(GroupsActivity.this, "", "Sending");
        }*/
    }

    /**
     * onClick listeners for the items in the menu
     * @param item item clicked
     * @return a boolean value
     */
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
               SettingsActivity.start(this);
                return true;
            case R.id.about_groups:
               // AboutActivity.start(this);
                return true;
            default:
                return false;
        }
    }

    /**
     * onClick listener for the list
     */
    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.groupsList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {

                Group clickedGroup = groups.get(position);
                String message = clickedGroup.getName();
                GroupMemberActivity.start(GroupsActivity.this, clickedGroup.getId());
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                positionClicked = pos;
                LayoutInflater layoutInflater = LayoutInflater.from(GroupsActivity.this);
                View promptView = layoutInflater.inflate(R.layout.groups_pop_up, null);

                TextView myAwesomeTextView = (TextView)promptView.findViewById(R.id.delete_group);
                myAwesomeTextView.setText("Delete " + groups.get(positionClicked).getName());

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GroupsActivity.this);
                alertDialogBuilder.setView(promptView);



                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Intent i = new Intent(GroupsActivity.this, GroupService.class);
                                        mReceiver = new ServiceResultReceiver(new Handler());
                                        mReceiver.setReceiver(GroupsActivity.this);
                                        i.putExtra(CommunicationKeys.RECEICER, mReceiver);
                                        i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.DELETE);
                                        i.putExtra(CommunicationKeys.GROUP_ID, groups.get(positionClicked).getId());
                                        startService(i);
                                        progressDialog = ProgressDialog.show(GroupsActivity.this, "", "Sending");
                                    }
                                }
                        )
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create an alert dialog
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();

                return true;
            }
        });
    }

    /**
     * gets the results from the service
     * @param resultCode the result code
     * @param resultData the data
     */
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == 200) {
            if (resultData.getString(CommunicationKeys.SERVICE) == CommunicationKeys.FROM_GROUPS_SERVICE){
                setListResult(resultData);
            } else if(resultData.getString(CommunicationKeys.SERVICE) == CommunicationKeys.FROM_GROUP_SERVICE){
                progressDialog.dismiss();
                groups.remove(positionClicked);
                adapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(this, resultCode, Toast.LENGTH_LONG).show();
            showError(resultCode);
        }
    }

    /**
     * shows the error message
     * @param resultCode the result code and specific error
     */
    private void showError(int resultCode){
        LayoutInflater layoutInflater = LayoutInflater.from(GroupsActivity.this);
        View promptView = layoutInflater.inflate(R.layout.error, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GroupsActivity.this);
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

    /**
     * sets the list of groups
     * @param resultData all the data from the service
     */

    private void setListResult(Bundle resultData) {

        String json = resultData.getString(CommunicationKeys.GROUPS);
        groups = groupsConverter.deserializeList(json, Group[].class);
        adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.groupsList);
        list.setAdapter(adapter);
    }

    /**
     * it a private class that sets up an adapter for the listView
     */
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

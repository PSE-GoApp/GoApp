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
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.ServiceResultReceiver;
import edu.kit.pse.client.goapp.datamodels.User;
import edu.kit.pse.client.goapp.service.GroupUserManagementService;
import edu.kit.pse.goapp.client.goapp.R;

/**
 * This Activity shows all Groupmembers,you can leave the Group and Administrators can search for Members to add ordelete them.
 */
public class GroupMemberActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ServiceResultReceiver.Receiver {

    private List<User> users = new ArrayList<User>();
    private ImageButton menu_button;
    private ServiceResultReceiver mReceiver;
    private int positionClicked;
    private ProgressDialog progressDialog;
    private ArrayAdapter<User> adapter;
    private static int groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        menu_button = (ImageButton) findViewById(R.id.menu_group_members);
        menu_button.setOnClickListener(this);
        populateListView();
        registerClickCallback();
    }

    /**
     * creates the listView
     */
    private void populateListView() {
        Intent i = new Intent(this, GroupUserManagementService.class);
        mReceiver = new ServiceResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        i.putExtra(CommunicationKeys.RECEICER, mReceiver);
        i.putExtra(CommunicationKeys.COMMAND, "GET");
        i.putExtra(CommunicationKeys.GROUP_ID,groupId);
        startService(i);
    }

    /**
     * onClick listener for the list
     */
    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.groupMemberList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {

                User clickedUser = users.get(position);
                String message = clickedUser.getName();
                //GroupMemberActivity.start(GroupMemberActivity.this);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                Log.e("got","in");
                // TODO Auto-generated method stub
                positionClicked = pos;
                LayoutInflater layoutInflater = LayoutInflater.from(GroupMemberActivity.this);
                View promptView = layoutInflater.inflate(R.layout.groups_pop_up, null);
                TextView myAwesomeTextView = (TextView)promptView.findViewById(R.id.delete_group);
                myAwesomeTextView.setText("Delete " + users.get(positionClicked).getName());
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GroupMemberActivity.this);
                alertDialogBuilder.setView(promptView);

                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Intent i = new Intent(GroupMemberActivity.this, GroupUserManagementService.class);
                                        mReceiver = new ServiceResultReceiver(new Handler());
                                        mReceiver.setReceiver(GroupMemberActivity.this);
                                        i.putExtra(CommunicationKeys.RECEICER, mReceiver);
                                        i.putExtra(CommunicationKeys.COMMAND, "DELETE");
                                        i.putExtra(CommunicationKeys.GROUP_ID, users.get(positionClicked).getId());
                                        startService(i);
                                        progressDialog = ProgressDialog.show(GroupMemberActivity.this, "", "Sending");
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
        if (resultCode == 202) {
            if (resultData.getString(CommunicationKeys.SERVICE) == "GroupsService"){
                // TODO setListResult(resultData);
            } else if(resultData.getString(CommunicationKeys.SERVICE) == "GroupUserManagementService"){
                Log.e("returned","groupService");
                progressDialog.dismiss();
                users.remove(positionClicked);
                adapter.notifyDataSetChanged();
            }
        } else {
            showError(resultCode);
        }
    }

    /**
     * shows the error message
     * @param resultCode the result code and specific error
     */
    private void showError(int resultCode){
        LayoutInflater layoutInflater = LayoutInflater.from(GroupMemberActivity.this);
        View promptView = layoutInflater.inflate(R.layout.error, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GroupMemberActivity.this);
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
    /* TODO-----------------------------------------------------------
    private void setListResult(Bundle resultData){
        ArrayList<ParcelableUser> parcelableUsers = resultData.getParcelableArrayList(CommunicationKeys.USERS);
        if (parcelableUsers != null) {
            for (ParcelableUser g : parcelableUsers) {
                users.add(g.getUser());
            }
            adapter = new MyListAdapter();
            ListView list = (ListView) findViewById(R.id.groupMemberList);
            list.setAdapter(adapter);
        }
    }
    */

    /**
     * method that shows up the popUpMenu
     * @param v the view
     */
    public void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(GroupMemberActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_group_members, popup.getMenu());
        popup.show();
    }

    /**
     * Starts the Activity
     * @param activity: the activity that is calling it
     */
    public static void start(Activity activity, int groupID) {
        groupId = groupID;
        Intent intent = new Intent(activity, GroupMemberActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu_group_members) {
            showPopUp(v);
        }
    }

    /**
     * onClick listeners for the items in the menu
     * @param item item clicked
     * @return a boolean value
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_teilnehmer:
                MeetingListActivity.start(this);
                return true;
            case R.id.neuer_termin_teilnehmer:
                //TerminActivity.start(this);
                return true;
            case R.id.groups_teilnehmer:
                GroupsActivity.start(this);
                return true;
            case R.id.settings_teilnehmer:
                SettingsActivity.start(this);
                return true;
            case R.id.about_teilnehmer:
               // AboutActivity.start(this);
                return true;
            default:
                return false;
        }
    }

    /**
     * it a private class that sets up an adapter for the listView
     */
    private class MyListAdapter extends ArrayAdapter<User> {
        public MyListAdapter() {
            super(GroupMemberActivity.this, R.layout.groups_item, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.groups_item, parent, false);
            }

            User currentUser = users.get(position);

            TextView makeText = (TextView) itemView.findViewById(R.id.item_textView);
            makeText.setText(currentUser.getName());

            return itemView;
        }
    }


}

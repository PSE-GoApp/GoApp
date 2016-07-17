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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.ServiceResultReceiver;
import edu.kit.pse.client.goapp.converter.ObjectConverter;
import edu.kit.pse.client.goapp.datamodels.Group;
import edu.kit.pse.client.goapp.datamodels.GroupMember;
import edu.kit.pse.client.goapp.datamodels.User;
import edu.kit.pse.client.goapp.service.GroupService;
import edu.kit.pse.client.goapp.service.GroupUserManagementService;
import edu.kit.pse.client.goapp.service.UsersService;
import edu.kit.pse.goapp.client.goapp.R;

//to do setLIsts check it
//pack users: sendUsers
// createGroup check it

/**
 * In this Activity you can create a new Grpup and add Members to it
 */
public class CreateNewGroupActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ServiceResultReceiver.Receiver{

    public List<User> users = new ArrayList<User>();
    public List<User> usersAdded = new ArrayList<User>();
    public ServiceResultReceiver mReceiver;
    private Group createdGroup = null;
    private ProgressDialog progressDialog;
    private ObjectConverter<Group> groupConverter;
    private ObjectConverter<User> userConverter;
    private EditText groupName;
    private ArrayAdapter adapter;
    private ArrayAdapter adapter2;
    private Button createButton;


    /**
     * on Create method for the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_group);
        groupName = (EditText) findViewById(R.id.editText_group_name);
        groupConverter = new ObjectConverter<>();
        createButton = (Button) findViewById(R.id.button_create_group);
        createButton.setOnClickListener(this);
        //set second list
        // TODO ----------------------------------------------------------------------------------------------------------------------
        getUsers();
        registerClickCallback();
    }


    /**
     * checks if the Name field is empty
     * @return true if not empty
     */
    private boolean nameGiven(){
        String name = groupName.getText().toString();
        if (name.matches("")) {
            Toast.makeText(this, "Gruppenname war nicht eingetragen", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    /**
     * gets the results from the service
     * @param resultCode the result code
     * @param resultData the data
     */
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == 200) {
            if (resultData.getString(CommunicationKeys.SERVICE) == CommunicationKeys.FROM_USERS_SERVICE){
                setListResult(resultData);
            } else if(resultData.getString(CommunicationKeys.SERVICE) == CommunicationKeys.FROM_GROUP_SERVICE) {

                switch (resultData.getString(CommunicationKeys.COMMAND)) {

                    case CommunicationKeys.POST:


                        String json = resultData.getString(CommunicationKeys.GROUP);

                        createdGroup = groupConverter.deserialize(json, Group.class);

                        // TODO delete Toast
                        Toast.makeText(this, "Group created\n GroupId: " + createdGroup.getId(), Toast.LENGTH_SHORT).show();

                        // TODo
                        sendUsers();
                        Log.e("getting users", "calling users");
                       // progressDialog.dismiss();
                        break;
                    default:
                        // for DEBUGGING
                        Toast.makeText(this, "Falscher Befehl", Toast.LENGTH_SHORT).show();
                }
            } else {
                if(resultData.getString(CommunicationKeys.SERVICE) == CommunicationKeys.FROM_GROUP_USER_MANAGEMENT) {
                    switch (resultData.getString(CommunicationKeys.COMMAND)) {

                        case CommunicationKeys.POST:
                            Log.e("Made","It User");
                            sendUsers();
                            /*
                            String json = resultData.getString(CommunicationKeys.USER);

                            User user = userConverter.deserialize(json, User.class);

                            Toast.makeText(this, "Benutzer " + user.getName() + " wurde hinzugefügt", Toast.LENGTH_SHORT).show();


                            // sendUsers();
                            progressDialog.dismiss();
                            break;
                            */
                        default:
                            // for DEBUGGING
                            Toast.makeText(this, "Falscher Befehl", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            showError(resultCode);
        }
    }

    /**
     * sends the users
     */
    private void sendUsers() {
        if (usersAdded.size() > 0){
            Intent i = new Intent(this, GroupUserManagementService.class);
            mReceiver = new ServiceResultReceiver(new Handler());
            mReceiver.setReceiver(this);
            i.putExtra(CommunicationKeys.RECEICER, mReceiver);
            i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.POST);

            GroupMember newMember = new GroupMember();
            newMember.setUserId(usersAdded.get(0).getId());
            usersAdded.remove(0);
            newMember.setGroupId(createdGroup.getId());
            ObjectConverter<GroupMember> groupMemberConverter = new ObjectConverter<>();
            String jsonObj = groupMemberConverter.serialize(newMember, GroupMember.class);

            i.putExtra(CommunicationKeys.USER, jsonObj);
            Log.e("Started"," sending");
            startService(i);
        }
        /*for (User u : usersAdded) {
            Intent i = new Intent(this, GroupUserManagementService.class);
            mReceiver = new ServiceResultReceiver(new Handler());
            mReceiver.setReceiver(this);
            i.putExtra(CommunicationKeys.RECEICER, mReceiver);
            i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.POST);

            GroupMember newMember = new GroupMember();
            newMember.setUserId(u.getId());
            newMember.setGroupId(createdGroup.getId());
            ObjectConverter<GroupMember> groupMemberConverter = new ObjectConverter<>();
            String jsonObj = groupMemberConverter.serialize(newMember, GroupMember.class);

            i.putExtra(CommunicationKeys.USER, jsonObj);
            Log.e("Started"," sending");
            startService(i);
        }*/
    }

    /**
     * sets the list of groups
     * @param resultData all the data from the service
     */

    private void setListResult(Bundle resultData){
        //GET DATA and FILL users
            String jsonObj = resultData.getString(CommunicationKeys.USERS);
            ObjectConverter<List<User>> mConverter = new ObjectConverter<>();
            List<User> userTerm = new ArrayList<User>();
            userTerm = mConverter.deserializeList ( jsonObj,User[].class);
            users.addAll(userTerm);
            setLists();
        }


    /**
     * shows the error message
     * @param resultCode the result code and specific error
     */
    private void showError(int resultCode){
        LayoutInflater layoutInflater = LayoutInflater.from(CreateNewGroupActivity.this);
        View promptView = layoutInflater.inflate(R.layout.error, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateNewGroupActivity.this);
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
     * starts activity
     * @param activity needs the activity that is calling it
     */
    public static void start(Activity activity) {
        Intent intent = new Intent(activity, CreateNewGroupActivity.class);
        activity.startActivity(intent);
    }

    /**
     * shows the menu
     * @param v the view
     */
    public void showPopUp(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(CreateNewGroupActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_new_group, popup.getMenu());
        popup.show();
    }

    /**
     * onClick listener for the Buttons
     * @param v the view
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.menu_new_group) {
            showPopUp(v);
        }else if(v.getId() == R.id.button_create_group) {
            createGroup();
        }
    }

    /**
     * sends the information for creating a group: the Group Name
     */
    private void createGroup(){
        if(nameGiven()) {
            Intent i = new Intent(this, GroupService.class);
            mReceiver = new ServiceResultReceiver(new Handler());
            mReceiver.setReceiver(this);
            i.putExtra(CommunicationKeys.RECEICER, mReceiver);
            i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.POST);
            //IS IT CORRECT ? Todo
            Group gr = new Group(0, groupName.getText().toString());
            String jsonObj = groupConverter.serialize(gr, Group.class);
            i.putExtra(CommunicationKeys.GROUP, jsonObj);
            startService(i);
        }
    }

    /**
     * listener for the menu
     * @param item item clicked
     * @return returns true if something was clicked
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_new_group:
                MeetingListActivity.start(this);
                return true;
            case R.id.neuer_new_group:
                CreateNewMeetingActivity.start(this);
                return true;
            case R.id.groups_new_group:
                GroupsActivity.start(this);
                return true;
            case R.id.about_new_group:
                AboutActivity.start(this);
                return true;
            default:
                return false;
        }
    }

    /**
     * method to get the users from the server. It only starts the service
     */
    private void getUsers() {
        Intent i = new Intent(this, UsersService.class);
        mReceiver = new ServiceResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        i.putExtra(CommunicationKeys.RECEICER, mReceiver);
        i.putExtra(CommunicationKeys.COMMAND, "GET");
        startService(i);
    }



    /**
     * onClick listener for the list
     */
    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.listView_users);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {

                User clickedUser = users.get(position);
                users.remove(position);
                adapter2.notifyDataSetChanged();
                addToList(clickedUser);
            }
        });
    }

    /**
     * adds people from the list the the group list
     * @param user
     */
    private void addToList(User user){
        usersAdded.add(user);
        adapter.notifyDataSetChanged();
    }

    /**
     * sets the initial Lists
     */
    public void setLists()
    {
        adapter = new MyListAdapterAdded();
        ListView listAdded = (ListView) findViewById(R.id.listView_added);
        listAdded.setAdapter(adapter);
        adapter2 = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.listView_users);
        list.setAdapter(adapter2);
    }


    /**
     * it a private class that sets up an adapter for the listView
     */
    private class MyListAdapter extends ArrayAdapter<User> {
        public MyListAdapter() {
            super(CreateNewGroupActivity.this, R.layout.users_item, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.users_item, parent, false);
            }

            User currentUser = users.get(position);

            TextView makeText = (TextView) itemView.findViewById(R.id.item_textView_users);
            makeText.setText(currentUser.getName());

            return itemView;
        }
    }

    /**
     * it a private class that sets up an adapter for the listViewthas been added
     */
    private class MyListAdapterAdded extends ArrayAdapter<User> {
        public MyListAdapterAdded() {
            super(CreateNewGroupActivity.this, R.layout.users_item, usersAdded);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.users_item, parent, false);
            }

            User currentUser = usersAdded.get(position);

            TextView makeText = (TextView) itemView.findViewById(R.id.item_textView_users);
            makeText.setText(currentUser.getName());

            return itemView;
        }
    }
}

package edu.kit.pse.client.goapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.datamodels.User;
import edu.kit.pse.client.goapp.parcelableAdapters.ParcelableUser;

/**
 * Created by e6420 on 4.7.2016 г..
 */
public class GroupUserManagmentService extends IntentService {

    public GroupUserManagmentService() {
        super("GroupsService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GroupUserManagmentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String command = intent.getStringExtra(CommunicationKeys.COMMAND);
        final ResultReceiver receiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);
        Bundle b = new Bundle();
        switch (command) {
            case "GET":
                List<User> list = listMe();
                ArrayList<ParcelableUser> parcelableUsers = new ArrayList<ParcelableUser>();
                for (User users : list) {
                    parcelableUsers.add(new ParcelableUser(users));
                }
                b.putParcelableArrayList(CommunicationKeys.USERS, parcelableUsers);
                b.putString(CommunicationKeys.COMMAND, "GET");
                b.putString(CommunicationKeys.SERVICE, "GroupsService");
                receiver.send(202, b);
                break;
            case "DELETE":
                Log.e("delete","in");
                b.putString(CommunicationKeys.SERVICE, "GroupUserManagmentService");
                receiver.send(202,b);
                break;
            case "PUT":
                break;
            case "POST":
                break;
            default:
                break;
        }
    }

    private List<User> listMe() {
        List<User> list = new ArrayList<>();
        list.add(new User(1,"fucker"));
        list.add(new User(2, "sucker"));
        return list;
    }
}

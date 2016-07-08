package edu.kit.pse.client.goapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.util.ArrayList;
import java.util.List;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.parcelableAdapters.ParcelableGroup;
import edu.kit.pse.client.goapp.datamodels.Group;

/**
 * Created by e6420 on 28.6.2016 г..
 */
public class GroupsService extends IntentService {

    public GroupsService() {
        super("GroupsService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GroupsService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String command = intent.getStringExtra(CommunicationKeys.COMMAND);
        switch (command) {
            case "GET":
                //this sends the group list
                List<Group> list = listMe();
                ArrayList<ParcelableGroup> parcelableGroups = new ArrayList<ParcelableGroup>();
                for (Group group: list) {
                    parcelableGroups.add(new ParcelableGroup(group));
                }
                final ResultReceiver receiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);
                Bundle b = new Bundle();
                b.putParcelableArrayList(CommunicationKeys.GROUPS,parcelableGroups);
                b.putString(CommunicationKeys.COMMAND, "GET");
                b.putString(CommunicationKeys.SERVICE, "GroupsService");
                receiver.send(202, b);
                break;
            case "DELETE":
                break;
            case "PUT":
                break;
            case "POST":
                break;
            default:
                break;
        }

    }

    // creates a list for testing
    private List<Group> listMe() {
        List<Group> list = new ArrayList<>();
        list.add(new Group(1,"fucker"));
        list.add(new Group(2, "sucker"));
        return list;
    }
}

package edu.kit.pse.client.goapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.GroupsActivity;
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
        List<Group> list = listMe();
        ArrayList<ParcelableGroup> parcelableGroups = new ArrayList<ParcelableGroup>();
        for (Group group: list) {
            parcelableGroups.add(new ParcelableGroup(group));
        }
        final ResultReceiver receiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);
        Bundle b = new Bundle();
        b.putParcelableArrayList(CommunicationKeys.GROUPS,parcelableGroups);
        b.putString(CommunicationKeys.ACTION, "GET");
        b.putString(CommunicationKeys.SERVICE, "GroupsService");
        receiver.send(202, b);
    }

    private List<Group> listMe() {
        List<Group> list = new ArrayList<>();
        list.add(new Group(1,"fucker"));
        list.add(new Group(2, "sucker"));
        return list;
    }
}

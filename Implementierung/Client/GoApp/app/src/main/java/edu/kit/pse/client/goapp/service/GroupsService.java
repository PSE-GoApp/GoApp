package edu.kit.pse.client.goapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
        Log.e("Still counting", "something");
    }

    private List<Group> listMe() {
        List<Group> list = new ArrayList<>();
        list.add(new Group(1,"fucker"));
        list.add(new Group(2, "sucker"));
        return list;
    }
}

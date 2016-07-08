package edu.kit.pse.client.goapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.datamodels.Group;
import edu.kit.pse.client.goapp.parcelableAdapters.ParcelableGroup;

/**
 * Created by e6420 on 3.7.2016 г..
 */
public class GroupService extends IntentService {

    public GroupService() {
        super("GroupService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GroupService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String command = intent.getStringExtra(CommunicationKeys.COMMAND);
        switch (command) {
            case "GET":
                break;
            case "DELETE":
                final ResultReceiver receiver = intent.getParcelableExtra(CommunicationKeys.RECEICER);
                Bundle b = new Bundle();
                b.putString(CommunicationKeys.COMMAND, "DELETE");
                b.putString(CommunicationKeys.SERVICE, "GroupService");
                receiver.send(202, b);
                break;
            case "PUT":
                break;
            case "POST":
                break;
            default:
                break;
        }

    }
}

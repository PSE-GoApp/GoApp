package edu.kit.pse.client.goapp.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.ServiceResultReceiver;
import edu.kit.pse.client.goapp.datamodels.Meeting;
import edu.kit.pse.goapp.client.goapp.R;

/**
 * Created by kansei on 10.07.16.
 */
public class LoginActivity extends AppCompatActivity implements  ServiceResultReceiver.Receiver {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {
            case 202:
                switch (resultData.getString(CommunicationKeys.SERVICE)) {
                    case CommunicationKeys.
                }

                Toast.makeText(this,"202", Toast.LENGTH_SHORT);
                String jsonString = resultData.getString(CommunicationKeys.MEETING);
                meeting = meetingConverter.deserialize(jsonString,Meeting.class);
                participants = meeting.getParticipants();
                if (participants != null) {
                    list = (ListView) findViewById(R.id.participant_listview);

                    ParticipantListAdapter adapter = new ParticipantListAdapter(this, participants);
                    list.setAdapter(adapter);
                    if (meeting.getTimestamp() >= System.currentTimeMillis()) {
                        cancelButton.setTag("Termin verlassen");
                    }
                } else {
                    Toast.makeText(this, "Error 500: Missing Informations", Toast.LENGTH_LONG).show();
                }
                break;
            case 400:
                Toast.makeText(this, "Error 400: Missing Informations", Toast.LENGTH_LONG).show();
                break;
            case 403:
                Toast.makeText(this, "Error 403: Missing Informations", Toast.LENGTH_LONG).show();
                break;
            case 408:
                Toast.makeText(this, "Error 408: Missing Informations", Toast.LENGTH_LONG).show();
                break;
            case 500:
                Toast.makeText(this, "Error 500: unexpected Error", Toast.LENGTH_LONG).show();
                break;
        }



    }

    }
}

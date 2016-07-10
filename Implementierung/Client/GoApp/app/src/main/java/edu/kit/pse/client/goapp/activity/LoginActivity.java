package edu.kit.pse.client.goapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.kit.pse.client.goapp.CommunicationKeys;
import edu.kit.pse.client.goapp.ServiceResultReceiver;
import edu.kit.pse.client.goapp.service.LoginService;
import edu.kit.pse.goapp.client.goapp.R;

/**
 * Created by kansei on 10.07.16.
 */
public class LoginActivity extends AppCompatActivity implements  ServiceResultReceiver.Receiver {

    public ServiceResultReceiver loginReceiver;

    EditText userName;
    EditText userPasswort;

    Button loginButton;
    Button registerButton;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_meeting_list);

        userName = (EditText) findViewById(R.id.login_tipUserName);
        userPasswort = (EditText) findViewById(R.id.login_tipUserPasswort);

        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        if (resultData.getString(CommunicationKeys.SERVICE) == CommunicationKeys.FROM_LOGIN_SERVICE) {
            switch (resultCode) {
                case 202:
                    Toast.makeText(this, "202 TEST!", Toast.LENGTH_SHORT).show();

                    switch (resultData.getString(CommunicationKeys.COMMAND)) {
                        case CommunicationKeys.GET:
                            // need it ?
                            break;
                        case CommunicationKeys.POST:
                            // a new User has registered
                            Toast.makeText(this, "Sie haben sich erfolgreich registiert", Toast.LENGTH_SHORT).show();
                            break;
                        case CommunicationKeys.PUT:
                            Toast.makeText(this, "Sie sind eingelogt", Toast.LENGTH_SHORT).show();

                            // sharePreferances save name and Password
                            SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("name", userName.getText().toString());
                            editor.putString("passwort", userPasswort.getText().toString());
                            editor.commit();
                            // TOdo move to the MeetingListactivity
                            MeetingListActivity.start(this);
                            // Todo Put it Not in the Stack
                            break;
                        default:
                            // TODO wrong Command
                            Toast.makeText(this, "Error: 500 wrong Command", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 400:
                    Toast.makeText(this, "Error 400: Bad request", Toast.LENGTH_LONG).show();
                    break;
                case 403:
                    Toast.makeText(this, "Error 403: forbidden request", Toast.LENGTH_LONG).show();
                    break;
                case 408:
                    Toast.makeText(this, "Error 408: time out", Toast.LENGTH_LONG).show();
                    break;
                case 500:
                    Toast.makeText(this, "Error 500: unexpected Error", Toast.LENGTH_LONG).show();
                    break;
            }
        } else {
            Toast.makeText(this, "Error: 500 wrong Service", Toast.LENGTH_SHORT).show();

        }
    }

    public void login(View view) {
        // todo block activity

        Intent i = new Intent(this, LoginService.class);
        loginReceiver = new ServiceResultReceiver(new Handler());
        loginReceiver.setReceiver(this);
        i.putExtra(CommunicationKeys.RECEICER, loginReceiver);
        i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.PUT);

        // TODO User google token

        // set userToken to the Service

        startService(i);
    }

    public void register(View view) {
        Intent i = new Intent(this, LoginService.class);
        loginReceiver = new ServiceResultReceiver(new Handler());
        loginReceiver.setReceiver(this);
        i.putExtra(CommunicationKeys.RECEICER, loginReceiver);
        i.putExtra(CommunicationKeys.COMMAND, CommunicationKeys.POST);



        startService(i);
    }
}

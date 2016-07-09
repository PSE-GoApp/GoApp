package edu.kit.pse.client.goapp.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by kansei on 07.07.16.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Good Morning!", Toast.LENGTH_LONG).show();

        // Start GPS service

        // Intent serviceIntent = new Intent(context,GPS_Service.class );

        /*Put extra in the intent ?
        serviceIntent.putExtra("keyWord", "Something/can be also a int,boolea,...")
        */
        //context.startService(serviceIntent);

    }
}

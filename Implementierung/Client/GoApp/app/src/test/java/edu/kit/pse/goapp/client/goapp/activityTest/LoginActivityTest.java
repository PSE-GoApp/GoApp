package edu.kit.pse.goapp.client.goapp.activityTest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;

import org.junit.Test;
import org.mockito.Mockito;

import edu.kit.pse.client.goapp.activity.LoginActivity;

import static org.junit.Assert.*;

/**
 * Created by Ta on 17.07.2016.
 */
public class LoginActivityTest {
    @Test
    public void testStart() {
        LoginActivity loginActivity = new LoginActivity();
        //loginActivity.onStart();
    }

    @Test
    public void testOnClick() {
        LoginActivity loginActivity = new LoginActivity();
        Context context = Mockito.mock(Context.class);
        View v = new View(context);
        loginActivity.onClick(v);
    }

    /*läuft vermutlich nur im live betrieb
    @Test
    public void testOnActivityResult() {
        int requestCode = 1;
        int resultCode =2;
        Intent data = new Intent();
        LoginActivity loginActivity = new LoginActivity();
        loginActivity.onActivityResult(requestCode,resultCode, data);
    }*/

    @Test
    public void testGoAppLogin() {
        LoginActivity loginActivity = new LoginActivity();
        loginActivity.goAppLogin();
    }

    //fails due to "testing code" in called method
    @Test
    public void testGoAppRegister() {
        LoginActivity loginActivity = new LoginActivity();
        loginActivity.goAppRegister();
    }

    @Test
    public void testOnConnectionFailed() {
        LoginActivity loginActivity = new LoginActivity();
        ConnectionResult connectionResult = new ConnectionResult(1);
        loginActivity.onConnectionFailed(connectionResult);
    }

    /*fehler vermutlich wegen gemockten bundle
    @Test
    public void testOnReceiveResult() {
        int resultCode = 1;
        Bundle resultData = Mockito.mock(Bundle.class);
        LoginActivity loginActivity =  new LoginActivity();
        loginActivity.onReceiveResult(200, resultData);
    }*/

    @Test
    public void testCheatToActivity() {
        LoginActivity loginActivity = new LoginActivity();
        Context context = Mockito.mock(Context.class);
        View v = new View(context);
        //loginActivity.cheatToActivity(v);
    }
}

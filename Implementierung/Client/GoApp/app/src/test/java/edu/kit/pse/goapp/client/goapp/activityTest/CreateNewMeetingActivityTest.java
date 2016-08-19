package edu.kit.pse.goapp.client.goapp.activityTest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import org.junit.Test;
import org.mockito.Mockito;

import edu.kit.pse.client.goapp.activity.CreateNewMeetingActivity;

import static org.junit.Assert.*;

/**
 * Created by Ta on 17.07.2016.
 */
public class CreateNewMeetingActivityTest {

    @Test
    public void testStartMeetingActivity() {
        CreateNewMeetingActivity newMeetingActivity = new CreateNewMeetingActivity();
        Activity activity = new Activity();
        newMeetingActivity.start(activity);
    }

    /*fails due to mocked Context
    @Test
    public void testShowPopUp() {
        CreateNewMeetingActivity newMeetingActivity = new CreateNewMeetingActivity();
        Context context = Mockito.mock(Context.class);
        View v = new View(context);
        newMeetingActivity.showPopUp(v);
    }*/

    @Test
    public void testOnClick() {
        CreateNewMeetingActivity newMeetingActivity = new CreateNewMeetingActivity();
        Context context = Mockito.mock(Context.class);
        View v = new View(context);
        newMeetingActivity.onClick(v);
    }

    @Test
    public void testOnMenuItemClickFailed() {
        CreateNewMeetingActivity newMeetingActivity = new CreateNewMeetingActivity();
        MenuItem menuItem = Mockito.mock(MenuItem.class);
        assertFalse(newMeetingActivity.onMenuItemClick(menuItem));
    }

    /*failes due to mocked MenuItem, can not contain the right ID
    @Test
    public void testOnMenuItemClick() {
        CreateNewMeetingActivity newMeetingActivity = new CreateNewMeetingActivity();
        MenuItem menuItem = Mockito.mock(MenuItem.class);

        assertTrue(newMeetingActivity.onMenuItemClick(menuItem));
    }*/

    /*Toasts propably only work in real Run
    @Test
    public void testOnReceiveResult() {
        int resultCode = 1;
        Bundle resultData = Mockito.mock(Bundle.class);
        CreateNewMeetingActivity newMeetingActivity = new CreateNewMeetingActivity();
        newMeetingActivity.onReceiveResult(resultCode, resultData);
    }*/
}

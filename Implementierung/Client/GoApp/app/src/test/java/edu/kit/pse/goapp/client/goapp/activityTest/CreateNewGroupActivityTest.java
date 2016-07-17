package edu.kit.pse.goapp.client.goapp.activityTest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.junit.Test;
import org.mockito.Mockito;

import edu.kit.pse.client.goapp.activity.CreateNewGroupActivity;

import static org.junit.Assert.*;

/**
 * Created by Ta on 16.07.2016.
 */
public class CreateNewGroupActivityTest {
   @Test
    public void testOnReceiveResult() {
       Bundle savedInstanceState = Mockito.mock(Bundle.class);
       CreateNewGroupActivity newGroupActivity = new CreateNewGroupActivity();
       Bundle bundle = Mockito.mock(Bundle.class);
       newGroupActivity.onReceiveResult(200,bundle);
    }

    @Test (expected = java.lang.NullPointerException.class)
    public void testOnReceiveResultFailed() throws Exception {
        Bundle savedInstanceState = Mockito.mock(Bundle.class);
        CreateNewGroupActivity newGroupActivity = new CreateNewGroupActivity();
        Bundle bundle = Mockito.mock(Bundle.class);
        newGroupActivity.onReceiveResult(1,bundle);
    }

    @Test
    public void testStartGroupActivity() {
        CreateNewGroupActivity newGroupActivity = new CreateNewGroupActivity();
        Activity activity = new Activity();
        newGroupActivity.start(activity);
    }

    @Test
    public void testShowPopUp() {
        CreateNewGroupActivity newGroupActivity = new CreateNewGroupActivity();
        Context context = Mockito.mock(Context.class);
        View v = new View(context);
        newGroupActivity.showPopUp(v);
    }

    @Test
    public void testOnClick() {
        CreateNewGroupActivity newGroupActivity = new CreateNewGroupActivity();
        Context context = Mockito.mock(Context.class);
        View v = new View(context);
        newGroupActivity.onClick(v);
    }

    @Test
    public void testOnMenuItemClickFailed() {
        CreateNewGroupActivity newGroupActivity = new CreateNewGroupActivity();
        MenuItem menuItem = Mockito.mock(MenuItem.class);
        assertFalse(newGroupActivity.onMenuItemClick(menuItem));
    }

    @Test
    public void testOnMenuItemClick() {
        CreateNewGroupActivity newGroupActivity = new CreateNewGroupActivity();
        MenuItem menuItem = Mockito.mock(MenuItem.class);

        assertTrue(newGroupActivity.onMenuItemClick(menuItem));
    }

    @Test
    public void testSetLists() {
        CreateNewGroupActivity newGroupActivity = new CreateNewGroupActivity();
        newGroupActivity.setLists();
    }
}

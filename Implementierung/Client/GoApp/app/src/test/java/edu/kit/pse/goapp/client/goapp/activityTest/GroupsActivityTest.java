package edu.kit.pse.goapp.client.goapp.activityTest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import org.junit.Test;
import org.mockito.Mockito;

import edu.kit.pse.client.goapp.activity.GroupsActivity;

import static org.junit.Assert.*;

/**
 * Created by Ta on 17.07.2016.
 */
public class GroupsActivityTest {
    @Test
    public void testStart() {
        GroupsActivity groupsActivity = new GroupsActivity();
        Activity activity = new Activity();
        groupsActivity.start(activity);
    }

    /*fails due to mocked context
    @Test
    public void testShowPopUp() {
        GroupsActivity groupsActivity = new GroupsActivity();
        Context context = Mockito.mock(Context.class);
        View v = new View(context);
        groupsActivity.showPopUp(v);
    }*/

    @Test
    public void testOnClick() {
        GroupsActivity groupsActivity = new GroupsActivity();
        Context context = Mockito.mock(Context.class);
        View v = new View(context);
        groupsActivity.onClick(v);
    }

    @Test
    public void testOnMenuItemClickFailed() {
        GroupsActivity groupsActivity = new GroupsActivity();
        MenuItem menuItem = Mockito.mock(MenuItem.class);
        assertFalse(groupsActivity.onMenuItemClick(menuItem));
    }

    /*faiils due to mocked MenuItem, can not contain the righht ID
    @Test
    public void testOnMenuItemClick() {
        GroupsActivity groupsActivity = new GroupsActivity();
        MenuItem menuItem = Mockito.mock(MenuItem.class);

        assertTrue(groupsActivity.onMenuItemClick(menuItem));
    }*/

    @Test
    public void testOnReceiveResult() {
        int resultCode = 1;
        Bundle resultData = Mockito.mock(Bundle.class);
        GroupsActivity groupsActivity =  new GroupsActivity();
        groupsActivity.onReceiveResult(resultCode, resultData);
    }
}

package edu.kit.pse.goapp.client.goapp.activityTest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

import edu.kit.pse.client.goapp.activity.GroupMemberActivity;
import edu.kit.pse.client.goapp.datamodels.GroupMember;

/**
 * Created by Ta on 17.07.2016.
 */
public class GroupMemberActivityTest {
    @Test
    public void testOnReceiveResult() {
        int resultCode = 1;
        Bundle resultData = Mockito.mock(Bundle.class);
        GroupMemberActivity GroupMemberActivity =  new GroupMemberActivity();
        GroupMemberActivity.onReceiveResult(resultCode, resultData);
    }

    @Test
    public void testShowPopUp() {
        GroupMemberActivity groupMemberActivity = new GroupMemberActivity();
        Context context = Mockito.mock(Context.class);
        View v = new View(context);
        groupMemberActivity.showPopUp(v);
    }

    @Test
    public void testStart() {
        GroupMemberActivity groupMemberActivity = new GroupMemberActivity();
        int groupId = 1;
        Activity activity = new Activity();
        groupMemberActivity.start(activity, groupId);
    }

    @Test
    public void testOnClick() {
        GroupMemberActivity groupMemberActivity = new GroupMemberActivity();
        Context context = Mockito.mock(Context.class);
        View v = new View(context);
        groupMemberActivity.onClick(v);
    }

    @Test
    public void testOnMenuItemClickFailed() {
        GroupMemberActivity groupMemberActivity = new GroupMemberActivity();
        MenuItem menuItem = Mockito.mock(MenuItem.class);
        assertFalse(groupMemberActivity.onMenuItemClick(menuItem));
    }

    @Test
    public void testOnMenuItemClick() {
        GroupMemberActivity groupMemberActivity = new GroupMemberActivity();
        MenuItem menuItem = Mockito.mock(MenuItem.class);

        assertTrue(groupMemberActivity.onMenuItemClick(menuItem));
    }
}

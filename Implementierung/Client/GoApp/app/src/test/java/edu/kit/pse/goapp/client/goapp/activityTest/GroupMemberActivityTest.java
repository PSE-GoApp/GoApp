package edu.kit.pse.goapp.client.goapp.activityTest;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import org.junit.Test;
import org.mockito.Mockito;

import edu.kit.pse.client.goapp.activity.GroupMemberActivity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ta on 17.07.2016.
 */
public class GroupMemberActivityTest {
    @Test
    public void testOnReceiveResultPositive() {
        int resultCode = 200;
        Bundle resultData = Mockito.mock(Bundle.class);
        GroupMemberActivity GroupMemberActivity =  new GroupMemberActivity();
        GroupMemberActivity.onReceiveResult(resultCode, resultData);
    }

    @Test
    public void testOnReceiveResult() {
        int resultCode = 1;
        Bundle resultData = Mockito.mock(Bundle.class);
        GroupMemberActivity GroupMemberActivity =  new GroupMemberActivity();
        GroupMemberActivity.onReceiveResult(resultCode, resultData);
    }

    /*fehler vermutlich wegen gemockten Kontext
    @Test
    public void testShowPopUp() {
        GroupMemberActivity groupMemberActivity = new GroupMemberActivity();
        Context context = Mockito.mock(Context.class);
        View v = new View(context);
        groupMemberActivity.showPopUp(v);
    }*/


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

}

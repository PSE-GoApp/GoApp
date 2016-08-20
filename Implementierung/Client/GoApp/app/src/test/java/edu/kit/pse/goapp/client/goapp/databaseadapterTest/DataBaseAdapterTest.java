package edu.kit.pse.goapp.client.goapp.databaseadapterTest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import edu.kit.pse.client.goapp.databaseadapter.DataBaseAdapter;
import edu.kit.pse.client.goapp.datamodels.Event;
import edu.kit.pse.client.goapp.datamodels.GPS;
import edu.kit.pse.client.goapp.datamodels.Meeting;
import edu.kit.pse.client.goapp.datamodels.MeetingConfirmation;
import edu.kit.pse.client.goapp.datamodels.Participant;
import edu.kit.pse.client.goapp.datamodels.User;

/**
 * Created by Ta on 16.07.2016.
 */
public class DataBaseAdapterTest {

    @Test
    public void testInsertEvent() {
        GPS gps = new GPS(2,3,4);
        User user = new User(8, "testUser");
        MeetingConfirmation meetingConfirmation = MeetingConfirmation.PENDING;
        Participant participant = new Participant(7, 1, user, meetingConfirmation);
        Event event = new Event(1, "testMeeting", gps, 5, 6, participant);
        Context context = Mockito.mock(Context.class);
        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(context);
        assertTrue(dataBaseAdapter.insertMeeting(event));
    }



    @Test
    public void testCreateDatabase() {
        SQLiteDatabase mockDb = Mockito.mock(SQLiteDatabase.class);
        Context mockContext = Mockito.mock(Context.class);
        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(mockContext);
        //dataBaseAdapter.onCreate(mockDb);
    }

    @Test
    public void testUpgradeDatabase() {
        SQLiteDatabase mockDb = Mockito.mock(SQLiteDatabase.class);
        int oldVersion = 1;
        int newVersion = 2;
        Context mockContext = Mockito.mock(Context.class);
        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(mockContext);
        //dataBaseAdapter.onUpgrade(mockDb, oldVersion, newVersion);
    }
}

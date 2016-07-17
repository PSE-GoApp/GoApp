package edu.kit.pse.goapp.client.goapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.Factory;

import edu.kit.pse.client.goapp.databasehandler.DataBaseHandler;
import edu.kit.pse.client.goapp.databasehandler.Entry;
import edu.kit.pse.client.goapp.databasehandler.MeetingTimeDataSource;

import static org.junit.Assert.*;

/**
 * Created by Ta on 16.07.2016.
 */
public class DataBaseHandlerTest {
    @Test
    public void testCreateDatabase() {
        SQLiteDatabase mockDb = Mockito.mock(SQLiteDatabase.class);
        Context mockContext = Mockito.mock(Context.class);
        DataBaseHandler dataBaseHandler = new DataBaseHandler(mockContext);
        dataBaseHandler.onCreate(mockDb);
    }

    @Test
    public void testUpgradeDatabase() {
        SQLiteDatabase mockDb = Mockito.mock(SQLiteDatabase.class);
        int oldVersion = 1;
        int newVersion = 2;
        Context mockContext = Mockito.mock(Context.class);
        DataBaseHandler dataBaseHandler = new DataBaseHandler(mockContext);
        dataBaseHandler.onUpgrade(mockDb, oldVersion, newVersion);
    }

    @Test
    public void testGetSetId() {
        Entry entry = new Entry();
        entry.setId(1);
        long id = entry.getId();
        assertEquals(id, 1);
    }

    @Test
    public void testGeSettMeetingId() {
        Entry entry = new Entry();
        int expectedId = 1;
        entry.setMeetingId(expectedId);
        int id = entry.getMeetingId();
        assertEquals(id, expectedId);
    }

    @Test
    public void testGetSetTimestamp() {
        Entry entry = new Entry();
        long expectedId = 1;
        entry.setTimestamp(expectedId);
        long id = entry.getTimestamp();
        assertEquals(id, expectedId);
    }

    @Test
    public void testToString() {
        Entry entry = new Entry();
        entry.setMeetingId(1);
        entry.setTimestamp(2);
        String expected = "Meeting mit Id: 1 ist am 1.0.1970 um 1:0";
        String string = entry.toString();
        assertEquals(expected, string);
    }

    @Test
    public void testOpenDatabaseSuccessfully() {
        Context context = Mockito.mock(Context.class);
        MeetingTimeDataSource meetingTimeDataSource = new MeetingTimeDataSource(context);
        meetingTimeDataSource.open();
    }

    /*@Test
    public void testOpenDatabaseSQLException() {
        Context context = Mockito.mock(Context.class);
        int mode = 0;
        SQLiteDatabase.CursorFactory cursorFactory = Mockito.mock(SQLiteDatabase.CursorFactory.class);
        Mockito.when(context.openOrCreateDatabase(Mockito.anyString(),mode, cursorFactory)).thenReturn(-1);
        MeetingTimeDataSource meetingTimeDataSource = new MeetingTimeDataSource(context);

    }*/

    @Test
    public void testCloseDatabase() {
        Context context = Mockito.mock(Context.class);
        MeetingTimeDataSource meetingTimeDataSource = new MeetingTimeDataSource(context);
        meetingTimeDataSource.close();
    }

    @Test
    public void testCreateEntry() {
        int meetingId = 1;
        long timestamp = 2;
        Context context = Mockito.mock(Context.class);
        MeetingTimeDataSource meetingTimeDataSource = new MeetingTimeDataSource(context);
        Entry entry = meetingTimeDataSource.createEntry(meetingId, timestamp);
        Entry expectedEntry = new Entry();
        assertEquals(expectedEntry,entry);
    }

}

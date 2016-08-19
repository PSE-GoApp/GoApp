package edu.kit.pse.client.goapp.databaseadapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.kit.pse.client.goapp.datamodels.Event;
import edu.kit.pse.client.goapp.datamodels.Meeting;

/**
 * Created by kansei on 05.07.16.
 */

public class DataBaseAdapter {

    private DataBaseHandler dbHelper;

    public DataBaseAdapter (Context context) {
        dbHelper = new DataBaseHandler(context);
    }

    /**
     * adds row in the database if they not contained, otherwise replace the values
     *
     * @param meeting the meeting to be added
     * @return true if the insert was successfully, otherwise false
     */
    public boolean insertMeeting(Meeting meeting) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int meetingId = meeting.getMeetingId();
        Cursor cursor = db.query(DataBaseHandler.TABLE_MEETING, null, DataBaseHandler.COLUMN_MEETING_ID+" = "+meetingId, null ,null ,null ,null);
        if (cursor.moveToNext()) { // TOdo test
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseHandler.COLUMN_TIMESTAMP, meeting.getTimestamp());
            contentValues.put(DataBaseHandler.COLUMN_DURATION, meeting.getDuration());

            int result = db.update(DataBaseHandler.TABLE_MEETING, contentValues, DataBaseHandler.COLUMN_MEETING_ID+" = "+meetingId, null);
            return true;
        } else { // TODO this seem thats works
            ContentValues values = new ContentValues();
            values.put(DataBaseHandler.COLUMN_MEETING_ID, meeting.getMeetingId());
            values.put(DataBaseHandler.COLUMN_TIMESTAMP, meeting.getTimestamp());
            values.put(DataBaseHandler.COLUMN_DURATION, meeting.getDuration());

            if (db.insert(DataBaseHandler.TABLE_MEETING, null, values) != -1) {
                // insert successful
                return true;
            }
            return false;
        }
    }

    /**
     * Returns All Meetings, that contains in the Database
     * @return Meeting List
     */
    public List<Meeting> getAllMeetings() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<Meeting> meetings = new ArrayList<>();

        Cursor cursor = db.query(DataBaseHandler.TABLE_MEETING, null, null, null, null, null,null);
        while (cursor.moveToNext()) {
            Meeting meeting;
            int id;
            int duration;
            long timeStamp;
            // Error ? COlUMNs was deleted!
                int index = cursor.getColumnIndex(DataBaseHandler.COLUMN_MEETING_ID);
                id = cursor.getInt(index);

                index = cursor.getColumnIndex(DataBaseHandler.COLUMN_TIMESTAMP);
                timeStamp = cursor.getLong(index);

                index = cursor.getColumnIndex(DataBaseHandler.COLUMN_DURATION);
                duration = cursor.getInt(index);

            meeting = new Event(id, null, null, timeStamp, duration, null);
            meetings.add(meeting);
        }
        return meetings;
    }

    /**
     * SQL:
     * Select *, from Meeting where MEETING_ID = meetingID
     * @param meetingId is the ID form Meeting
     * @return a Meeting thats Contains in the SQL, with ID, Timestamp and Duration and
     * other Parameter from meeting are null, otherwise returns null
     *
     */
    public Meeting getMeeting(int meetingId) {
        Meeting meeting;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(DataBaseHandler.TABLE_MEETING, null, DataBaseHandler.COLUMN_MEETING_ID+" = "+meetingId, null, null, null,null);
        if (cursor.moveToNext()) {
            int id;
            int duration;
            long timeStamp;
            try {
                int index = cursor.getColumnIndex(DataBaseHandler.TABLE_MEETING);
                id = cursor.getInt(index);

                index = cursor.getColumnIndex(DataBaseHandler.COLUMN_TIMESTAMP);
                timeStamp = cursor.getLong(index);

                index = cursor.getColumnIndex(DataBaseHandler.COLUMN_DURATION);
                duration = cursor.getInt(index);

            } catch (Exception e) {
                Log.e("DataBaseAdapter" , e.getMessage());
                return null;
            }
            meeting = new Event(id, null, null, timeStamp, duration, null);
            return meeting;
        } else {
            return null;
        }
    }

    /**
     * deletes a row
     *
     * @param meetingId the meeting Id
     * @return True if the Row deleted, otherwise false
     */
    public boolean deleteProduct(int meetingId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int numberOfDeletRows = db.delete(DataBaseHandler.TABLE_MEETING, DataBaseHandler.COLUMN_MEETING_ID+" = "+meetingId,null);
        if (numberOfDeletRows > 0) {
            return true;
        } else {
            return false;
        }
    }




    /**
     * Delete All row in the DB
     * @return true if all rows deleted, otherwise false
     */
    public boolean deletAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int numberOfDeleteRows = db.delete(DataBaseHandler.TABLE_MEETING, null, null);
        if (numberOfDeleteRows > 0) {
            return true;
        } else {
            return false;
        }
    }



    private static class DataBaseHandler extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "MeetingStarts.db";
        private static final int DATABASE_VERSION = 1;
        public static final String TABLE_MEETING = "MEETING";
        public static final String COLUMN_MEETING_ID = "MEETING_ID";
        public static final String COLUMN_TIMESTAMP = "TIMESTAMP";
        public static final String COLUMN_DURATION = "DURATION";

    /* Datenbank erstellt mit den namen MEETINGS
    ID als primary key autoincrement
    gespeichert werten in den Db MEETING_ID, TIMESTAMP, DURATION
     */
        private static final String TABLE_CREATE_VERLAUF = ""
                + "create table " + TABLE_MEETING + " ("
                + "MEETING_ID integer primary key, "
                + "TIMESTAMP long, "
                + "DURATION integer)";

        /**
         * constructor
         *
         * @param context
         */
        public DataBaseHandler(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

            // TOdo test (vor debugging)
            Log.e("DataBaseHandler", "Constructor DatabaseHandler was called");
        }

        /**
         * method is called when table is created
         *
         * @param db the database
         */
        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(TABLE_CREATE_VERLAUF);
            Log.e("DataBaseHandler", "A new DB created");
        }

        /**
         * method called on upgrade
         *
         * @param db         database
         * @param oldVersion old version
         * @param newVersion new version
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.e(DataBaseHandler.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEETING);
            onCreate(db);
        }
    }

}
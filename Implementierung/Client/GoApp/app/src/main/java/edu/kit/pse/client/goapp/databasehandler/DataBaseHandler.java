package edu.kit.pse.client.goapp.databasehandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import edu.kit.pse.client.goapp.datamodels.Meeting;

/**
 * Created by kansei on 05.07.16.
 */
public class DataBaseHandler extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "MeetingStarts.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_MEETING = "MEETINGS";
    public static final String COLUMN_MEETING_ID = "MEETING_ID";
    public static final String COLUMN_TIMESTAMP = "TIMESTAMP";
    public static final String COLUMN_DURATION = "DURATION";

    /* Datenbank erstellt mit den namen MEETINGS
    ID als primary key autoincrement
    gespeichert werten in den Db MEETING_ID, TIMESTAMP
     */


    private static final String TABLE_CREATE_VERLAUF = ""
            + "create table MEETINGS("
            + "MEETING_ID int primary key, "
            + "TIMESTAMP long"
            + "DURATION int";

    /**
     * constructor
     * @param context
     */
    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * method is called when table is created
     * @param db the database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_VERLAUF);
    }

    /**
     * method called on upgrade
     * @param db database
     * @param oldVersion old version
     * @param newVersion new version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DataBaseHandler.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS SCANITEM");
        onCreate(db);
    }

    /**
     * adds row to database
     * @param meeting the meeting to be added
     */
    public void addMeeting(Meeting meeting){
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEETING_ID, meeting.getMeetingId());
        values.put(COLUMN_TIMESTAMP, meeting.getTimestamp());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_MEETING, null, values);
        db.close();
    }

    /**
     * deletes a row
     * @param meetingId the meeting Id
     */
    public void deleteProduct(int meetingId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MEETING + " WHERE " + COLUMN_MEETING_ID + "=\"" + meetingId + "\";");
    }
}

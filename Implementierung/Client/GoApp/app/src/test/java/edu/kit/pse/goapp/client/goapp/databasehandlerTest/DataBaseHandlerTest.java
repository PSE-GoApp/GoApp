package edu.kit.pse.goapp.client.goapp.databasehandlerTest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.Mockito;

import edu.kit.pse.client.goapp.databasehandler.DataBaseHandler;

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
}

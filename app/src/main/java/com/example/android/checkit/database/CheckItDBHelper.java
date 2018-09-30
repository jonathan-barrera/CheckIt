package com.example.android.checkit.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.checkit.database.CheckItContract.CheckOutEntry;
import com.example.android.checkit.database.CheckItContract.ThingEntry;

/**
 * Created by jonathanbarrera on 9/25/18.
 */

public class CheckItDBHelper extends SQLiteOpenHelper {

    // Version number
    public static final int DATABASE_VERSION = 1;

    // Name of the database
    public static final String DATABASE_NAME = "checkit.db";

    // Constructor
    public CheckItDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_CHECKOUTS_TABLE = "CREATE TABLE " + CheckOutEntry.TABLE_NAME_CHECK_OUTS +
                " (" + CheckOutEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CheckOutEntry.COLUMN_CHECKOUT_DATE + " INTEGER NON NULL, " +
                CheckOutEntry.COLUMN_CHECKOUT_TIME + " INTEGER NON NULL, " +
                CheckOutEntry.COLUMN_CHECKOUT_ACCOM_NAME + " TEXt NON NULL);";

        String SQL_CREATE_THINGS_TABLE = "CREATE TABLE " + ThingEntry.TABLE_NAME_THINGS + " (" +
                ThingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ThingEntry.COLUMN_THINGS + " TEXT NON NULL);";

        // Execute the SQL statements
        db.execSQL(SQL_CREATE_CHECKOUTS_TABLE);
        db.execSQL(SQL_CREATE_THINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

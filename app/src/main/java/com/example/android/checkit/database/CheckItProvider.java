package com.example.android.checkit.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.android.checkit.database.CheckItContract.CheckOutEntry;
import com.example.android.checkit.database.CheckItContract.ThingEntry;

import timber.log.Timber;

/**
 * Created by jonathanbarrera on 9/25/18.
 */

public class CheckItProvider extends ContentProvider {

    // Member variables
    private CheckItDBHelper mDbHelper;

    // URI codes
    private static final int CHECK_OUTS = 100;
    private static final int CHECK_OUT_ID = 101;
    private static final int THINGS = 200;
    private static final int THING_ID = 201;

    // UriMatcher to match a content Uri to a corresponding code.
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer, runs the first time this class is called
    static {
        // Uri Matcher for entire checkouts table
        sUriMatcher.addURI(CheckItContract.CONTENT_AUTHORITY, CheckItContract.PATH_CHECK_OUTS, CHECK_OUTS);
        // Uri Matcher for one row in the checkouts table
        sUriMatcher.addURI(CheckItContract.CONTENT_AUTHORITY,
                CheckItContract.PATH_CHECK_OUTS + "/#", CHECK_OUT_ID);
        // Uri Matcher for the entire things table
        sUriMatcher.addURI(CheckItContract.CONTENT_AUTHORITY, CheckItContract.PATH_THINGS, THINGS);
        // Uri Matcher for one row in the things table
        sUriMatcher.addURI(CheckItContract.CONTENT_AUTHORITY,
                CheckItContract.PATH_THINGS + "/#", THING_ID);
    }

    // Initialize the provider and the database helper
    @Override
    public boolean onCreate() {
        mDbHelper = new CheckItDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Get the readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result
        Cursor cursor;

        // Match the URI to a specific code using the UriMatcher
        int match = sUriMatcher.match(uri);
        switch (match) {
            case CHECK_OUTS:
                cursor = database.query(CheckOutEntry.TABLE_NAME_CHECK_OUTS, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case CHECK_OUT_ID:
                selection = CheckOutEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(CheckOutEntry.TABLE_NAME_CHECK_OUTS, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case THINGS:
                cursor = database.query(ThingEntry.TABLE_NAME_THINGS, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case THING_ID:
                selection = CheckOutEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(ThingEntry.TABLE_NAME_THINGS, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI: " + uri);
        }
        // Set notification URI on the cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CHECK_OUTS:
                return CheckOutEntry.CONTENT_LIST_TYPE;
            case CHECK_OUT_ID:
                return CheckOutEntry.CONTENT_ITEM_TYPE;
            case THINGS:
                return ThingEntry.CONTENT_LIST_TYPE;
            case THING_ID:
                return ThingEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown uri: " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CHECK_OUTS:
                return insertCheckOut(uri, values);
            case THINGS:
                return insertThing(uri, values);
            default:
                throw new IllegalArgumentException("Insertion not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    // Helper method for inserting a new row into the checkouts table
    private Uri insertCheckOut(Uri uri, ContentValues values) {
        // First check that the Date is valid
        long date = values.getAsLong(CheckOutEntry.COLUMN_CHECKOUT_DATE);
        if (date < 0) {
            throw new IllegalArgumentException("Check Out requires valid date.");
        }

        // Check that the time is valid
        int time = values.getAsInteger(CheckOutEntry.COLUMN_CHECKOUT_TIME);
        if (time < 0) {
            throw new IllegalArgumentException("Check Out requires valid time.");
        }

        // Check that the accommodation name is valid
        String name = values.getAsString(CheckOutEntry.COLUMN_CHECKOUT_ACCOM_NAME);
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Check Out requires valid accommodation name");
        }

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new checkout information into the database
        long id = database.insert(CheckOutEntry.TABLE_NAME_CHECK_OUTS, null, values);

        // Check if the insertion succeeded
        if (id == -1) {
            Timber.d("Insertion failed for: " + uri);
            return null;
        }

        // Notify listeners that the data has changed for the checkout content uri
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    // Helper method for inserting a new row into the things table
    private Uri insertThing(Uri uri, ContentValues values) {
        // Check that the thing has a valid name
        String name = values.getAsString(ThingEntry.COLUMN_THINGS);
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Thing requires valid name.");
        }

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new thing information into the database
        long id = database.insert(ThingEntry.TABLE_NAME_THINGS, null, values);

        // Check if the insertion succeeded
        if (id == -1) {
            Timber.d("Insertion failed for: " + uri);
            return null;
        }

        // Notify listeners that the data has changed for the things content uri
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }
}

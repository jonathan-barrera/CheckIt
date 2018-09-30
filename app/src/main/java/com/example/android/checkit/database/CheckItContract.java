package com.example.android.checkit.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android.checkit.models.CheckOutEvent;

/**
 * Created by jonathanbarrera on 9/25/18.
 */

public class CheckItContract {

    private CheckItContract(){}

    // Content URI statics
    public static final String CONTENT_AUTHORITY = "com.example.android.checkit";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_CHECK_OUTS = "checkouts";
    public static final String PATH_THINGS = "things";

    public static abstract class CheckOutEntry implements BaseColumns {
        // The content uri to access the check out data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CHECK_OUTS);

        // The MIME type of the CONTENT_URI for a list of checkouts
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHECK_OUTS;

        // The MIME type of the CONTENT_URI for a single checkout
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHECK_OUTS;

        public static final String TABLE_NAME_CHECK_OUTS = "checkouts";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_CHECKOUT_DATE = "date";
        public static final String COLUMN_CHECKOUT_TIME = "time";
        public static final String COLUMN_CHECKOUT_ACCOM_NAME = "name";
    }

    public static abstract class ThingEntry implements BaseColumns {
        // The content uri to access the check out data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_THINGS);

        // The MIME type of the CONTENT_URI for a list of checkouts
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_THINGS;

        // The MIME type of the CONTENT_URI for a single checkout
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_THINGS;

        public static final String TABLE_NAME_THINGS = "things";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_THINGS = "things-coloumn";
    }
}

package com.example.banana.sqlitelearningproject.contentProvider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.banana.sqlitelearningproject.sqlite.MySQLiteHelper;

/**
 * Created by Citrixer on 2/26/15.
 */
public class ItemProvider extends ContentProvider {

    private MySQLiteHelper mDB;

    private static final String AUTHORITY = "com.example.banana.ItemProvider";
    public static final int ITEMS = 100;
    public static final int ITEM_ID = 110;

    private static final String ITEMS_BASE_PATH = "items";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ITEMS_BASE_PATH);

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/mt-tutorial";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/mt-tutorial";


    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, ITEMS_BASE_PATH, ITEMS);
        sURIMatcher.addURI(AUTHORITY, ITEMS_BASE_PATH + "/#", ITEM_ID);
    }

    @Override
    public boolean onCreate() {

        mDB = new MySQLiteHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MySQLiteHelper.TABLE_ITEMS);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case ITEM_ID:
                queryBuilder.appendWhere(MySQLiteHelper.KEY_ROW_ID + "="
                        + uri.getLastPathSegment());
                break;
            case ITEMS:
                // no filter
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(mDB.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

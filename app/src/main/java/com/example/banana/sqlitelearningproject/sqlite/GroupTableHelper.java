package com.example.banana.sqlitelearningproject.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.banana.sqlitelearningproject.model.Group;

import java.util.ArrayList;
import java.util.List;


public class GroupTableHelper extends SQLiteOpenHelper {

    static private final String TAG = "SQL-LEARN";

    // Database Name
    private static final String DATABASE_NAME = "ItemDB";

    // Items Table Columns names
    public static final String KEY_ROW_ID = "_id";
    public static final String KEY_GROUP = "groupId";
    public static final String KEY_TITLE = "title";
    public static final String KEY_FRONT = "front";
    public static final String KEY_BACK = "back";
    public static final String KEY_BOOKMARK = "bookMark";


    // Items table name
    public static final String TABLE_ITEMS = "items";
    // Groups table name
    public static final String TABLE_GROUP = "Groups";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Groups Table Columns names
    public static final String KEY_GROUP_ROW_ID = "_id";
    public static final String KEY_GROUP_NAME = "groupName";

    private static final String[] COLUMNS = {
            KEY_GROUP_ROW_ID, KEY_GROUP_NAME};

    public GroupTableHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create item table
        String CREATE_ITEM_TABLE = "create table " + TABLE_ITEMS + " ( "
                + KEY_ROW_ID + " integer primary key autoincrement , "
                + KEY_GROUP + " text  , "
                + KEY_TITLE + "  text  , "
                + KEY_FRONT + "  text  , "
                + KEY_BACK + "  text  , "
                + KEY_BOOKMARK + "  integer  ) ";

        // create items table
        db.execSQL(CREATE_ITEM_TABLE);

        Log.d(TAG, "CREATE_GROUP_TABLE");
        String CREATE_GROUP_TABLE = "create table " + TABLE_GROUP + " ( "
                + KEY_GROUP_ROW_ID + " integer primary key autoincrement , "
                + KEY_GROUP_NAME + " text  ) ";

        // create groups table
        db.execSQL(CREATE_GROUP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_GROUP);

        // create fresh books table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */
    public void addGroup(Group group) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_GROUP_NAME, group.getGroupName()); // get groupName

        // 3. insert
        db.insert(TABLE_GROUP, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Group getGroup(int groupId) {

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_GROUP, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[]{String.valueOf(groupId)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build group object
        Group group = new Group();
        group.setGroupId(Integer.parseInt(cursor.getString(0)));
        group.setGroupName(cursor.getString(1));

        // 5. return group
        return group;
    }

    // Get All Groups
    public List<String> getAllGroups() {
        List<String> groups = new ArrayList<>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_GROUP;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build group and add it to list
        Group group = null;
        if (cursor.moveToFirst()) {
            do {
                group = new Group();
                group.setGroupId(Integer.parseInt(cursor.getString(0)));
                group.setGroupName(cursor.getString(1));

                // Add group to groups
                groups.add(group.getGroupName());

            } while (cursor.moveToNext());
        }

        // return groups
        return groups;
    }

    public Cursor getAllGroupsCursor() {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT  * FROM " + TABLE_GROUP;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    // Updating single group
    public int updateGroup(Group group) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("groupName", group.getGroupName()); // get title


        // 3. updating row
        int i = db.update(TABLE_GROUP, //table
                values, // column/value
                KEY_GROUP_ROW_ID + " = ?", // selections
                new String[]{String.valueOf(group.getGroupId())}); //selection args

        // 4. close
        db.close();

        return i;
    }

    // Deleting single group
    public void deleteGroup(int groupId) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_GROUP,
                KEY_GROUP_ROW_ID + " = ?",
                new String[]{String.valueOf(groupId)});

        // 3. close
        db.close();

        Log.d(TAG, "deleteGroup: " + String.valueOf(groupId));
    }
}

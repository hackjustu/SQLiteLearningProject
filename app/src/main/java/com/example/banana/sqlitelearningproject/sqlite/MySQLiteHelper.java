package com.example.banana.sqlitelearningproject.sqlite;

import java.util.LinkedList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.banana.sqlitelearningproject.model.Item;

public class MySQLiteHelper extends SQLiteOpenHelper {

    static private final String TAG = "SQL-LEARN";

    // Database Name
    private static final String DATABASE_NAME = "ItemDB";

    // Items table name
    public static final String TABLE_ITEMS = "items";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Items Table Columns names
    public static final String KEY_ROW_ID = "_id";
    public static final String KEY_GROUP = "groupId";
    public static final String KEY_TITLE = "title";
    public static final String KEY_FRONT = "front";
    public static final String KEY_BACK = "back";

    private static final String[] COLUMNS = {KEY_ROW_ID,KEY_GROUP,KEY_TITLE,KEY_FRONT,KEY_BACK};

    public MySQLiteHelper(Context context) {
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
                + KEY_BACK + "  text  ) ";

        // create items table
        db.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_ITEMS);

        // create fresh books table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */
    public void addItem(String groupId ,Item item){
        Log.d(TAG, "addItem: " + item.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_GROUP, groupId); // get groupId
        values.put(KEY_TITLE, item.getTitle()); // get title
        values.put(KEY_FRONT, item.getFront()); // get front
        values.put(KEY_BACK, item.getBack()); // get back

        // 3. insert
        db.insert(TABLE_ITEMS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Item getItem(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_ITEMS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build item object
        Item item = new Item();
        item.setId(Integer.parseInt(cursor.getString(0)));
        item.setTitle(cursor.getString(2));
        item.setFront(cursor.getString(3));
        item.setBack(cursor.getString(4));

        Log.d(TAG, "getItem("+id+")" + item.toString());

        // 5. return item
        return item;
    }

    // Get All Books
    public List<Item> getAllItems() {
        List<Item> items = new LinkedList<Item>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_ITEMS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build item and add it to list
        Item item = null;
        Log.d(TAG, "getAllItems(): \n");
        if (cursor.moveToFirst()) {
            do {
                item = new Item();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setTitle(cursor.getString(2));
                item.setFront(cursor.getString(3));
                item.setBack(cursor.getString(4));

                // Add item to items
                items.add(item);

                Log.d(TAG, cursor.getString(1) + ": " + item.toString() + '\n');
            } while (cursor.moveToNext());
        }

        // return items
        return items;
    }

    // Updating single item
    public int updateItem(String groupId ,Item item) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("groupId", groupId); // get groupId
        values.put("title", item.getTitle()); // get title
        values.put("front", item.getFront()); // get front
        values.put("back", item.getBack()); // get back

        // 3. updating row
        int i = db.update(TABLE_ITEMS, //table
                values, // column/value
                KEY_ROW_ID+" = ?", // selections
                new String[] { String.valueOf(item.getId()) }); //selection args

        // 4. close
        db.close();

        return i;
    }

    // Deleting single item
    public void deleteItem(String groupId ,Item item) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_ITEMS,
                KEY_ROW_ID+" = ?",
                new String[] { String.valueOf(item.getId()) });

        // 3. close
        db.close();

        Log.d(TAG, "deleteItem: " + item.toString());
    }
}

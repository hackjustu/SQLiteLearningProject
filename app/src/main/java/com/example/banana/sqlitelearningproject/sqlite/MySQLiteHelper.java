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

    static private final String TAG = "SQL-LEARN";;

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ItemDB";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create item table
        String CREATE_ITEM_TABLE = "CREATE TABLE items ( " +
                "orderKey INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, "+
                "front TEXT, "+
                "back TEXT )";

        // create items table
        db.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS items");

        // create fresh books table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */

    // Books table name
    private static final String TABLE_ITEMS = "items";

    // Books Table Columns names
    private static final String KEY_ID = "orderKey";
    private static final String KEY_TITLE = "title";
    private static final String KEY_FRONT = "front";
    private static final String KEY_BACK = "back";

    private static final String[] COLUMNS = {KEY_ID,KEY_TITLE,KEY_FRONT,KEY_BACK};

    public void addItem(Item item){
        Log.d(TAG, "addItem: " + item.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
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
        item.setTitle(cursor.getString(1));
        item.setFront(cursor.getString(2));
        item.setBack(cursor.getString(3));

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
        if (cursor.moveToFirst()) {
            do {
                item = new Item();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setTitle(cursor.getString(1));
                item.setFront(cursor.getString(2));
                item.setBack(cursor.getString(3));

                // Add item to items
                items.add(item);
            } while (cursor.moveToNext());
        }

        Log.d(TAG, "getAllItems(): " + items.toString());

        // return items
        return items;
    }

    // Updating single item
    public int updateItem(Item item) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("title", item.getTitle()); // get title
        values.put("front", item.getFront()); // get front
        values.put("back", item.getBack()); // get back

        // 3. updating row
        int i = db.update(TABLE_ITEMS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(item.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    // Deleting single item
    public void deleteItem(Item item) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_ITEMS,
                KEY_ID+" = ?",
                new String[] { String.valueOf(item.getId()) });

        // 3. close
        db.close();

        Log.d(TAG, "deleteItem: " + item.toString());

    }
}

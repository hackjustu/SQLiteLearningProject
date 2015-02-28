package com.example.banana.sqlitelearningproject.model;

import android.content.Context;
import android.util.Log;

import com.example.banana.sqlitelearningproject.sqlite.ItemTableHelper;

import java.util.List;

/**
 * Created by Citrixer on 2/27/15.
 */
public class ItemFactory {

    static private final String TAG = "SQL-LEARN";

    private static List<Item> mItemList;
    private static ItemTableHelper dbhelper;

    public static List<Item> getItemList(Context ctx) {

        if (mItemList != null) {
            Log.d(TAG, "mItemList != null");
            return mItemList;
        }

        Log.d(TAG, "mItemList == null, retrieve data from database");
        //TODO: Retrieve ItemList from Database
        dbhelper = new ItemTableHelper(ctx);
        mItemList = dbhelper.getAllItems();

        return mItemList;
    }

    public static void createItem() {


    }

    public static void deleteItem(final int itemId, int position) {
        Log.d(TAG, "ItemFactory.deleteItem: " + String.valueOf(position));

        Runnable runnable = new Runnable() {
            public void run() {
                dbhelper.deleteItem(itemId);
                Log.d(TAG, "Deletion updated in Database.");
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }
}

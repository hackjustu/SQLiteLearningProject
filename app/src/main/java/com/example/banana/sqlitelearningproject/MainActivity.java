package com.example.banana.sqlitelearningproject;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.banana.sqlitelearningproject.contentProvider.ItemProvider;
import com.example.banana.sqlitelearningproject.model.Item;
import com.example.banana.sqlitelearningproject.sqlite.MySQLiteHelper;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.*;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    static private final String TAG = "SQL-LEARN";

    ListView mlistView;
    SimpleCursorAdapter mAdapter;
    MySQLiteHelper dbHelper;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       dbHelper = new MySQLiteHelper(this);

        /**
         * CRUD Operations
         * */
        // add Books
        dbHelper.addItem("Apple", new Item("title_0", "front_0", "back_0"));
        dbHelper.addItem("Google", new Item("title_1", "front_1", "back_1"));
        dbHelper.addItem("Apple", new Item("title_2", "front_2", "back_2"));
        dbHelper.addItem("Google", new Item("title_3", "front_3", "back_3"));
        dbHelper.addItem("Apple", new Item("title_4", "front_4", "back_4"));
        dbHelper.addItem("Google", new Item("title_5", "front_5", "back_5"));
        dbHelper.addItem("Apple", new Item("title_6", "front_6", "back_6"));
        dbHelper.addItem("Google", new Item("title_7", "front_7", "back_7"));
        dbHelper.addItem("Apple", new Item("title_8", "front_8", "back_8"));
        dbHelper.addItem("Google", new Item("title_9", "front_9", "back_9"));

        // get all items
        final List<Item> list = dbHelper.getAllItems();

        // delete one item
//        dbHelper.deleteItem("Apple", list.get(0));

        // get all items
        dbHelper.getAllItems();

        /****************************************************************/

        mlistView = (ListView) findViewById(R.id.listview);

        String[] uiBindFrom = { MySQLiteHelper.KEY_TITLE, MySQLiteHelper.KEY_FRONT, MySQLiteHelper.KEY_BACK };
        int[] uiBindTo = { R.id.title, R.id.front, R.id.back };

        mAdapter = new SimpleCursorAdapter(getBaseContext(),
                R.layout.listview_item_layout,
                null,
                uiBindFrom,
                uiBindTo,
                0);

        mlistView.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(0, null, this);

        bt = (Button)findViewById(R.id.button);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // Learn how to delete...
                Item toDelete = dbHelper.getAllItems().get(0);
                dbHelper.deleteItem("Apple", toDelete);
                Cursor newCursor = dbHelper.getAllItemsCursor();
                mAdapter.swapCursor(newCursor);
            }
        });
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String SELECTION = MySQLiteHelper.KEY_GROUP + "=?";
        List<String> selectionArgs = new ArrayList<String>();
        selectionArgs.add("Apple");
        final String[] SELECTION_ARGS = new String[selectionArgs.size()];
        selectionArgs.toArray(SELECTION_ARGS);

        Uri uri = ItemProvider.CONTENT_URI;
        return new android.support.v4.content.CursorLoader(
                this,
                uri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }
}

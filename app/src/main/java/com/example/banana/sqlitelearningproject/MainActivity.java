package com.example.banana.sqlitelearningproject;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.banana.sqlitelearningproject.adapter.ItemArrayAdapter;
import com.example.banana.sqlitelearningproject.contentProvider.ItemProvider;
import com.example.banana.sqlitelearningproject.model.Group;
import com.example.banana.sqlitelearningproject.model.Item;
import com.example.banana.sqlitelearningproject.model.ItemFactory;
import com.example.banana.sqlitelearningproject.sqlite.GroupTableHelper;
import com.example.banana.sqlitelearningproject.sqlite.ItemTableHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.OnClickListener;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    static private final String TAG = "SQL-LEARN";

    ListView mlistView;
    SimpleCursorAdapter mAdapter;
    ItemTableHelper itemTableHelper;
    GroupTableHelper groupTableHelper;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       itemTableHelper = new ItemTableHelper(this);

        /**
         * CRUD Operations
         * */
        // add Items
        itemTableHelper.addItem("Apple", new Item("title_0", "front_0", "back_0", 1));
        itemTableHelper.addItem("Google", new Item("title_1", "front_1", "back_1", 0));
        itemTableHelper.addItem("Apple", new Item("title_2", "front_2", "back_2", 1));
        itemTableHelper.addItem("Tesla", new Item("title_3", "front_3", "back_3", 0));
        itemTableHelper.addItem("Apple", new Item("title_4", "front_4", "back_4", 0));
        itemTableHelper.addItem("Google", new Item("title_5", "front_5", "back_5", 1));
        itemTableHelper.addItem("Apple", new Item("title_6", "front_6", "back_6", 1));
        itemTableHelper.addItem("Tesla", new Item("title_7", "front_7", "back_7", 1));
        itemTableHelper.addItem("Apple", new Item("title_8", "front_8", "back_8", 0));
        itemTableHelper.addItem("Google", new Item("title_9", "front_9", "back_9", 0));


        groupTableHelper = new GroupTableHelper(this);

        // add Groups
        groupTableHelper.addGroup(new Group("Apple"));
        groupTableHelper.addGroup(new Group("Google"));
        groupTableHelper.addGroup(new Group("Tesla"));

        // get all items
//        final List<Item> list = itemTableHelper.getAllItems();

        // delete one item
//        itemTableHelper.deleteItem("Apple", list.get(0));

        // get all items
//        itemTableHelper.getAllItems();

        /****************************************************************/

        mlistView = (ListView) findViewById(R.id.listview);

        /**/

        final ItemArrayAdapter itemArrayAdapter = new ItemArrayAdapter(
                this,
                (ArrayList<Item>)ItemFactory.getItemList(this));


        ArrayAdapter groupArrayAdapter = new ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                groupTableHelper.getAllGroups());

        mlistView.setAdapter(groupArrayAdapter);
        /**/

//        String[] uiBindFrom = { ItemTableHelper.KEY_TITLE, ItemTableHelper.KEY_FRONT, ItemTableHelper.KEY_BACK };
//        int[] uiBindTo = { R.id.title, R.id.front, R.id.back };
//
//        mAdapter = new SimpleCursorAdapter(getBaseContext(),
//                R.layout.listview_item_layout,
//                null,
//                uiBindFrom,
//                uiBindTo,
//                0);
//
//        mlistView.setAdapter(mAdapter);
//
//        getSupportLoaderManager().initLoader(0, null, this);
//
        bt = (Button)findViewById(R.id.button);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

//                // Learn how to delete...
//                Item toDelete = itemTableHelper.getAllItems().get(0);
//                itemTableHelper.deleteItem("Apple", toDelete);
//                Cursor newCursor = itemTableHelper.getAllItemsCursor();
//                mAdapter.swapCursor(newCursor);

                animateRemoval(itemArrayAdapter, mlistView, 0);
            }
        });
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String SELECTION = ItemTableHelper.KEY_GROUP + "=?";
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

    // Adapted from Google I/O 2013
    public void animateRemoval(final ArrayAdapter adapter,
                               final ListView listview,
                               int position) {

        final HashMap<Long, Integer> mItemIdTopMap = new HashMap<>();

        int firstVisiblePosition = listview.getFirstVisiblePosition();

        for (int i = 0; i < listview.getChildCount(); ++i) {

            int tmpPosition = firstVisiblePosition + i;

            if (tmpPosition != position) {
                View child = listview.getChildAt(i);
                long itemId = adapter.getItemId(tmpPosition);
                mItemIdTopMap.put(itemId, child.getTop());
            }
        }
        // Delete the item from the adapter
        Item toDelete = (Item) adapter.getItem(position);
        ItemFactory.deleteItem(toDelete.getId(), position);
        adapter.remove(toDelete);

//        Item toDelete = itemTableHelper.getAllItems().get(2);
//        itemTableHelper.deleteItem("Apple", toDelete);
//        Cursor newCursor = itemTableHelper.getAllItemsCursor();
//        adapter.swapCursor(newCursor);
        //

        final ViewTreeObserver observer = listview.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listview.getFirstVisiblePosition();
                for (int i = 0; i < listview.getChildCount(); ++i) {
                    final View child = listview.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = adapter.getItemId(position);
                    Integer startTop = mItemIdTopMap.get(itemId);
                    int top = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(300).translationY(0);
                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable() {
                                    public void run() {

                                        listview.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                            }
                        }
                    } else {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on neighboring views.
                        int childHeight = child.getHeight() + listview.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(300).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                public void run() {

                                    listview.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                        }
                    }
                }
                mItemIdTopMap.clear();
                return true;
            }
        });
    }
}

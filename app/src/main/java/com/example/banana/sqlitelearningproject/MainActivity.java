package com.example.banana.sqlitelearningproject;

import android.app.Activity;
import android.os.Bundle;


import com.example.banana.sqlitelearningproject.model.Item;
import com.example.banana.sqlitelearningproject.sqlite.MySQLiteHelper;

import java.util.List;


public class MainActivity extends Activity {

    static private final String TAG = "SQL-LEARN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MySQLiteHelper db = new MySQLiteHelper(this);

        /**
         * CRUD Operations
         * */
        // add Books
        db.addItem(new Item("Android Application Development Cookbook", "Wei Meng Lee", "Cool!"));
        db.addItem(new Item("Android Programming: The Big Nerd Ranch Guide", "Bill Phillips and Brian Hardy", "Amazing!"));
        db.addItem(new Item("Learn Android App Development", "Wallace Jackson", "Awesome!"));

        // get all books
        List<Item> list = db.getAllItems();

        // delete one book
        db.deleteItem(list.get(0));

        // get all books
        db.getAllItems();
    }

}

package com.example.android.bookinventory;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import com.example.android.bookinventory.data.BookContract.BookEntry;
import com.example.android.bookinventory.data.BookDbHelper;

public class MainActivity extends AppCompatActivity {
    BookDbHelper mDbHelper = new BookDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        ListView BookListView = findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        BookListView.setEmptyView(emptyView);
        displayDatabaseInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void displayDatabaseInfo() {
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_PRICE,
                BookEntry.COLUMN_QUANTITY,
                BookEntry.COLUMN_SUPPLIER_NAME,
                BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };
        Cursor cursor = getContentResolver().query(BookEntry.CONTENT_URI, projection, null, null, null);

        ListView BookListView = findViewById(R.id.list);
        BookCursorAdapter adapter = new BookCursorAdapter(this, cursor);
        BookListView.setAdapter(adapter);
    }

}

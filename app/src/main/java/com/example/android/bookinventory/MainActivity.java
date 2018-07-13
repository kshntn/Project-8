package com.example.android.bookinventory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.android.bookinventory.data.BookDbHelper;
import com.example.android.bookinventory.data.BookContract.BookEntry;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void Submit(View view){
        Intent i = new Intent(MainActivity.this, EditorActivity.class);
        startActivity(i);
    }
    public void plus(View view){
        BookDbHelper mDbHelper=new BookDbHelper(this);
        SQLiteDatabase db=mDbHelper.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME,"hi");
        values.put(BookEntry.COLUMN_PRICE,7);
        values.put(BookEntry.COLUMN_QUANTITY,8);
        values.put(BookEntry.COLUMN_SUPPLIER_NAME,"now");
        values.put(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER,"u");

        long newRowId=db.insert(BookEntry.TABLE_NAME,null,values);
        if (newRowId == -1) {
            Toast.makeText(this, "Error with saving Book", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Book saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }
}

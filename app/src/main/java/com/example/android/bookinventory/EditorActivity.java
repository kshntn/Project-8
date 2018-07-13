package com.example.android.bookinventory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.bookinventory.data.BookContract;
import com.example.android.bookinventory.data.BookDbHelper;
import com.example.android.bookinventory.data.BookContract.BookEntry;

public class EditorActivity extends AppCompatActivity {
private EditText mProductNameEditText;
private EditText mPriceEditText;
private EditText mQuantityEditText;
private EditText mSuppNameEditText;
private EditText mSuppPhoneNoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mProductNameEditText=findViewById(R.id.ProdName);
        mPriceEditText=findViewById(R.id.Price);
        mQuantityEditText =findViewById(R.id.Quantity);
        mSuppNameEditText =findViewById(R.id.SuppName);
        mSuppPhoneNoEditText =findViewById(R.id.SuppPhoneNo);
    }
    private void insertBook(){
        String nameString=mProductNameEditText.getText().toString().trim();
        String priceString=mPriceEditText.getText().toString().trim();
        int price=Integer.parseInt(priceString);
        String quantityString=mQuantityEditText.getText().toString().trim();
        int quantity=Integer.parseInt(quantityString);
        String SuppNameString=mSuppNameEditText.getText().toString().trim();
        String SuppPhoneNoString=mSuppPhoneNoEditText.getText().toString().trim();

        BookDbHelper mDbHelper=new BookDbHelper(this);
        SQLiteDatabase db=mDbHelper.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME,nameString);
        values.put(BookEntry.COLUMN_PRICE,price);
        values.put(BookEntry.COLUMN_QUANTITY,quantity);
        values.put(BookEntry.COLUMN_SUPPLIER_NAME,SuppNameString);
        values.put(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER,SuppPhoneNoString);

        long newRowId=db.insert(BookEntry.TABLE_NAME,null,values);
        if (newRowId == -1) {
            Toast.makeText(this, "Error with saving Book", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Book saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }
    public void Save(View view){
        insertBook();
        finish();
        Intent i = new Intent(EditorActivity.this, MainActivity.class);
        startActivity(i);
    }
}

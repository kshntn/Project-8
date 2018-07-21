package com.example.android.bookinventory;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.bookinventory.data.BookContract.BookEntry;

public class EditorActivity extends AppCompatActivity {
    int price = 0;
    int quantity = 0;
    private EditText mProductNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mSuppNameEditText;
    private EditText mSuppPhoneNoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mProductNameEditText = findViewById(R.id.ProdName);
        mPriceEditText = findViewById(R.id.Price);
        mQuantityEditText = findViewById(R.id.Quantity);
        mSuppNameEditText = findViewById(R.id.SuppName);
        mSuppPhoneNoEditText = findViewById(R.id.SuppPhoneNo);
    }

    private int insertBook() {
        String nameString = mProductNameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(nameString))
            return 0;
        String priceString = mPriceEditText.getText().toString().trim();
        if (priceString.equals(""))
            return 0;
        price = Integer.parseInt(priceString);
        String quantityString = mQuantityEditText.getText().toString().trim();
        if (quantityString.equals(""))
            return 0;
        quantity = Integer.parseInt(quantityString);
        String SuppNameString = mSuppNameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(SuppNameString))
            return 0;
        String SuppPhoneNoString = mSuppPhoneNoEditText.getText().toString().trim();
        if (TextUtils.isEmpty(SuppPhoneNoString))
            return 0;

        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(BookEntry.COLUMN_PRICE, price);
        values.put(BookEntry.COLUMN_QUANTITY, quantity);
        values.put(BookEntry.COLUMN_SUPPLIER_NAME, SuppNameString);
        values.put(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, SuppPhoneNoString);

        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);

        if (newUri == null)
            Toast.makeText(this, "Error with saving Book", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Book Saved", Toast.LENGTH_SHORT).show();

        return 1;
    }

    public void Save(View view) {
        int flag = insertBook();
        if (flag == 0) {
            Toast.makeText(this, "Enter All Details", Toast.LENGTH_SHORT).show();
        } else {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }
}

package com.example.android.bookinventory;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.bookinventory.data.BookContract.BookEntry;
import com.example.android.bookinventory.data.BookDbHelper;

public class MainActivity extends AppCompatActivity {
    BookDbHelper mDbHelper = new BookDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayDatabaseInfo();

    }

    private void displayDatabaseInfo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(BookEntry.TABLE_NAME, null, null, null, null, null, null);
        try {
            TextView displayView = findViewById(R.id.BookViewId);
            displayView.append(BookEntry._ID + " - " + BookEntry.COLUMN_PRODUCT_NAME + " - " + BookEntry.COLUMN_PRICE + " - " + BookEntry.COLUMN_QUANTITY + " - " + BookEntry.COLUMN_SUPPLIER_NAME + " - " + BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER + "\n\n");
            int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
            int ProdNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
            int PriceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);
            int QuantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);
            int SupplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NAME);
            int SupplierPhNoColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            while (cursor.moveToNext()) {
                int CurrentID = cursor.getInt(idColumnIndex);
                String CurrentProdName = cursor.getString(ProdNameColumnIndex);
                int CurrentPrice = cursor.getInt(PriceColumnIndex);
                int CurrentQuantity = cursor.getInt(QuantityColumnIndex);
                String CurrentSupplierName = cursor.getString(SupplierNameColumnIndex);
                String CurrentSupplierPhNo = cursor.getString(SupplierPhNoColumnIndex);
                displayView.append("\n" + CurrentID + " - " + CurrentProdName + " - " + CurrentPrice + " - " + CurrentQuantity + " - " + CurrentSupplierName + " - " + CurrentSupplierPhNo);
            }

        } finally {
            cursor.close();
        }
    }

    public void Submit(View view) {
        Intent i = new Intent(MainActivity.this, EditorActivity.class);
        startActivity(i);
    }

}

package com.example.android.bookinventory.data;

import android.provider.BaseColumns;

public final class BookContract {
    private BookContract() {

    }

    public static final class BookEntry implements BaseColumns {
        public final static String TABLE_NAME = "Books";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PRODUCT_NAME = "ProductName";
        public final static String COLUMN_PRICE = "Price";
        public final static String COLUMN_QUANTITY = "Quantity";
        public final static String COLUMN_SUPPLIER_NAME = "SupplierName";
        public final static String COLUMN_SUPPLIER_PHONE_NUMBER = "SupplierPhoneNumber";
    }
}

package com.example.android.bookinventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class BookContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.bookinventory";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BOOKS = "Books";

    private BookContract() {

    }

    public static final class BookEntry implements BaseColumns {
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        public final static String TABLE_NAME = "Books";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PRODUCT_NAME = "ProductName";
        public final static String COLUMN_PRICE = "Price";
        public final static String COLUMN_QUANTITY = "Quantity";
        public final static String COLUMN_SUPPLIER_NAME = "SupplierName";
        public final static String COLUMN_SUPPLIER_PHONE_NUMBER = "SupplierPhoneNumber";
    }
}

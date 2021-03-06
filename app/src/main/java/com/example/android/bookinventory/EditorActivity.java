package com.example.android.bookinventory;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.bookinventory.data.BookContract.BookEntry;

import static android.text.TextUtils.isEmpty;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_BOOK_LOADER = 0;
    int price = 0;
    int quantity = 0;
    private EditText mProductNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mSuppNameEditText;
    private EditText mSuppPhoneNoEditText;
    private Button mOrderButton;
    private Uri mCurrentBookUri;
    private boolean mBookHasChanged = false;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        mOrderButton = findViewById(R.id.order);
        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();
        if (mCurrentBookUri == null) {
            setTitle(getString(R.string.AddBook));
            invalidateOptionsMenu();
            mOrderButton.setVisibility(View.GONE);
        } else {
            setTitle(getString(R.string.EditBook));
            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }
        mProductNameEditText = findViewById(R.id.ProdName);
        mPriceEditText = findViewById(R.id.Price);
        mQuantityEditText = findViewById(R.id.Quantity);
        mSuppNameEditText = findViewById(R.id.SuppName);
        mSuppPhoneNoEditText = findViewById(R.id.SuppPhoneNo);

        mProductNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSuppNameEditText.setOnTouchListener(mTouchListener);
        mSuppPhoneNoEditText.setOnTouchListener(mTouchListener);
    }

    private int insertBook() {
        String nameString = mProductNameEditText.getText().toString().trim();
        if (isEmpty(nameString))
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
        if (isEmpty(SuppNameString))
            return 0;
        String SuppPhoneNoString = mSuppPhoneNoEditText.getText().toString().trim();
        if (isEmpty(SuppPhoneNoString))
            return 0;
        if (price < 1)
            return 2;
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(BookEntry.COLUMN_PRICE, price);
        values.put(BookEntry.COLUMN_QUANTITY, quantity);
        values.put(BookEntry.COLUMN_SUPPLIER_NAME, SuppNameString);
        values.put(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, SuppPhoneNoString);
        if (mCurrentBookUri == null) {
            Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
            if (newUri == null)
                Toast.makeText(this, R.string.ErrorSavingBook, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, R.string.BookSaved, Toast.LENGTH_SHORT).show();
        } else {
            int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);
            if (rowsAffected == 0)
                Toast.makeText(this, R.string.BookUpdateFailed, Toast.LENGTH_SHORT).show();
            else Toast.makeText(this, R.string.BookUpdated, Toast.LENGTH_SHORT).show();
        }
        return 1;
    }

    public void Save() {
        int flag = insertBook();
        if (flag == 0) {
            Toast.makeText(this, R.string.EnterDetails, Toast.LENGTH_SHORT).show();
        } else if (flag == 2) {
            Toast.makeText(this, R.string.zeroPrice, Toast.LENGTH_SHORT).show();
        } else if (flag == 1) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void minus(View view) {
        String quantityString = mQuantityEditText.getText().toString().trim();
        quantity = Integer.parseInt(quantityString);
        if (quantity == 0) {
            Toast.makeText(this, R.string.NoNegative, Toast.LENGTH_SHORT).show();
        } else {
            quantity--;
            mQuantityEditText.setText(String.valueOf(quantity));
        }
    }

    public void plus(View view) {
        String quantityString = mQuantityEditText.getText().toString().trim();
        quantity = Integer.parseInt(quantityString);
        quantity++;
        mQuantityEditText.setText(String.valueOf(quantity));
    }

    public void order(View view) {
        String suppPhone = mSuppPhoneNoEditText.getText().toString().trim();
        if (!suppPhone.equals("")) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(getString(R.string.tel) + suppPhone));
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentBookUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete_entry);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                Save();
                return true;
            case R.id.action_delete_entry:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mBookHasChanged && !hasEntry()) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                showUnsavedChangesDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //    setting the up button
    private void showUnsavedChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.DiscardQuery);
        builder.setPositiveButton(R.string.Discard, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NavUtils.navigateUpFromSameTask(EditorActivity.this);
            }
        });
        builder.setNegativeButton(R.string.Editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!mBookHasChanged && !hasEntry()) {
            super.onBackPressed();
            return;
        }
        showUnsavedChangesDialog();
    }

    //    Check Empty lines
    public boolean hasEntry() {
        boolean hasInput = false;
        if (mCurrentBookUri == null) {
            String product = mProductNameEditText.getText().toString().trim();
            String price = mPriceEditText.getText().toString().trim();
            String quantity = mQuantityEditText.getText().toString().trim();
            String supplier = mSuppNameEditText.getText().toString().trim();
            String phone = mSuppPhoneNoEditText.getText().toString().trim();

            if (!isEmpty(product) || (!isEmpty(price) && (Integer.valueOf(price) > 0)) ||
                    (!isEmpty(quantity) && (Integer.valueOf(quantity) > 0)) ||
                    !isEmpty(supplier) || !isEmpty(phone)) {
                hasInput = true;
            }
        }
        return hasInput;
    }

    //    setting Delete funtion
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.DeleteBook);
        builder.setPositiveButton(R.string.Delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteBook() {
        if (mCurrentBookUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);
            if (rowsDeleted == 0)
                Toast.makeText(this, R.string.FailedDelete, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, R.string.BookDeleted, Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    //    Implementing CursorLoader
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_PRICE,
                BookEntry.COLUMN_QUANTITY,
                BookEntry.COLUMN_SUPPLIER_NAME,
                BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER};
        return new CursorLoader(this, mCurrentBookUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);
            int suppNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NAME);
            int suppPhoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String suppName = cursor.getString(suppNameColumnIndex);
            int suppPhone = cursor.getInt(suppPhoneColumnIndex);

            mProductNameEditText.setText(name);
            mPriceEditText.setText(Integer.toString(price));
            mQuantityEditText.setText(Integer.toString(quantity));
            mSuppNameEditText.setText(suppName);
            mSuppPhoneNoEditText.setText(Integer.toString(suppPhone));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}

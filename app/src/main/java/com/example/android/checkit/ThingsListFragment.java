package com.example.android.checkit;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.android.checkit.database.CheckItContract;
import com.example.android.checkit.database.CheckItContract.ThingEntry;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThingsListFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    // Views
//    @BindView(R.id.things_list_add_image_button)
//    ImageView mAddImageView;
//    @BindView(R.id.things_list_fragment_edit_text)
//    EditText mEditText;
    @BindView(R.id.things_list_fragment_list_view)
    ListView mListView;
    EditText mEditText;
    ImageView mAddImageView;

    // Member variables
    SimpleCursorAdapter mAdapter;

    public ThingsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_things_list, container, false);
        View header = inflater.inflate(R.layout.things_list_header, mListView, false);

        // Bind views
        ButterKnife.bind(this, rootView);
        mEditText = header.findViewById(R.id.things_list_fragment_edit_text);
        mAddImageView = header.findViewById(R.id.things_list_add_image_button);

        // Add header to listview
        mListView.addHeaderView(header);

        // Put an onClickListener on the AddImageView
        mAddImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveThingToList();
            }
        });

        // Initialize and set adapter
        mAdapter = new SimpleCursorAdapter(getContext(), R.layout.things_list_item, null,
                new String[]{ThingEntry.COLUMN_THINGS},
                new int[]{R.id.things_list_item_text_view});
        mListView.setAdapter(mAdapter);

        // Initialized loader
        getLoaderManager().initLoader(0, null, this);

        return rootView;
    }

    // Helper method for saving things to the list of things
    private void saveThingToList() {
        // Extract the name of the thing from the edit text
        String newThing = mEditText.getText().toString().trim();

        // Check if the user's input is valid
        if (TextUtils.isEmpty(newThing)) {
            Toast.makeText(getContext(), "You haven't entered anything yet!", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ThingEntry.COLUMN_THINGS, newThing);

        // Save to database
        Uri newUri = getContext().getContentResolver().insert(ThingEntry.CONTENT_URI, values);

        // Notify the user of the success or failure
        String toastMessage;
        if (newUri == null) {
            toastMessage = "Error saving new valuable: " + newThing;
        } else {
            toastMessage = "New valuable " + newThing + " saved successfully.";

            // Clear the Edit Text field
            mEditText.setText("");

            // Make the soft keyboard go away
            InputMethodManager inputMethodManager = (InputMethodManager) getContext()
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mListView.getWindowToken(), 0);
        }

        Toast.makeText(getContext(), toastMessage, Toast.LENGTH_SHORT).show();
    }


    @NonNull
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Timber.d("oncreateloader called");
        String[] projection = { ThingEntry._ID, ThingEntry.COLUMN_THINGS };

        return new android.support.v4.content.CursorLoader(getContext(), ThingEntry.CONTENT_URI, projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}

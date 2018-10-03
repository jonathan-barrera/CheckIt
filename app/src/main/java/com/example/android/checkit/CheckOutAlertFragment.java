package com.example.android.checkit;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.android.checkit.database.CheckItContract.ThingEntry;
import com.example.android.checkit.utils.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonathanbarrera on 10/2/18.
 */

public class CheckOutAlertFragment extends DialogFragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    // Empty Constructor
    public CheckOutAlertFragment(){}

    // IDs/Keys
    private final int LOADER_THINGS_ID = 0;
    private final String ACCOM_NAME_KEY = "accommodation-name-key";
    private final String CHECK_OUT_TIME_KEY = "checkout-time-key";

    // Views
    @BindView(R.id.check_out_alert_fragment_list_view)
    ListView mListView;
    @BindView(R.id.check_out_alert_fragment_text_view)
    TextView mTextView;
    @BindView(R.id.check_out_alert_dismiss_button)
    Button mDismissButton;

    // Member Variables
    private SimpleCursorAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view
        View rootView = inflater.inflate(R.layout.fragment_check_out_alert, container, false);

        // Bind views
        ButterKnife.bind(this, rootView);

        // Extract information about the check out
        Bundle bundle = getArguments();
        String accommodation = bundle.getString(ACCOM_NAME_KEY);
        String time = DateUtils.formatCheckOutTime(bundle.getString(CHECK_OUT_TIME_KEY));

        // Set text for the text view
        mTextView.setText("You're due to check out today from " + accommodation + " at " + time + ". " +
                "Don't forget to pack all your valuables!");

        // Initialize and set adapter
        mAdapter = new SimpleCursorAdapter(getContext(), R.layout.things_alert_list_item, null,
                new String[]{ThingEntry.COLUMN_THINGS},
                new int[]{R.id.things_alert_list_item_text_view});
        mListView.setAdapter(mAdapter);

        // Initialize loader
        getLoaderManager().initLoader(LOADER_THINGS_ID, null, this);

        // Set an OnClickListener on the Dismiss Button
        mDismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = { ThingEntry._ID, ThingEntry.COLUMN_THINGS };

        return new android.support.v4.content.CursorLoader(getContext(), ThingEntry.CONTENT_URI, projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}

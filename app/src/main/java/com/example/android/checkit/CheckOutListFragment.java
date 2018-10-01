package com.example.android.checkit;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.checkit.database.CheckItContract.CheckOutEntry;
import com.example.android.checkit.models.CheckOutEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckOutListFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>{

    // Member Variables
    List<CheckOutEvent> mCheckOutEvents;
    CheckOutAdapter mAdapter;

    // Views
    @BindView(R.id.check_out_list_recycler_view)
    RecyclerView mRecyclerView;

    public CheckOutListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.d("oncreateview called");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_check_out_list, container, false);

        // Bind Views
        ButterKnife.bind(this, rootView);

        // Initialize the list of checkoutevent objects
        mCheckOutEvents = new ArrayList<>();

        // Set a layoutmanager to the recyclerview
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the adapter and set to the recycler view
        mAdapter = new CheckOutAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // Prepare the loader
        getLoaderManager().initLoader(0, null, this);

        return rootView;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Get the data from the database
        String[] projection = {
                CheckOutEntry._ID,
                CheckOutEntry.COLUMN_CHECKOUT_DATE,
                CheckOutEntry.COLUMN_CHECKOUT_TIME,
                CheckOutEntry.COLUMN_CHECKOUT_ACCOM_NAME
        };

        return new android.support.v4.content.CursorLoader(getContext(), CheckOutEntry.CONTENT_URI, projection,
                null, null, null);
    }

    private void getInfo(Cursor data) {
        String accommodation = data.getString(data.getColumnIndex(
                CheckOutEntry.COLUMN_CHECKOUT_ACCOM_NAME));
        long date = data.getLong(data.getColumnIndex(
                CheckOutEntry.COLUMN_CHECKOUT_DATE));
        String time = data.getString(data.getColumnIndex(
                CheckOutEntry.COLUMN_CHECKOUT_TIME));
        long id = data.getLong(data.getColumnIndex(
                CheckOutEntry._ID));

        mCheckOutEvents.add(new CheckOutEvent(date, time, accommodation, id));
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        // First clear the list of data
        mCheckOutEvents.clear();

        if (data.moveToFirst()) {
            getInfo(data);
            while (data.moveToNext()) {
                getInfo(data);
            }

            mAdapter.setCheckOutData(mCheckOutEvents);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mCheckOutEvents.clear();
    }

    @Override
    public void onPause() {
        super.onPause();

        // Clear the data list
        mCheckOutEvents.clear();
    }
}

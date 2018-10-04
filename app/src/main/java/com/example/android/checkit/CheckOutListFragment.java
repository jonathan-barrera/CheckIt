package com.example.android.checkit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.checkit.adapters.CheckOutAdapter;
import com.example.android.checkit.database.CheckItContract.CheckOutEntry;
import com.example.android.checkit.models.CheckOutEvent;
import com.example.android.checkit.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.example.android.checkit.utils.DateUtils.isChosenDateAfterToday;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckOutListFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>{

    // Keys/IDs
    private final int LOADER_CHECK_OUT_LIST_ID = 0;
    private final String ACCOM_NAME_KEY = "accommodation-name-key";
    private final String CHECK_OUT_TIME_KEY = "checkout-time-key";
    private final String SHARED_PREFS_ISFIRSTRUN_KEY = "shared-prefs-isfirstrun-key";

    // Member Variables
    private List<CheckOutEvent> mCheckOutEvents;
    private CheckOutAdapter mAdapter;
    private CheckOutAlertFragment mCheckOutAlertFragment;
    private FragmentManager mFragmentManager;

    // Views
    @BindView(R.id.check_out_list_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.fab_button)
    FloatingActionButton mFabButton;

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

        // Show options menu
        setHasOptionsMenu(true);

        // Initialize the list of checkoutevent objects
        mCheckOutEvents = new ArrayList<>();

        // Initialize the CheckOutAlertFragment and FragmentManager
        mCheckOutAlertFragment = new CheckOutAlertFragment();
        mFragmentManager = getFragmentManager();

        // Set a layoutmanager to the recyclerview
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the adapter and set to the recycler view
        mAdapter = new CheckOutAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // Prepare the loader
        getLoaderManager().initLoader(LOADER_CHECK_OUT_LIST_ID, null, this);

        // Set onClickListener on the FAB
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditCheckOutActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_check_out_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO finish the function for settings options menu
        switch (item.getItemId()) {
            case R.id.action_delete_all_check_outs:
                getContext().getContentResolver().delete(CheckOutEntry.CONTENT_URI, null, null);
                getLoaderManager().restartLoader(LOADER_CHECK_OUT_LIST_ID, null, this);
                return true;
            case R.id.action_open_settings:
                // Open the settings activity
                startActivity(new Intent(getContext(), SettingsActivity.class));
                return true;
            default:
                throw new IllegalArgumentException("Invalid menu item selected: " + item);
        }
    }

    // Helper method for extracting all of the check out events from the database
    private void getInfo(Cursor data) {
        // Extract information from the cursor
        String accommodation = data.getString(data.getColumnIndex(
                CheckOutEntry.COLUMN_CHECKOUT_ACCOM_NAME));
        long date = data.getLong(data.getColumnIndex(
                CheckOutEntry.COLUMN_CHECKOUT_DATE));
        String time = data.getString(data.getColumnIndex(
                CheckOutEntry.COLUMN_CHECKOUT_TIME));
        long id = data.getLong(data.getColumnIndex(
                CheckOutEntry._ID));

        // For each checkout, check if it is coming up soon (so we can alert the user)
        // TODO change the rangehours and rangeminutes, last two variables
        if (DateUtils.isCheckOutComingUp(date, time, 2, 0)) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            // First check if this is the first run
            if (sharedPreferences.getBoolean(SHARED_PREFS_ISFIRSTRUN_KEY, true)) {
                showCheckOutDialog(accommodation, time);

                // If it is the first fun, now save the first run shared prefs as false
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SHARED_PREFS_ISFIRSTRUN_KEY, false);
                editor.apply();
            }
        }

        // Only show upcoming checkouts
        if (isChosenDateAfterToday(date)) {
            mCheckOutEvents.add(new CheckOutEvent(date, time, accommodation, id));
        }
    }

    // Helper method for showing the user a dialog alerting them that a checkout is coming up soon
    private void showCheckOutDialog(String accommodation, String time) {
        Bundle bundle = new Bundle();
        bundle.putString(ACCOM_NAME_KEY, accommodation);
        bundle.putString(CHECK_OUT_TIME_KEY, time);
        mCheckOutAlertFragment.setArguments(bundle);
        mCheckOutAlertFragment.show(mFragmentManager, "Things List");

        Toast.makeText(getContext(), "Check out coming up soon: " + accommodation, Toast.LENGTH_SHORT).show();
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

        mAdapter.notifyDataSetChanged();
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

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mCheckOutEvents.clear();
        //TODO fix crash that happens here upon navigating back from homepage
        mAdapter.setCheckOutData(null);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Clear the data list
        mCheckOutEvents.clear();
    }
}

package com.example.android.checkit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.checkit.adapters.CheckOutAdapter;
import com.example.android.checkit.database.CheckItContract.CheckOutEntry;
import com.example.android.checkit.models.CheckOutEvent;
import com.example.android.checkit.utils.DateUtils;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class EditCheckOutActivity extends AppCompatActivity {

    // Views
    @BindView(R.id.accommodation_edit_text)
    EditText mAccomEditText;
    @BindView(R.id.edit_check_out_date_text_view)
    TextView mDateTextView;
    @BindView(R.id.edit_check_out_time_text_view)
    TextView mTimeTextView;

    // Member variables
    private String mAccomName;
    private long mDate;
    private String mTime;
    private CheckOutEvent mCheckOutEvent;
    private boolean mIsUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_check_out);

        // Bind views
        ButterKnife.bind(this);

        // Get intent extra (if it exist)
        // If so, that is the current CheckOutEvent we are looking at
        if (getIntent().hasExtra(CheckOutAdapter.CHECKOUT_EXTRA_KEY)) {
            // Mark this Check Out as already existing and being updated
            mIsUpdate = true;

            // Extract the CheckOutEvent object and parse the info
            mCheckOutEvent = getIntent().getParcelableExtra(CheckOutAdapter.CHECKOUT_EXTRA_KEY);

            mAccomEditText.setText(mCheckOutEvent.getAccommodation());
            mDate = mCheckOutEvent.getDate();
            mDateTextView.setText(DateUtils.convertDateLongToString(mDate));
            mTime = mCheckOutEvent.getCheckOutTime();
            mTimeTextView.setText(DateUtils.formatCheckOutTime(mTime));
        }
    }

    // Helper method for saving a CheckOut object
    public void saveCheckOut(View view) {
        // Get Accommodation Name
        mAccomName = mAccomEditText.getText().toString().trim();

        // First check if the user's input is valid, if so go ahead with saving to database
        if (checkUserInput()) {

            // Store values in a contentvalues object
            ContentValues contentValues = new ContentValues();
            contentValues.put(CheckOutEntry.COLUMN_CHECKOUT_ACCOM_NAME, mAccomName);
            contentValues.put(CheckOutEntry.COLUMN_CHECKOUT_DATE, mDate);
            contentValues.put(CheckOutEntry.COLUMN_CHECKOUT_TIME, mTime);

            String toastMessage;

            if (mIsUpdate) {
                // This is updating an old Check Out
                Uri uri = ContentUris.withAppendedId(CheckOutEntry.CONTENT_URI, mCheckOutEvent.getId());
                Timber.d(String.valueOf(uri));
                int rowsChanged = getContentResolver().update(uri, contentValues,
                        null, null);

                if (rowsChanged == 0) {
                    toastMessage = "Error saving Check Out. (update)";
                } else {
                    toastMessage = "Check Out saved successfully.";
                }
            } else {
                // This is a new Check Out to be saved
                Uri newUri = getContentResolver().insert(CheckOutEntry.CONTENT_URI, contentValues);

                if (newUri == null) {
                    toastMessage = "Error saving Check Out.";
                } else {
                    toastMessage = "Check Out saved successfully.";
                }
            }

            // Toast message to notify user of failure or success
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();

            // Finish activity
            finish();
        }
    }

    // Helper method for checking if the user's input is valid
    private boolean checkUserInput() {
        // Check to see that the user input a name
        if (TextUtils.isEmpty(mAccomName)) {
            Toast.makeText(this, "Please enter a valid accommodation name.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check to see that the date is valid
        if (mDate == 0) {
            Toast.makeText(this, "Please enter a valid date.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check to see that the time is valid
        if (TextUtils.isEmpty(mTime)) {
            Toast.makeText(this, "Please enter a valid time.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Helper method for opening the Date Picker Dialog and selecting a date
    public void openDatePicker(View v) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // If a time has been previously chosen, set it as the default for the timepickerdialog
        // otherwise just use the current time.
        int year;
        int month;
        int day;
        if (mDate == 0) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        } else {
            String dateString = simpleDateFormat.format(mDate);
            String[] dateStringArray = dateString.split("/");
            day = Integer.parseInt(dateStringArray[0]);
            month = Integer.parseInt(dateStringArray[1]) - 1;
            year = Integer.parseInt(dateStringArray[2]);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Format the date like this: Jan 01, 2018
                DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
                String dateString = dateFormatSymbols.getMonths()[month] + " " + dayOfMonth + ", " + year;
                mDateTextView.setText(dateString);

                // Save the date in the database as a long
                try {
                    String dateStringForStorage = dayOfMonth + "/" + (month + 1) + "/" + year;
                    Date date = simpleDateFormat.parse(dateStringForStorage);

                    mDate = date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    // Helper method for opening the Time Picker Dialog and selecting a time
    public void openTimePicker(View v) {
        // If a time has been previously chosen, set it as the default for the timepickerdialog
        // otherwise just use the current time.
        int hourOfDay;
        int minute;
        if (TextUtils.isEmpty(mTime)) {
            Calendar c = Calendar.getInstance();
            hourOfDay = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        } else {
            String[] timeArray = mTime.split(":");
            hourOfDay = Integer.parseInt(timeArray[0]);
            minute = Integer.parseInt(timeArray[1]);
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Save the time as HH:MM (hour is on the scale of 0 to 23)
                mTime = hourOfDay + ":" + minute;

                // Format the time like this: 10:30 AM, 5:30 PM, etc.
                mTimeTextView.setText(DateUtils.formatCheckOutTime(mTime));
            }
        }, hourOfDay, minute, false);

        timePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_check_out, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_check_out:
                // Delete the check out line from the database
                Uri uri = ContentUris.withAppendedId(CheckOutEntry.CONTENT_URI, mCheckOutEvent.getId());
                getContentResolver().delete(uri, null, null);

                // Close the activity
                finish();
                return true;
            default:
                throw new IllegalArgumentException("Invalid menu item selected: " + item);
        }
    }
}

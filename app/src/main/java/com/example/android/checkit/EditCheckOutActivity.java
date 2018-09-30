package com.example.android.checkit;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditCheckOutActivity extends AppCompatActivity {

    // Views
    @BindView(R.id.accommodation_edit_text)
    EditText mAccomEditText;
    @BindView(R.id.edit_check_out_date_text_view)
    TextView mDateTextView;

    // Member variables
    private long mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_check_out);

        // Bind views
        ButterKnife.bind(this);
    }

    // Helper method for saving a CheckOut object
    public void saveCheckOut(View view) {
        Toast.makeText(this, "CheckOut saved.", Toast.LENGTH_SHORT).show();

        // Get Accommodation Name
        String accommodationName = mAccomEditText.getText().toString().trim();

        // Check to see that the user input a name
        if (TextUtils.isEmpty(accommodationName)) {
            Toast.makeText(this, "Please enter a valid accommodation name.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check to see that the date is valid
        if (mDate == 0) {
            Toast.makeText(this, "Please enter a valid date.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    // Helper method for opening the Date Picker Dialog and selecting a date
    public void openDatePicker(View v) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Format the date like this: Jan 01, 2018
                DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
                String dateString = dateFormatSymbols.getMonths()[month] + " " + day + ", " + year;
                mDateTextView.setText(dateString);

                // Save the date in the database as a long
                try {
                    String dateStringForStorage = day + "/" + (month + 1) + "/" + year;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = simpleDateFormat.parse(dateStringForStorage);

                    mDate = date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }
}

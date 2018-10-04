package com.example.android.checkit;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonathanbarrera on 10/3/18.
 */

public class RangePickerPreference extends DialogPreference {

    // Variables
    private String[] mDisplayedValues;

    // Views
    @BindView(R.id.hours_number_picker)
    NumberPicker mHoursNumberPicker;
    @BindView(R.id.minutes_number_picker)
    NumberPicker mMinutesNumberPicker;

    public RangePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.preference_range_picker);
    }

    @Override
    protected View onCreateDialogView() {
        View rootView = super.onCreateDialogView();

        // Bind Views
        ButterKnife.bind(this, rootView);

        // Set the min and max for both number pickers
        mHoursNumberPicker.setMinValue(0);
        mHoursNumberPicker.setMaxValue(24);
        mDisplayedValues = new String[4];
        for (int i = 0; i < 4; i++) {
            mDisplayedValues[i] = String.valueOf(i * 15);
        }
        mMinutesNumberPicker.setMinValue(0);
        mMinutesNumberPicker.setMaxValue(3);
        mMinutesNumberPicker.setDisplayedValues(mDisplayedValues);

        return rootView;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int hours = mHoursNumberPicker.getValue();
            String mMinutes = mDisplayedValues[mMinutesNumberPicker.getValue()];
            if (mMinutes.equals("0")) {
                mMinutes = "00";
            }

            persistString(hours + ":" + mMinutes);
        }
    }
}

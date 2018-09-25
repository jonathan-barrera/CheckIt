package com.example.android.checkit.models;

/**
 * Created by jonathanbarrera on 9/25/18.
 */

public class CheckOutEvent {

    // Variables to hold object information
    private long mDate;
    private String mCheckOutTime;
    private String mAccomodationName;

    public CheckOutEvent(){}

    // Constructor
    public CheckOutEvent(long date, String checkOutTime, String accomodationName) {
        mDate = date;
        mCheckOutTime = checkOutTime;
        mAccomodationName = accomodationName;
    }

    // Methods for retrieving information
    public long getDate() {
        return mDate;
    }

    public String getCheckoutTime() {
        return mCheckOutTime;
    }

    public String getAccomodation() {
        return mAccomodationName;
    }
}

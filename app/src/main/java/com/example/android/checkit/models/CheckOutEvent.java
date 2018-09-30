package com.example.android.checkit.models;

/**
 * Created by jonathanbarrera on 9/25/18.
 */

public class CheckOutEvent {

    // Variables to hold object information
    private long mDate;
    private int mCheckOutTime;
    private String mAccomodationName;

    public CheckOutEvent(){}

    // Constructor
    public CheckOutEvent(long date, int checkOutTime, String accomodationName) {
        mDate = date;
        mCheckOutTime = checkOutTime;
        mAccomodationName = accomodationName;
    }

    // Methods for retrieving information
    public long getDate() {
        return mDate;
    }

    public int getCheckoutTime() {
        return mCheckOutTime;
    }

    public String getAccomodation() {
        return mAccomodationName;
    }
}

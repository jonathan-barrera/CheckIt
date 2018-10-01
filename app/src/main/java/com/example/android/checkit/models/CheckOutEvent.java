package com.example.android.checkit.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jonathanbarrera on 9/25/18.
 */

public class CheckOutEvent implements Parcelable{

    // Variables to hold object information
    private long mDate;
    private String mCheckOutTime;
    private String mAccommodationName;
    private long mId;

    // Constructor
    public CheckOutEvent(long date, String checkOutTime, String accomodationName, long id) {
        mDate = date;
        mCheckOutTime = checkOutTime;
        mAccommodationName = accomodationName;
        mId = id;
    }

    public static final Creator<CheckOutEvent> CREATOR = new Creator<CheckOutEvent>() {
        @Override
        public CheckOutEvent createFromParcel(Parcel in) {
            return new CheckOutEvent(in);
        }

        @Override
        public CheckOutEvent[] newArray(int size) {
            return new CheckOutEvent[size];
        }
    };

    // Methods for retrieving information
    public long getDate() {
        return mDate;
    }

    public String getCheckOutTime() {return mCheckOutTime;}

    public String getAccommodation() {
        return mAccommodationName;
    }

    public long getId() { return mId; }

    // Methods for setting information
    public void setDate(long date) { mDate = date; }

    public void setCheckOutTime(String checkOutTime) { mCheckOutTime = checkOutTime; }

    public void setAccommodationName(String accommodationName) { mAccommodationName = accommodationName; }

    public void setId(long id) { mId = id; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mDate);
        dest.writeString(mCheckOutTime);
        dest.writeString(mAccommodationName);
        dest.writeLong(mId);
    }

    public CheckOutEvent(Parcel in) {
        mDate = in.readLong();
        mCheckOutTime = in.readString();
        mAccommodationName = in.readString();
        mId = in.readLong();
    }
}

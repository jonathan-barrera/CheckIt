package com.example.android.checkit.utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import timber.log.Timber;

/**
 * Created by jonathanbarrera on 9/30/18.
 */

public class DateUtils {

    // Helper method for converting from milliseconds to date string
    // ex. "January 01, 2018"
    public static String convertDateLongToString(long birthdate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(birthdate);
        String[] dateIntegerStrings = dateString.split("/");

        int day = Integer.parseInt(dateIntegerStrings[0]);
        int month = Integer.parseInt(dateIntegerStrings[1]) - 1;
        int year = Integer.parseInt(dateIntegerStrings[2]);

        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
        return dateFormatSymbols.getMonths()[month] + " " + day + ", " + year;
    }


    // Helper method to check if the chosen date is later than today's date
    public static boolean isChosenDateAfterToday(long chosenDate) {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Format the date of the check out
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String checkOutDate = simpleDateFormat.format(chosenDate);
        String[] checkOutDateArray = checkOutDate.split("/");

        Timber.d("Checkout date: " + checkOutDate + "; Current date: " + year + "/" + month + "/" + dayOfMonth);

        // Check if the check out date is today or later
        if (Integer.valueOf(checkOutDateArray[0]) > year) {
            return true;
        } else if (Integer.valueOf(checkOutDateArray[0]) == year &&
                Integer.valueOf(checkOutDateArray[1]) > month) {
            return true;
        } else if (Integer.valueOf(checkOutDateArray[0]) == year &&
                Integer.valueOf(checkOutDateArray[1]) == month &&
                Integer.valueOf(checkOutDateArray[2]) >= dayOfMonth) {
            return true;
        } else {
            return false;
        }
    }

    // Helper method to check if the current time is within a certain range of a designated time
    public static boolean isCheckOutComingUp(long checkOutDate, String checkOutTime,
                                             int rangeHours, int rangeMinutes) {
        String[] timeArray = checkOutTime.split(":");
        int checkOutHour = Integer.parseInt(timeArray[0]);
        int checkOutMinute = Integer.parseInt(timeArray[1]);

        // Get the exact checkoutdatetime as a long (milliseconds)
        long checkOutDateTime = checkOutDate + checkOutHour*60*60*1000 + checkOutMinute*60*1000;
        long alarmTime = checkOutDateTime - rangeHours*60*60*1000 - rangeMinutes*60*1000;

        // Add a little bit of extra time after checkout (30 minutes)
        checkOutDateTime = checkOutDateTime + 30*60*1000;

        // Get the current time
        long currentTime = Calendar.getInstance().getTimeInMillis();

        return currentTime >= alarmTime && currentTime <= checkOutDateTime;
    }

    // Helper method to format the check out time
    public static String formatCheckOutTime(String time) {
        // Format the time like this: 10:30 AM, 5:30 PM, etc.
        String[] timeArray = time.split(":");
        int hourOfDay = Integer.parseInt(timeArray[0]);
        int minute = Integer.parseInt(timeArray[1]);

        String minuteString;
        if (minute < 10) {
            minuteString = "0" + minute;
        } else {
            minuteString = String.valueOf(minute);
        }

        String timeString;
        if (hourOfDay >= 1 && hourOfDay <= 11) {
            timeString = hourOfDay + ":" + minuteString + " AM";
        } else if (hourOfDay >= 13 && hourOfDay <= 23) {
            timeString = (hourOfDay - 12) + ":" + minuteString + " PM";
        } else if (hourOfDay == 12) {
            timeString = hourOfDay + ":" + minuteString + " PM";
        } else {
            timeString = "12:" + minuteString + " AM";
        }

        return timeString;
    }
}

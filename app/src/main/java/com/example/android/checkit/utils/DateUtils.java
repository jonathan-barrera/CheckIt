package com.example.android.checkit.utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;

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
}

package ru.anddever.currencyviewer.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    /**
     * Method for converting given timestamp to more readable format
     *
     * @param rawDate - timestamp from response
     * @return string representation of converted date or empty string
     */
    public static String dateConverter(String rawDate) {
        DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm",
                Locale.getDefault());
        DateFormat srcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX",
                Locale.getDefault());
        Date date;
        try {
            date = srcFormat.parse(rawDate);
        } catch (ParseException e) {
            Log.e("MainActivity", "dateConverter: ", e);
            return "";
        }
        if (date != null) {
            return targetFormat.format(date);
        } else {
            return "";
        }
    }
}
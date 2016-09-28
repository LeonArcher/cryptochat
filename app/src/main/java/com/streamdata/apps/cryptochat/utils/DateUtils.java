package com.streamdata.apps.cryptochat.utils;

import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateUtils {

    private DateUtils(){}

    public static Date stringToDate(String strDate) {

        Date date = new Date();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault());

            date = dateFormat.parse(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());

        return dateFormat.format(date);
    }

}



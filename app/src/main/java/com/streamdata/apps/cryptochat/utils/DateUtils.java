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
            SimpleDateFormat format = new SimpleDateFormat("M d yyyy", Locale.ENGLISH);
            date = format.parse(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}



package com.streamdata.apps.cryptochat.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateUtils {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
            Locale.getDefault());

    private DateUtils() {}

    public static Date stringToDate(String strDate) throws ParseException {

        return dateFormat.parse(strDate);
    }

    public static String dateToString(Date date) {
        return dateFormat.format(date);
    }
}

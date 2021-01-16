package com.shahs.tradeload.tradeload.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MiscUtils {

    public static int getIterableSize(Iterable iterableData ) {
        int counter = 0;
        for (Object i: iterableData) {
            counter++;
        }
        return counter;
    }

    public static java.util.Date stringToDate(String date, String format) {    // format example "MM/dd/yyyy"

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        java.sql.Date sqlDate = null;
        try {
            java.util.Date utilDate = sdf.parse(date);
            sqlDate = new java.sql.Date(utilDate.getTime());

        } catch(ParseException e) {
            e.printStackTrace();
        }
        return sqlDate;
    }
    public static String dateToString(Date date, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String stringDate = "";
        try {
            stringDate = sdf.format(date);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return stringDate;
    }
}

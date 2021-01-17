package com.shahs.tradeload.tradeload.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
        DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
        DateTime dt = formatter.parseDateTime(date);
        return dt.toDate();
    }
    public static String dateToString(Date date, String format) {
        DateTime dt = new DateTime(date);
        DateTimeFormatter fmt = DateTimeFormat.forPattern(format);
        return fmt.print(dt);
    }
}

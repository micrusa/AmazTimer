package me.micrusa.amaztimer.TCX;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TCXUtils {
    public static String formatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.TIME_FORMAT);
        dateFormat.setTimeZone(TimeZone.getDefault());
        timeFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(date)
                + Constants.CHAR_DATETIME
                + timeFormat.format(date)
                + Constants.CHAR_AFTERTIME;
    }
}

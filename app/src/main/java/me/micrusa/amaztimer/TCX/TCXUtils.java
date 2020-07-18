package me.micrusa.amaztimer.TCX;

import com.pixplicity.easyprefs.library.Prefs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import me.micrusa.amaztimer.defValues;

public class TCXUtils {
    public static String formatDate(Date date){
        //Workaround for wrong times
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hours = calendar.get(Calendar.HOUR) + Integer.parseInt(Prefs.getString(defValues.KEY_TCX_TIME, "0"));
        calendar.set(Calendar.HOUR, hours);
        date = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
        SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.TIME_FORMAT, Locale.US);
        dateFormat.setTimeZone(TimeZone.getDefault());
        timeFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(date)
                + Constants.CHAR_DATETIME
                + timeFormat.format(date)
                + Constants.CHAR_AFTERTIME;
    }
}

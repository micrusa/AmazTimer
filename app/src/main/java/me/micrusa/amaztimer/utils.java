package me.micrusa.amaztimer;

import android.content.Context;
import android.os.Vibrator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class utils {

    public void vibrate(int q, Context Context) {
        Vibrator v = (Vibrator) Context.getSystemService(android.content.Context.VIBRATOR_SERVICE);
        v.vibrate(q);
    }

    public String sToMinS(int seconds){
        int millis = seconds * 1000;
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("mm:ss", Locale.getDefault());
        return df.format(new Date(millis));
    }

}

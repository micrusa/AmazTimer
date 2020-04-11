package me.micrusa.amaztimer;

import android.content.Context;
import android.os.Vibrator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class utils {

    void vibrate(int time, Context Context) {
        Vibrator v = (Vibrator) Context.getSystemService(android.content.Context.VIBRATOR_SERVICE);
        v.vibrate(time);
    }

    String sToMinS(int seconds) {
        SimpleDateFormat df = new SimpleDateFormat("mm:ss", Locale.getDefault());
        return df.format(new Date(seconds * 1000));
    }

}

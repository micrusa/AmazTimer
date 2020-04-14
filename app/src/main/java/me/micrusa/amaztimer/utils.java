package me.micrusa.amaztimer;

import android.content.Context;
import android.os.Vibrator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class utils {

    private defValues defValues = new defValues();

    void vibrate(int time, Context Context) {
        Vibrator v = (Vibrator) Context.getSystemService(android.content.Context.VIBRATOR_SERVICE);
        v.vibrate(time);
    }

    String sToMinS(int seconds) {
        //Format seconds to mm:ss
        SimpleDateFormat df = new SimpleDateFormat(defValues.timeFormat, Locale.getDefault());
        return df.format(new Date(seconds * 1000));
    }

}

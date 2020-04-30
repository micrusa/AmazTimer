package me.micrusa.amaztimer.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Vibrator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.micrusa.amaztimer.defValues;

public class utils {

    private me.micrusa.amaztimer.defValues defValues = new defValues();

    public void vibrate(int time, Context Context) {
        //Get vibrator service and vibrate
        Vibrator v = (Vibrator) Context.getSystemService(android.content.Context.VIBRATOR_SERVICE);
        v.vibrate(time);
    }

    public String formatTime(int seconds) {
        //Format seconds to mm:ss
        SimpleDateFormat df = new SimpleDateFormat(defValues.timeFormat, Locale.getDefault());
        return df.format(new Date(seconds * 1000));
    }

    public void setLang(Context context, String lang) {
        Locale locale = new Locale(lang);
        Configuration config = new Configuration(context.getResources().getConfiguration());
        Locale.setDefault(locale);
        config.setLocale(locale);
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }

    public void pushToFile(file f, int sets, int work, int rest) {
        //Save all values to the given file
        f.set(defValues.sSets, sets);
        f.set(defValues.sWork, work);
        f.set(defValues.sRest, rest);
    }

}

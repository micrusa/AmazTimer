package me.micrusa.amaztimer.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Vibrator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.defValues;

public class utils {


    public static void vibrate(int time, Context context) {
        vibrate(time, context, false);
    }

    public static void vibrate(int time, Context context, boolean sound){
        if(sound && (SystemProperties.isVerge())){
            MediaPlayer.create(context, R.raw.beep).start();
        }
        Vibrator v = (Vibrator) context.getSystemService(android.content.Context.VIBRATOR_SERVICE);
        if (v != null) {
            v.vibrate(time);
        }
    }

    public static String formatTime(int seconds) {
        //Format seconds to mm:ss
        SimpleDateFormat df = new SimpleDateFormat(defValues.timeFormat, Locale.getDefault());
        return df.format(new Date(seconds * 1000));
    }

    public static void setLang(Context context, String lang) {
        Locale locale = new Locale(lang);
        Configuration config = new Configuration(context.getResources().getConfiguration());
        Locale.setDefault(locale);
        config.setLocale(locale);
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }

    public static void pushToFile(file f, int sets, int work, int rest) {
        //Save all values to the given file
        f.set(defValues.SETTINGS_SETS, sets);
        f.set(defValues.SETTINGS_WORK, work);
        f.set(defValues.SETTINGS_REST, rest);
    }

    public static int getUpdatedTime(int currentTime, int update, Context paramContext){
        int result = currentTime + update;
        if(result < defValues.MIN_TIME){
            result = defValues.MIN_TIME;
            vibrate(defValues.SHORT_VIBRATION, paramContext);
        } else if(result > defValues.MAX_TIME){
            result = defValues.MAX_TIME;
            vibrate(defValues.SHORT_VIBRATION, paramContext);
        }
        return result;
    }

    public static int getUpdatedSets(int currentSets, int update, Context paramContext){
        int result = currentSets + update;
        if(result < defValues.MIN_SETS){
            result = defValues.MIN_SETS;
            vibrate(defValues.SHORT_VIBRATION, paramContext);
        } else if(result > defValues.MAX_SETS){
            result = defValues.MAX_SETS;
            vibrate(defValues.SHORT_VIBRATION, paramContext);
        }
        return result;
    }

}

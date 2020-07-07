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
        if(sound && SystemProperties.isVerge()
                && new file(defValues.SETTINGS_FILE).get(defValues.SETTINGS_SOUND, defValues.DEFAULT_SOUND))
            MediaPlayer.create(context, R.raw.beep).start();
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
        return getUpdatedValue(currentTime + update, defValues.MIN_TIME, defValues.MAX_TIME, paramContext);
    }

    public static int getUpdatedSets(int currentSets, int update, Context paramContext){
        return getUpdatedValue(currentSets + update, defValues.MIN_SETS, defValues.MAX_SETS, paramContext);
    }

    private static int getUpdatedValue(int result, int min, int max, Context paramContext){
        if(result < min){
            vibrate(defValues.SHORT_VIBRATION, paramContext);
            return min;
        } else if(result > max){
            vibrate(defValues.SHORT_VIBRATION, paramContext);
            return max;
        }
        return result;
    }

    public static int getMode(){
        file settings = new file(defValues.SETTINGS_FILE);
        if(settings.get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE))
            return 1;
        if(settings.get(defValues.SETTINGS_WORKOUTMODE, defValues.DEFAULT_WORKOUTMODE))
            return 2;
        else return 0;
    }

}

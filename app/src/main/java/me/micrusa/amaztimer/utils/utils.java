package me.micrusa.amaztimer.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Vibrator;

import com.pixplicity.easyprefs.library.Prefs;

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
                && Prefs.getBoolean(defValues.KEY_SOUND, defValues.DEFAULT_SOUND))
            MediaPlayer.create(context, R.raw.beep).start();
        Vibrator v = (Vibrator) context.getSystemService(android.content.Context.VIBRATOR_SERVICE);
        if (v != null) {
            v.vibrate(time);
        }
    }

    public static String formatTime(int seconds) {
        //Format seconds to mm:ss
        SimpleDateFormat df = new SimpleDateFormat(defValues.timeFormat, Locale.US);
        return df.format(new Date(seconds * 1000));
    }

    public static void setupPrefs(Context context){
        new Prefs.Builder()
                .setContext(context)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(context.getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

    public static void setLang(Context context, String lang) {
        Locale locale = new Locale(lang);
        Configuration config = new Configuration(context.getResources().getConfiguration());
        Locale.setDefault(locale);
        config.setLocale(locale);
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
        //Also configure easyprefs so that I don't have to add it everywhere
        setupPrefs(context);
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
        if(Prefs.getBoolean(defValues.KEY_REPSMODE, false))
            return 1;
        if(Prefs.getBoolean(defValues.KEY_WORKOUT, false))
            return 2;
        else return 0;
    }

    public static boolean isModeManualSets(){
        return getMode() == 2;
    }

    public static String hrZonePercentage(int hr){
        return hrZonePercentageInt(hr) + "%";
    }

    public static int hrZonePercentageInt(int hr){
        if(hr == 0 | prefUtils.getAge() == 0){
            return 0;
        }
        //noinspection UnnecessaryCallToStringValueOf
        return hr * 100 / (220 - prefUtils.getAge());
    }

}

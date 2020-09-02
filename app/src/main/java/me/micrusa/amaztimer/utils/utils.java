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
import me.micrusa.amaztimer.Constants;

public class utils {


    public static void vibrate(int time, Context context) {
        vibrate(time, context, false);
    }

    public static void vibrate(int time, Context context, boolean sound){
        if(sound && SystemProperties.isVerge()
                && Prefs.getBoolean(Constants.KEY_SOUND, Constants.DEFAULT_SOUND))
            MediaPlayer.create(context, R.raw.beep).start();
        Vibrator v = (Vibrator) context.getSystemService(android.content.Context.VIBRATOR_SERVICE);
        if (v != null) {
            v.vibrate(prefUtils.getVibration(time));
        }
    }

    public static String formatTime(int seconds) {
        //Format seconds to mm:ss
        SimpleDateFormat df = new SimpleDateFormat(Constants.timeFormat, Locale.US);
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

    public static void setupLang(Context context) {
        Locale locale = new Locale(Prefs.getString(Constants.KEY_LANG, "en"));
        Configuration config = new Configuration(context.getResources().getConfiguration());
        Locale.setDefault(locale);
        config.setLocale(locale);
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }

    public static int getUpdatedTime(int currentTime, int update, Context paramContext){
        return getUpdatedValue(currentTime + update, Constants.MIN_TIME, Constants.MAX_TIME, paramContext);
    }

    public static int getUpdatedSets(int currentSets, int update, Context paramContext){
        return getUpdatedValue(currentSets + update, Constants.MIN_SETS, Constants.MAX_SETS, paramContext);
    }

    private static int getUpdatedValue(int result, int min, int max, Context paramContext){
        if(result < min){
            vibrate(Constants.SHORT_VIBRATION, paramContext);
            return min;
        } else if(result > max){
            vibrate(Constants.SHORT_VIBRATION, paramContext);
            return max;
        }
        return result;
    }

    public static int getMode(){
        if(Prefs.getBoolean(Constants.KEY_REPSMODE, false))
            return 1;
        if(Prefs.getBoolean(Constants.KEY_WORKOUT, false))
            return 2;
        else return 0;
    }

    public static boolean isModeManualSets(){
        return getMode() == 2;
    }

    public static int[] getValues(int[] data, boolean longClick, Context context){
        int sets = data[0]; //data -> 0 = sets, 1 = work, 2 = rest, 3 = res
        int workTime = data[1];
        int restTime = data[2];
        switch(data[3]){
            case R.id.plus:
                sets = utils.getUpdatedSets(sets, longClick ? 5 : 1, context);
                break;
            case R.id.plus2:
                workTime = utils.getUpdatedTime(workTime, longClick ? 60 : 1, context);
                break;
            case R.id.plus3:
                restTime = utils.getUpdatedTime(restTime, longClick ? 60 : 1, context);
                break;
            case R.id.minus2:
                sets = utils.getUpdatedSets(sets, longClick ? -5 : -1, context);
                break;
            case R.id.minus:
                workTime = utils.getUpdatedTime(workTime, longClick ? -60 : -1, context);
                break;
            case R.id.minus3:
                restTime = utils.getUpdatedTime(restTime, longClick ? -60 : -1, context);
                break;
            default:
                break;
        }
        return new int[]{sets, workTime, restTime};
    }
}

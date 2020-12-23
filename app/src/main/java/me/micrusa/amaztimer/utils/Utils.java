/*
 * MIT License
 *
 * Copyright (c) 2020 Miguel Cruces
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.micrusa.amaztimer.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.pixplicity.easyprefs.library.Prefs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.activities.TimerActivity;
import me.micrusa.amaztimer.activities.PrepareActivity;
import me.micrusa.amaztimer.activities.saved.SavedTimerRun;
import me.micrusa.amaztimer.utils.devices.AmazfitUtils;

public class Utils {


    public static void vibrate(int time, Context context) {
        vibrate(time, context, false);
    }

    public static void vibrate(int time, Context context, boolean sound){
        if(sound && soundEnabled(context)) {
            MediaPlayer player = MediaPlayer.create(context, R.raw.beep);
            player.start();
            player.setOnCompletionListener(mediaPlayer -> {
                mediaPlayer.reset();
                mediaPlayer.release();
            });
        }
        Vibrator v = (Vibrator) context.getSystemService(android.content.Context.VIBRATOR_SERVICE);
        if (v != null) {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
                v.vibrate(PrefsUtil.getVibration(time));
            else
                v.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    private static boolean soundEnabled(Context context){
        return Prefs.getBoolean(Constants.KEY_SOUND, Constants.DEFAULT_SOUND)
                && (AmazfitUtils.isVerge() || hasSoundOutput(context));
    }

    private static boolean hasSoundOutput(Context context) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()
                && mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED)
                || (audioManager != null && (audioManager.isBluetoothA2dpOn() || audioManager.isBluetoothScoOn() || audioManager.isSpeakerphoneOn()))
                || context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT);
    }

    public static String formatTime(int seconds) {
        //Format seconds to mm:ss
        SimpleDateFormat df = new SimpleDateFormat(Constants.timeFormat, Locale.US);
        return df.format(new Date(seconds * 1000));
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
        if(Prefs.getBoolean(Constants.KEY_WORKOUT, false)
                || Prefs.getBoolean(Constants.KEY_REPSCOUNT, false))
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
        if (data[3] == R.id.plus1) {
            sets = Utils.getUpdatedSets(sets, longClick ? 5 : 1, context);
        } else if (data[3] == R.id.plus2) {
            workTime = Utils.getUpdatedTime(workTime, longClick ? 60 : 1, context);
        } else if (data[3] == R.id.plus3) {
            restTime = Utils.getUpdatedTime(restTime, longClick ? 60 : 1, context);
        } else if (data[3] == R.id.minus1) {
            sets = Utils.getUpdatedSets(sets, longClick ? -5 : -1, context);
        } else if (data[3] == R.id.minus2) {
            workTime = Utils.getUpdatedTime(workTime, longClick ? -60 : -1, context);
        } else if (data[3] == R.id.minus3) {
            restTime = Utils.getUpdatedTime(restTime, longClick ? -60 : -1, context);
        }
        return new int[]{sets, workTime, restTime};
    }

    public static void startTimer(Context context, boolean prepareDone){
        context.startActivity(new Intent(context,
                Prefs.getBoolean(Constants.KEY_ENABLEPREPARE, false)
                        && !prepareDone
                        ? PrepareActivity.class : TimerActivity.class));
    }

    public static void start(Context context, SavedTimerRun t){
        Prefs.putInt(Constants.KEY_SETS, t.sets);
        Prefs.putInt(Constants.KEY_WORK, t.work);
        Prefs.putInt(Constants.KEY_REST, t.rest);
        Prefs.putBoolean(Constants.KEY_HRTOGGLE, t.heartrate);
        context.startActivity(new Intent(context, Prefs.getBoolean(Constants.KEY_ENABLEPREPARE, false) ? PrepareActivity.class : TimerActivity.class));
    }
}

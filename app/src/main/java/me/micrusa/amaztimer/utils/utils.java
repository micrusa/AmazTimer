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
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Vibrator;

import com.pixplicity.easyprefs.library.Prefs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.TimerActivity;
import me.micrusa.amaztimer.activities.PrepareActivity;

public class utils {


    public static void vibrate(int time, Context context) {
        vibrate(time, context, false);
    }

    public static void vibrate(int time, Context context, boolean sound){
        if(sound && soundEnabled())
            MediaPlayer.create(context, R.raw.beep).start();
        Vibrator v = (Vibrator) context.getSystemService(android.content.Context.VIBRATOR_SERVICE);
        if (v != null) {
            v.vibrate(prefUtils.getVibration(time));
        }
    }

    private static boolean soundEnabled(){
        return Prefs.getBoolean(Constants.KEY_SOUND, Constants.DEFAULT_SOUND)
                && (SystemProperties.isVerge() || isBluetoothHeadsetConnected());
    }

    private static boolean isBluetoothHeadsetConnected() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()
                && mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED;
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

    public static void startTimer(Context context, boolean prepareDone){
        context.startActivity(new Intent(context,
                Prefs.getBoolean(Constants.KEY_ENABLEPREPARE, false)
                        && !prepareDone
                        ? PrepareActivity.class : TimerActivity.class));
    }
}

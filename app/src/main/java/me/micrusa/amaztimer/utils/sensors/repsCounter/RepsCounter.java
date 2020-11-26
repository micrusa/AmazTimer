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

package me.micrusa.amaztimer.utils.sensors.repsCounter;

import android.app.Dialog;
import android.content.Context;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.HashMap;

import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.utils.sensors.repsCounter.listeners.Accelerometer;
import me.micrusa.amaztimer.utils.sensors.repsCounter.objects.Exercise;
import me.micrusa.amaztimer.utils.sensors.repsCounter.ui.dialog.NewRepExerciseDialog;
import me.micrusa.amaztimer.utils.sensors.repsCounter.utils.Filtering;
import me.micrusa.amaztimer.utils.sensors.repsCounter.utils.PeaksChecker;

public class RepsCounter {

    public static Exercise CURRENT_EXERCISE = RepsConstants.EXERCISES[RepsConstants.EXERCISES.length - 1];

    private static ArrayList<RepsListener> listeners = new ArrayList<>();
    private static Accelerometer accelerometer = new Accelerometer();

    private static long lastTimeCheckedPeaks = 0;
    private static int currentPeaks = 0;
    private static boolean isCounting = true;

    private static ArrayList<Double> allAccelValues = new ArrayList<>();
    private static ArrayList<Float> currentBatchAccelValues = new ArrayList<>();

    //Sensor outputs
    public static void newAccelValues(float accelX, float accelY, float accelZ){
        if(!isCounting) return;
        float accel = accelX;

        if(CURRENT_EXERCISE.AXIS == 'X') accel = accelX;
        else if(CURRENT_EXERCISE.AXIS == 'Y') accel = accelY;
        else if(CURRENT_EXERCISE.AXIS == 'Z') accel = accelZ;

        if(accel == 0) return; //Ignore filtered values

        currentBatchAccelValues.add(accel);

        if(lastTimeCheckedPeaks == 0) //Make it wait 4s until checking peaks for the first time
            lastTimeCheckedPeaks = System.currentTimeMillis() + 3000;

        if(System.currentTimeMillis() - lastTimeCheckedPeaks >= RepsConstants.PEAK_CHECKING_INTERVAL)
            checkPeaks();
    }

    public static void newGyroValues(float gyroX, float gyroZ){} //For a possible future usage

    //Peaks
    private static void checkPeaks(){
        lastTimeCheckedPeaks = System.currentTimeMillis();

        double[] newValues = Filtering.filterSignal(currentBatchAccelValues, 80000, 30000, 2, 0, CURRENT_EXERCISE.CHEBYSHEV_FILTER_RIPPLE_PERCENT);
        currentBatchAccelValues = new ArrayList<>();

        for(double value : newValues)
            allAccelValues.add(value);

        double[] array = new double[allAccelValues.size()];
        for(int i = 0; i < allAccelValues.size(); i++)
            array[i] = allAccelValues.get(i);

        updatePeaks(PeaksChecker.get(array));
    }

    private static void updatePeaks(HashMap<Double, Integer> peaks){
        currentPeaks = peaks.size();
        for(RepsListener listener : listeners)
            listener.onNewRepsValue(currentPeaks);
    }

    public static int getReps(){
        return currentPeaks;
    }

    //UI controlled stuff
    public static void setExercise(Exercise ex){
        CURRENT_EXERCISE = ex;
    }

    public static void newSet(boolean count){
        isCounting = count;
        resetData();
    }

    public static void addRepsListener(RepsListener listener){
        listeners.add(listener);
    }

    public static void startCounting(Context context){
        resetData();
        accelerometer.register(context);
    }

    public static void stopCounting(Context context){
        accelerometer.unregister(context);
        listeners.clear();
    }

    private static void resetData(){
        currentBatchAccelValues = new ArrayList<>();
        allAccelValues = new ArrayList<>();
        currentPeaks = 0;
        lastTimeCheckedPeaks = 0;
    }

    public static void startIfEnabled(RepsListener listener, Context context){
        if(Prefs.getBoolean(Constants.KEY_REPSCOUNT, false)){
            RepsCounter.addRepsListener(listener);
            RepsCounter.startCounting(context);
        }
    }
}

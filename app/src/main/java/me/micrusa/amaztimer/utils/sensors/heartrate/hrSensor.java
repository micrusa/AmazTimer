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

package me.micrusa.amaztimer.utils.sensors.heartrate;


import android.content.Context;
import android.hardware.Sensor;
import android.os.Handler;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.Date;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.saveworkout.SaveWorkout;
import me.micrusa.amaztimer.utils.tcx.TCXConstants;
import me.micrusa.amaztimer.utils.tcx.SaveTCX;
import me.micrusa.amaztimer.utils.tcx.TCXUtils;
import me.micrusa.amaztimer.utils.tcx.data.Lap;
import me.micrusa.amaztimer.utils.tcx.data.TCXData;
import me.micrusa.amaztimer.utils.tcx.data.Trackpoint;
import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.utils.sensors.objects.Listener;
import me.micrusa.amaztimer.utils.sensors.heartrate.listeners.experimentalListener;
import me.micrusa.amaztimer.utils.sensors.heartrate.listeners.mainListener;

@SuppressWarnings("CanBeFinal")
public class hrSensor {
    private hrListener listener;
    private long startTime;
    private String latestTrackpointTime;
    private boolean prevListening = false;
    public int latestHr = 0;

    private static hrSensor hrSensor;
    private Listener hrListener;

    //All tcx needed stuff
    private String currentLapStatus = TCXConstants.STATUS_RESTING;
    private Lap currentLap;
    private TCXData TCXData;

    public static hrSensor getInstance(){
        return hrSensor;
    }

    public static hrSensor initialize(hrListener listener){
        hrSensor = new hrSensor(listener);
        return hrSensor;
    }

    private hrSensor(hrListener listener) {
        //Setup sensor manager, sensor and textview
        this.TCXData = new TCXData();
        this.listener = listener;
    }

    public void setListener(hrListener listener){
        this.listener = listener;
    }

    public void newValue(int i){
        if(prevListening) return; //Don't use value if activity haven't started
        if(latestHr != i){
            listener.onHrChanged(i);
            latestHr = i;
        }
        SaveWorkout.addHrValue(i);
        //Set latest hr value
        String currentDate = TCXUtils.formatDate(new Date());
        //Create Trackpoint and add it to current Lap
        if (!currentDate.equals(this.latestTrackpointTime) && Prefs.getBoolean(Constants.KEY_TCX, Constants.DEFAULT_TCX)) { //This will limit trackpoints to 1/s
            currentLap.addTrackpoint(new Trackpoint(i, new Date()));
            this.latestTrackpointTime = currentDate;
        }
    }

    public void onAccuracyChanged(Sensor param1Sensor, int param1Int) {}

    public void onMainActCreate(Context context){
        if(Prefs.getBoolean(Constants.KEY_HRTOGGLE, true) && !prevListening
                && Prefs.getBoolean(Constants.KEY_HRONSTART, true)){
            prevListening = true;
            if(Prefs.getBoolean(Constants.KEY_HREXPERIMENT, false))
                hrListener = new experimentalListener();
            else
                hrListener = new mainListener();
            hrListener.register(context);
        }
    }

    public void onMainActDestroy(Context context){
        if(Prefs.getBoolean(Constants.KEY_HRTOGGLE, true)
                && Prefs.getBoolean(Constants.KEY_HRONSTART, true))
            hrListener.unregister(context);
    }

    public void registerListener(Context context) {
        SaveWorkout.startWorkout();
        this.startTime = System.currentTimeMillis();
        //Register listener taking into account experimental sensor
        if(!prevListening) {
            if (Prefs.getBoolean(Constants.KEY_HREXPERIMENT, false))
                hrListener = new experimentalListener();
            else
                hrListener = new mainListener();
            hrListener.register(context);
        } else prevListening = false;
    }

    public void unregisterListener(Context context) {
        hrListener.unregister(context);
        SaveWorkout.endWorkout();

        if (Prefs.getBoolean(Constants.KEY_TCX, Constants.DEFAULT_TCX)) {
            new Handler().postDelayed(() -> {
              addCurrentLap();
              if(SaveTCX.saveToFile(this.TCXData)){
                Toast.makeText(context, R.string.tcxexporting, Toast.LENGTH_SHORT).show();
              } else {
                Toast.makeText(context, R.string.tcxerror, Toast.LENGTH_SHORT).show();
              }
              resetTcxData();
            }, 1);
        } else {
            resetTcxData();
        }
    }

    private void resetTcxData(){
        this.currentLap = null;
        this.TCXData = new TCXData();
    }

    private void addCurrentLap(){
        if (this.currentLap != null) {
            this.currentLap.setIntensity(this.currentLapStatus);
            this.currentLap.endLap(System.currentTimeMillis());
            this.currentLap.calcCalories();
            this.TCXData.addLap(this.currentLap);
        }
    }

    public void newLap(String lapStatus){
        this.addCurrentLap();
        this.currentLapStatus = lapStatus;
        this.currentLap = new Lap();
    }

    public interface hrListener{
        void onHrChanged(int hr);
    }
}
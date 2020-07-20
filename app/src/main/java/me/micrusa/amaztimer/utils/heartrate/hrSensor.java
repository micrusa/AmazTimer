package me.micrusa.amaztimer.utils.heartrate;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.Date;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.TCX.Constants;
import me.micrusa.amaztimer.TCX.SaveTCX;
import me.micrusa.amaztimer.TCX.TCXUtils;
import me.micrusa.amaztimer.TCX.data.Lap;
import me.micrusa.amaztimer.TCX.data.TCXData;
import me.micrusa.amaztimer.TCX.data.Trackpoint;
import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.heartrate.listeners.Listener;
import me.micrusa.amaztimer.utils.heartrate.listeners.experimentalListener;
import me.micrusa.amaztimer.utils.heartrate.listeners.mainListener;
import me.micrusa.amaztimer.utils.prefUtils;
import me.micrusa.amaztimer.utils.utils;

@SuppressWarnings("CanBeFinal")
public class hrSensor {
    private hrListener listener;
    private final latestTraining latestTraining = new latestTraining();
    private long startTime;
    private int accuracy = 2;
    private String latestTrackpointTime;
    public int latestHr = 0;

    private static hrSensor hrSensor;
    private Listener hrListener;

    //All tcx needed stuff
    private String currentLapStatus = Constants.STATUS_RESTING;
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

    public void newValue(int v){
        v = hrUtils.getFlattenedHr(v); //Hr will be flattened if the preference is enabled
        if(latestHr != v){
            listener.onHrChanged(v);
            latestHr = v;
        }
        //Send hr value to latestTraining array
        latestTraining.addHrValue(v);
        //Set latest hr value
        String currentDate = TCXUtils.formatDate(new Date());
        //Create Trackpoint and add it to current Lap
        if (!currentDate.equals(this.latestTrackpointTime) && Prefs.getBoolean(defValues.KEY_TCX, defValues.DEFAULT_TCX)) { //This will limit trackpoints to 1/s
            currentLap.addTrackpoint(new Trackpoint(v, new Date()));
            this.latestTrackpointTime = currentDate;
        }
    }

    public void onAccuracyChanged(Sensor param1Sensor, int param1Int) {
        this.accuracy = param1Int;
    }

    public void registerListener(Context context) {
        utils.setupPrefs(context);
        //Clean all values to avoid merging other values
        latestTraining.cleanAllValues();
        //Register start time
        this.startTime = System.currentTimeMillis();
        //Register listener taking into account experimental sensor
        if(Prefs.getBoolean(defValues.KEY_HREXPERIMENT, false))
            hrListener = new experimentalListener();
        else
            hrListener = new mainListener();
        hrListener.register(context);
    }

    public void unregisterListener(Context context) {
        //Unregister listener
        hrListener.unregister(context);
        //Save time and send it to latestTraining
        long endTime = System.currentTimeMillis();
        int totalTimeInSeconds = (int) (endTime - startTime) / 1000;
        latestTraining.saveDataToFile(context, totalTimeInSeconds);
        if (Prefs.getBoolean(defValues.KEY_TCX, defValues.DEFAULT_TCX)) {
            addCurrentLap();
            boolean result = SaveTCX.saveToFile(this.TCXData);
            resetTcxData();
            utils.setLang(context, Prefs.getString(defValues.KEY_LANG, "en"));
            if (result)
                Toast.makeText(context, R.string.tcxexporting, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, R.string.tcxerror, Toast.LENGTH_SHORT).show();
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
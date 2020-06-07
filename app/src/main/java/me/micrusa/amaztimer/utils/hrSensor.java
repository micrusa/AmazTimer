package me.micrusa.amaztimer.utils;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import me.micrusa.amaztimer.defValues;

@SuppressWarnings("CanBeFinal")
public class hrSensor implements SensorEventListener {
    private Context context;
    private SensorManager sensorManager;
    private Sensor hrSens;
    private TextView hrText;
    private final me.micrusa.amaztimer.defValues defValues = new defValues();
    private final latestTraining latestTraining = new latestTraining();
    private long startTime;
    private int accuracy;
    private int latestHr = 0;

    public hrSensor(Context c, TextView hr) {
        //Setup sensor manager, sensor and textview
        this.sensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            this.hrSens = sensorManager.getDefaultSensor(defValues.HRSENSOR);
        }
        this.hrText = hr;
        this.context = c;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int v = (int) event.values[0];
        if (isAccuracyValid()) {
            //Get hr value and set the text if battery saving mode is disabled
            if (!new file(defValues.SETTINGS_FILE, this.context).get(defValues.SETTINGS_BATTERYSAVING, defValues.DEFAULT_BATTERYSAVING)
                    && !new file(defValues.SETTINGS_FILE, this.context).get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE))
                this.hrText.setText(String.valueOf(v));
            //Send hr value to latestTraining array
            latestTraining.addHrValue(v);
            //Set latest hr value
            this.latestHr = v;
        } else {
            Log.i("AmazTimer", "hrSensor: unvalid heart rate: " + String.valueOf(v) + " with " + String.valueOf(this.accuracy) + " accuracy");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor param1Sensor, int param1Int) {
        this.accuracy = param1Int;
    }

    public void registerListener() {
        //Clean all values to avoid merging other values
        latestTraining.cleanAllValues(this.context);
        //Register listener with delay in defValues class
        this.sensorManager.registerListener(this, this.hrSens, defValues.HRSENSOR_DELAY);
        //Register start time
        this.startTime = System.currentTimeMillis();
    }

    public int getLatestValue(){
        return this.latestHr;
    }

    private boolean isAccuracyValid(){
        return this.accuracy >= defValues.ACCURACY_RANGE[0] && this.accuracy <= defValues.ACCURACY_RANGE[1];
    }

    public void unregisterListener() {
        //Unregister listener to avoid battery drain
        this.sensorManager.unregisterListener(this, this.hrSens);
        //Save time and send it to latestTraining
        long endTime = System.currentTimeMillis();
        int totalTimeInSeconds = (int) (endTime - startTime) / 1000;
        latestTraining.saveDataToFile(this.context, totalTimeInSeconds);
    }
}
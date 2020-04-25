package me.micrusa.amaztimer.utils;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import me.micrusa.amaztimer.defValues;

public class hrSensor implements SensorEventListener {
    private Context context;
    private SensorManager sensorManager;
    private Sensor hrSens;
    private TextView hrText;
    private me.micrusa.amaztimer.defValues defValues = new defValues();
    private latestTraining latestTraining = new latestTraining();
    private long startTime;

    public hrSensor(Context c, TextView hr) {
        //Setup sensor manager, sensor and textview
        sensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
        hrSens = sensorManager.getDefaultSensor(defValues.hrSensor);
        this.hrText = hr;
        this.context = c;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int v = (int) event.values[0];
        this.hrText.setText(String.valueOf(v));
        latestTraining.addHrValue(v);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void registerListener() {
        latestTraining.cleanAllValues(this.context);
        //Register listener with delay in defValues class
        sensorManager.registerListener(this, this.hrSens, defValues.hrSensorDelay);
        //Register start time
        this.startTime = System.currentTimeMillis();
    }

    public void unregisterListener() {
        sensorManager.unregisterListener(this, this.hrSens);
        long endTime = System.currentTimeMillis();
        int totalTimeInSeconds = (int) (endTime - startTime) / 1000;
        latestTraining.saveDataToFile(this.context, totalTimeInSeconds);
    }
}
package me.micrusa.amaztimer.utils;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import me.micrusa.amaztimer.defValues;

public class hrSensor implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor hrSens;
    private TextView hrText;
    private me.micrusa.amaztimer.defValues defValues = new defValues();

    public hrSensor(Context c, TextView hr){
        //Setup sensor manager, sensor and textview
        sensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
        hrSens = sensorManager.getDefaultSensor(defValues.hrSensor);
        this.hrText = hr;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int v = (int) event.values[0];
        this.hrText.setText(String.valueOf(v));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    public void registerListener() {
        //Register listener with delay in defValues class
        sensorManager.registerListener(this, this.hrSens, defValues.hrSensorDelay);
    }

    public void unregisterListener() {
        sensorManager.unregisterListener(this, this.hrSens);
    }
}
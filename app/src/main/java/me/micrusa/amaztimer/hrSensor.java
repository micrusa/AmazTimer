package me.micrusa.amaztimer;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

public class hrSensor implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor hrSens;
    private TextView hrText;
    private defValues defValues = new defValues();

    hrSensor(Context c, TextView hr){
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

    void registerListener() {
        sensorManager.registerListener(this, this.hrSens, defValues.hrSensorDelay);
    }

    void unregisterListener() {
        sensorManager.unregisterListener(this, this.hrSens);
    }
}
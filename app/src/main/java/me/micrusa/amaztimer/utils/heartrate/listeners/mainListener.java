package me.micrusa.amaztimer.utils.heartrate.listeners;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

import me.micrusa.amaztimer.utils.heartrate.hrSensor;

public class mainListener implements SensorEventListener, Listener {
    @Override
    public void onSensorChanged(SensorEvent event) {
        hrSensor.getInstance().newValue((int) event.values[0]);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        hrSensor.getInstance().onAccuracyChanged(sensor, accuracy);
    }

    public void register(Context context){
        SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(this, sm.getDefaultSensor(21 /*Android HR sensor (processed data)*/),
                SensorManager.SENSOR_DELAY_FASTEST, 500_000 /*500ms batching*/,
                new Handler() /*Use a handler to avoid freezes*/);
    }
    public void unregister(Context context){
        SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sm.unregisterListener(this);
    }
}
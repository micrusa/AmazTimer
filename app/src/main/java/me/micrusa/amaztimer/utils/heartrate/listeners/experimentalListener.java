package me.micrusa.amaztimer.utils.heartrate.listeners;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

import me.micrusa.amaztimer.utils.heartrate.hrSensor;

public class experimentalListener implements SensorEventListener, Listener {

    private long lastTime;
    private long totalDataThisBatch = 0;
    private long currentBatchSize = 0;

    public experimentalListener(){
        this.lastTime = System.currentTimeMillis();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long now = System.currentTimeMillis();
        totalDataThisBatch += (int) event.values[0] / 100;
        currentBatchSize++;
        if(now - lastTime >= 500) { //This sensor is SO fast so limit rate to a value every 500ms
            int v = (int) (totalDataThisBatch / currentBatchSize);
            hrSensor.getInstance().newValue(v);
            totalDataThisBatch = 0;
            currentBatchSize = 0;
            lastTime = now;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        hrSensor.getInstance().onAccuracyChanged(sensor, accuracy);
    }

    public void register(Context context){
        SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(this, sm.getDefaultSensor(65538 /*PPG Sensor*/),
                200_000 /*It's much faster*/, 500_000 /*500ms batching*/,
                new Handler() /*Use a handler to avoid freezes*/);
    }
    public void unregister(Context context){
        SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sm.unregisterListener(this);
    }
}
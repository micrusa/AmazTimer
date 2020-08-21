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
    private long totalHrCurrentBatch = 0;
    private long currentBatchSize = 0;

    public experimentalListener(){
        this.lastTime = System.currentTimeMillis();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int thisValue = (int) (event.values[0] / 100);
        if(thisValue >= 220 || thisValue <= 40) return; //Return on bad values for a more accurate result
        long now = System.currentTimeMillis();
        totalHrCurrentBatch += thisValue;
        currentBatchSize++;
        if(now - lastTime >= 750) { //This sensor is SO fast so limit rate to a value every 750ms
            int v = (int) (totalHrCurrentBatch / currentBatchSize);
            hrSensor.getInstance().newValue(v);
            totalHrCurrentBatch = 0;
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
                200_000 /*This value is ignored*/, 500_000 /*500ms batching*/,
                new Handler() /*Use a handler to avoid freezes*/);
    }
    public void unregister(Context context){
        SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sm.unregisterListener(this);
    }
}
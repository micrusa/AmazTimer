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

package me.micrusa.amaztimer.utils.sensors.heartrate.listeners;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

import java.util.ArrayList;

import me.micrusa.amaztimer.utils.sensors.heartrate.hrSensor;
import me.micrusa.amaztimer.utils.sensors.objects.Listener;
import me.micrusa.amaztimer.utils.sensors.repsCounter.utils.Filtering;

public class experimentalListener implements SensorEventListener, Listener {

    private ArrayList<Float> allHr = new ArrayList<>();
    private long lastTime;

    public experimentalListener(){
        this.lastTime = System.currentTimeMillis();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float thisValue = event.values[0] / 100;
        if(!(thisValue >= 220 || thisValue <= 40))
            allHr.add(thisValue); //Add just great values for a more accurate result

        long now = System.currentTimeMillis();
        if(now - lastTime >= 750 && allHr.size() >= 1) { //This sensor is SO fast so limit rate to a value every 750ms
            double[] values = Filtering.filterSignal(allHr, 80000, 30000, 2, 0, 15);
            double totalValue = 0;
            for(double value : values)
                totalValue += value;
            hrSensor.getInstance().newValue((int) (totalValue / values.length));
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
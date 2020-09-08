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

package me.micrusa.amaztimer.utils.sensors.repsCounter.listeners;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

import org.tinylog.Logger;

import java.util.ArrayList;

import me.micrusa.amaztimer.utils.sensors.objects.Listener;
import me.micrusa.amaztimer.utils.sensors.repsCounter.RepsCounter;

public class Accelerometer implements SensorEventListener, Listener {

    @Override
    public void onSensorChanged(SensorEvent event) {
        float accelerationX = event.values[0];
        float accelerationY = event.values[1];
        float accelerationZ = event.values[2];

        //Noise filtering
        if (Math.abs(accelerationX) < RepsCounter.CURRENT_EXERCISE.MIN_MOVEMENT_TO_RECORD) accelerationX = 0;
        if (Math.abs(accelerationY) < RepsCounter.CURRENT_EXERCISE.MIN_MOVEMENT_TO_RECORD) accelerationY = 0;
        if (Math.abs(accelerationZ) < RepsCounter.CURRENT_EXERCISE.MIN_MOVEMENT_TO_RECORD) accelerationZ = 0;

        //Logger.debug("Received X:" + accelerationX + " Y:" + accelerationY + " Z:" + accelerationZ);

        RepsCounter.newAccelValues(accelerationX, accelerationY, accelerationZ);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void register(Context context) {
        SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME, new Handler());
    }

    @Override
    public void unregister(Context context) {
        SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sm.unregisterListener(this);
    }

}

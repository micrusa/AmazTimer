package me.micrusa.amaztimer;

import android.hardware.Sensor;
import android.hardware.SensorManager;

class defValues {
    int sets = 8;
    int workTime = 20;
    int restTime = 10;
    int sVibration = 100; //0.1s
    int lVibration = 500; //0.5s
    int hrSensorDelay = SensorManager.SENSOR_DELAY_UI;
    int hrSensor = Sensor.TYPE_HEART_RATE;
}

package me.micrusa.amaztimer;

import android.hardware.Sensor;
import android.hardware.SensorManager;

class defValues {
    //Default sets and times
    int sets = 8;
    int workTime = 20;
    int restTime = 10;
    //Vibration times
    int sVibration = 100; //0.1s
    int lVibration = 500; //0.5s
    //Sensor value and sensor delay
    int hrSensorDelay = SensorManager.SENSOR_DELAY_FASTEST;
    int hrSensor = Sensor.TYPE_HEART_RATE;
    //Max and min values
    int minSets = 1;
    int maxSets = 99;
    int minTime = 1; //1s
    int maxTime = 900; //15m
    //Settings
    String lang = "en";
    boolean batterySaving = false;
}

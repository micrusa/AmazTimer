package me.micrusa.amaztimer;

import android.hardware.Sensor;
import android.hardware.SensorManager;

class defValues {
    //Format for times
    String timeFormat = "mm:ss";
    //Default sets and times
    int defSets = 8;
    int defWorkTime = 20;
    int defRestTime = 10;
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
    boolean BatterySavingDefault = false;
    boolean HrSwitchDefault = true;
    //Files name
    String timerFile = "amaztimer";
    String settingsFile = "settings";
    //Training settings name
    String sSets = "sets";
    String sWork = "work";
    String sRest = "rest";
    String sBatterySaving = "batterySaving";
    String sHrSwitch = "hrEnabled";
}

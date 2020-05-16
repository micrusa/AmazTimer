package me.micrusa.amaztimer;

import android.hardware.Sensor;
import android.hardware.SensorManager;

public class defValues {
    //Format for times
    public final String timeFormat = "mm:ss";
    //Default sets and times
    public final int defSets = 8;
    public final int defWorkTime = 20;
    public final int defRestTime = 10;
    //Vibration times
    public final int sVibration = 100; //0.1s
    public final int lVibration = 500; //0.5s
    //Preparation times
    public final int sPrepare = 5 * 1000;
    public final int lPrepare = 60 * 1000;
    //Sensor value and sensor delay
    public final int hrSensorDelay = SensorManager.SENSOR_DELAY_FASTEST;
    public final int hrSensor = Sensor.TYPE_HEART_RATE;
    //Max and min values
    public final int minSets = 1;
    public final int maxSets = 99;
    public final int minTime = 1; //1s
    public final int maxTime = 900; //15m
    //Default values
    public final String LangDefault = "en";
    public final Boolean defLongprep = false;
    public final boolean defBatterySaving = false;
    public final boolean defHrSwitch = true;
    public final boolean defRepsMode = false;
    public final int defWeight = 70;
    public final int defAge = 20;
    public final boolean defMale = true;
    public final int defHrValues = 0;
    public final int defKcalValues = 0;
    //Files name
    //They're in different files bc sometimes they have conflicts if they're all in the same file
    public final String timerFile = "amaztimer";
    public final String settingsFile = "settings";
    public final String latestTrainFile = "latesttrain";
    public final String bodyFile = "bodysettings";
    //Settings names
    public final String sSets = "sets";
    public final String sWork = "work";
    public final String sRest = "rest";
    public final String sBatterySaving = "batterySaving";
    public final String sHrSwitch = "hrEnabled";
    public final String sLang = "lang";
    public final String sWeight = "weight";
    public final String sAge = "age";
    public final String sMale = "gender";
    public final String sAvgHr = "avghr";
    public final String sMinHr = "minhr";
    public final String sMaxHr = "maxhr";
    public final String sKcal = "kcal";
    public final String sLongPrepare = "longprep";
    public final String sRepsMode = "repsmode";
}

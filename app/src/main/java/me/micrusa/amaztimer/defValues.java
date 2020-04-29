package me.micrusa.amaztimer;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.ArrayList;

public class defValues {
    //Format for times
    public String timeFormat = "mm:ss";
    //Default sets and times
    public int defSets = 8;
    public int defWorkTime = 20;
    public int defRestTime = 10;
    //Vibration times
    public int sVibration = 100; //0.1s
    public int lVibration = 500; //0.5s
    //Preparation times
    public int sPrepare = 5 * 1000;
    public int lPrepare = 60 * 1000;
    //Sensor value and sensor delay
    public int hrSensorDelay = SensorManager.SENSOR_DELAY_FASTEST;
    public int hrSensor = Sensor.TYPE_HEART_RATE;
    //Max and min values
    public int minSets = 1;
    public int maxSets = 99;
    public int minTime = 1; //1s
    public int maxTime = 900; //15m
    //Settings
    public String LangDefault = "en";
    public Boolean defLongprep = false;
    public boolean defBatterySaving = false;
    public boolean defHrSwitch = true;
    public int defWeight = 70;
    public int defAge = 20;
    public boolean defMale = true;
    public int defHrValues = 0;
    public int defKcalValues = 0;
    //Files name
    //They're in different files bc sometimes they have conflicts if they're all in the same file
    public String timerFile = "amaztimer";
    public String settingsFile = "settings";
    public String latestTrainFile = "latesttrain";
    public String bodyFile = "bodysettings";
    //Training settings name
    public String sSets = "sets";
    public String sWork = "work";
    public String sRest = "rest";
    public String sBatterySaving = "batterySaving";
    public String sHrSwitch = "hrEnabled";
    public String sLang = "lang";
    public String sWeight = "weight";
    public String sAge = "age";
    public String sMale = "gender";
    public String sAvgHr = "avghr";
    public String sMinHr = "minhr";
    public String sMaxHr = "maxhr";
    public String sKcal = "kcal";
    public String sLongPrepare = "longprep";
}

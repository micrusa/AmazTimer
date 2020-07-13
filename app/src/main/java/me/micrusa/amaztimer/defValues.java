package me.micrusa.amaztimer;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class defValues {
    //Format for times
    public static final String timeFormat = "mm:ss";
    //Default sets and times
    public static final int DEF_SETS = 8;
    public static final int DEF_WORKTIME = 20;
    public static final int DEF_RESTTIME = 10;
    //Vibration times
    public static final int SHORT_VIBRATION = 100; //0.1s
    public static final int LONG_VIBRATION = 700; //0.7s
    //Preparation times
    //Sensor value and sensor delay
    public static final int HRSENSOR_DELAY = SensorManager.SENSOR_DELAY_FASTEST;
    public static final int HRSENSOR = Sensor.TYPE_HEART_RATE;
    public static final int[] ACCURACY_RANGE = {0, 3};
    //Max and min values
    public static final int MIN_SETS = 1;
    public static final int MAX_SETS = 99;
    public static final int MIN_TIME = 1; //1s
    public static final int MAX_TIME = 900; //15m
    //Default values
    public static final boolean DEFAULT_HRTOGGLE = true;
    public static final int DEFAULT_WEIGHT = 70;
    public static final int DEFAULT_AGE = 20;
    public static final boolean DEFAULT_MALE = true;
    public static final int DEFAULT_HR_VALUES = 0;
    public static final boolean DEFAULT_TCX = true;
    public static final boolean DEFAULT_SOUND = true;
    //Files name
    //They're in different files bc sometimes they have conflicts if they're all in the same file
    public static final String TIMER_FILE = "amaztimer";
    //Settings names
    public static final String SETTINGS_SETS = "sets";
    public static final String SETTINGS_WORK = "work";
    public static final String SETTINGS_REST = "rest";
    //Settings keys
    public static final String KEY_SETS = "sets";
    public static final String KEY_WORK = "work";
    public static final String KEY_REST = "rest";
    public static final String KEY_HRTOGGLE = "hrOn";
    public static final String KEY_LANG = "lang";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_AGE = "age";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_ENABLEPREPARE = "prepon";
    public static final String KEY_REPSMODE = "repsmode";
    public static final String KEY_SAVED = "saved";
    public static final String KEY_LATESTTRAIN = "latesttrain";
    public static final String KEY_APPINFO = "appinfo";
    public static final String KEY_WORKOUT = "workoutmode";
    public static final String KEY_TCX = "tcx";
    public static final String KEY_SOUND = "sound";
    public static final String KEY_AVGHR = "avghr";
    public static final String KEY_MAXHR = "maxhr";
    public static final String KEY_MINHR = "minhr";
    public static final String KEY_KCAL = "kcal";
    //Some useful stuff
    public static final String VERSION_NAME = "v" + BuildConfig.VERSION_NAME;
    public static final int VERSION_CODE = BuildConfig.VERSION_CODE;
    public static final int CURRENT_YEAR = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.US).format(new Date()));
}

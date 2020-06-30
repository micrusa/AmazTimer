package me.micrusa.amaztimer;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    public static final int SHORT_PREPARETIME = 5 * 1000;
    public static final int LONG_PREPARETIME = 60 * 1000;
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
    public static final String DEFAULT_LANG = "en";
    public static final Boolean DEFAULT_LONGPREPARE = false;
    public static final Boolean DEFAULT_ENABLEPREPARE = true;
    public static final boolean DEFAULT_BATTERYSAVING = false;
    public static final boolean DEFAULT_HRSWITCH = true;
    public static final boolean DEFAULT_REPSMODE = false;
    public static final int DEFAULT_WEIGHT = 70;
    public static final int DEFAULT_AGE = 20;
    public static final boolean DEFAULT_MALE = true;
    public static final int DEFAULT_HR_VALUES = 0;
    public static final int DEFAULT_KCAL_VALUES = 0;
    public static final boolean DEFAULT_WORKOUTMODE = false;
    public static final boolean DEFAULT_CHRONOMODE = false;
    public static final boolean DEFAULT_TCX = true;
    public static final boolean DEFAULT_SOUND = true;
    //Files name
    //They're in different files bc sometimes they have conflicts if they're all in the same file
    public static final String TIMER_FILE = "amaztimer";
    public static final String SETTINGS_FILE = "settings";
    public static final String LATEST_TRAIN_FILE = "latesttrain";
    public static final String BODY_FILE = "bodysettings";
    //Settings names
    public static final String SETTINGS_SETS = "sets";
    public static final String SETTINGS_WORK = "work";
    public static final String SETTINGS_REST = "rest";
    public static final String SETTINGS_BATTERYSAVING = "batterySaving";
    public static final String SETTINGS_HRSWITCH = "hrEnabled";
    public static final String SETTINGS_LANG = "lang";
    public static final String SETTINGS_WEIGHT = "weight";
    public static final String SETTINGS_AGE = "age";
    public static final String SETTINGS_MALE = "gender";
    public static final String SETTINGS_AVGHR = "avghr";
    public static final String SETTINGS_MINHR = "minhr";
    public static final String SETTINGS_MAXHR = "maxhr";
    public static final String SETTINGS_KCAL = "kcal";
    public static final String SETTINGS_LONGPREPARE = "longprep";
    public static final String SETTINGS_ENABLEPREPARE = "prepon";
    public static final String SETTINGS_REPSMODE = "repsmode";
    public static final String SETTINGS_WORKOUTMODE = "workout";
    public static final String SETTINGS_CHRONOMODE = "chrono";
    public static final String SETTINGS_TCX = "tcx";
    public static final String SETTINGS_SOUND = "sound";
    //Settings keys
    public static final String KEY_BATTERYSAVING = "batterySaving";
    public static final String KEY_HRTOGGLE = "hrOn";
    public static final String KEY_LANG = "lang";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_AGE = "age";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_LONGPREPARE = "huamiactivity";
    public static final String KEY_ENABLEPREPARE = "prepon";
    public static final String KEY_REPSMODE = "repsmode";
    public static final String KEY_SAVED = "saved";
    public static final String KEY_LATESTTRAIN = "latesttrain";
    public static final String KEY_APPINFO = "appinfo";
    public static final String KEY_WORKOUT = "workoutmode";
    public static final String KEY_CHRONO = "chronomode";
    public static final String KEY_TCX = "tcx";
    public static final String KEY_SOUND = "sound";
    //Some useful stuff
    public static final String VERSION_NAME = "v" + BuildConfig.VERSION_NAME;
    public static final int VERSION_CODE = BuildConfig.VERSION_CODE;
    @SuppressLint("SimpleDateFormat")
    public static final int CURRENT_YEAR = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
}

package me.micrusa.amaztimer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import me.micrusa.amaztimer.TCX.Constants;
import me.micrusa.amaztimer.button.buttonEvent;
import me.micrusa.amaztimer.button.buttonListener;
import me.micrusa.amaztimer.utils.handlers.chronoHandler;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.heartrate.hrSensor;
import me.micrusa.amaztimer.utils.handlers.timeHandler;
import me.micrusa.amaztimer.utils.handlers.timerHandler;
import me.micrusa.amaztimer.utils.utils;

public class TimerActivity extends AppCompatActivity {

    private file settingsFile, timerFile;
    private TextView time, status, intervaltime, heartrate;
    private Chronometer elapsedtime;
    private Button cancel, finishset;

    private timerHandler timerHandler;
    private me.micrusa.amaztimer.utils.handlers.timeHandler timeHandler;

    private boolean hasResumed;
    private boolean isWorking;
    private boolean hasFinished;
    private int currSet;

    private me.micrusa.amaztimer.utils.handlers.chronoHandler chronoHandler;
    private me.micrusa.amaztimer.button.buttonListener buttonListener = new buttonListener();

    private void init(){
        settingsFile = new file(defValues.SETTINGS_FILE);
        timerFile = new file(defValues.TIMER_FILE);
        utils.setLang(this, settingsFile.get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
        setContentView(R.layout.activity_timer);
        time = findViewById(R.id.time);
        status = findViewById(R.id.status);
        intervaltime = findViewById(R.id.intervaltime);
        heartrate = findViewById(R.id.heartrate);
        elapsedtime = findViewById(R.id.totaltime);
        cancel = findViewById(R.id.cancel);
        finishset = findViewById(R.id.finishset);
        setupBtnListener();
        //Setup onClickListeners
        cancel.setOnLongClickListener(view -> endActivity());
        finishset.setOnClickListener(view -> {
            if (isWorking)
                resting();
            else
                working();
        });
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        startTimer();
    }

    private void startTimer(){
        //Setup time
        timeHandler = new timeHandler(time);
        //Setup total time elapsed chrono
        elapsedtime.setBase(SystemClock.elapsedRealtime());
        elapsedtime.start();

        hrSensor.initialize(hr -> heartrate.setText(String.valueOf(hr)));
        if(settingsFile.get(defValues.SETTINGS_HRSWITCH, defValues.DEFAULT_HRSWITCH))
            hrSensor.getInstance().registerListener(this); //Register if hr enabled

        currSet = utils.isModeManualSets() ? 0 : timerFile.get(defValues.SETTINGS_SETS, defValues.DEF_SETS) + 1;
        working();
    }

    private boolean endActivity(){
        if(hasFinished) return true;
        hasFinished = true;
        if(settingsFile.get(defValues.SETTINGS_HRSWITCH, defValues.DEFAULT_HRSWITCH))
            hrSensor.getInstance().unregisterListener(this); //Unregister if hr enabled
        if (timerHandler != null) timerHandler.stop();
        if (chronoHandler != null) chronoHandler.stop();
        if (timeHandler != null) timeHandler.stop();
        finish();
        return true;
    }

    private void working(){
        updateStatus(true);
    }
    private void resting(){
        updateStatus(false);
    }

    private void updateStatus(boolean working){
        isWorking = working;
        if(working && (utils.isModeManualSets() ? ++currSet : --currSet) == 0) endActivity();
        hrSensor.getInstance().newLap(working ? Constants.STATUS_ACTIVE : Constants.STATUS_RESTING);
        String text = getResources().getString(working ? R.string.work : R.string.rest);
        status.setBackground(getDrawable(working ? R.color.work : R.color.rest));
        status.setText(currSet + "|" + text);
        if(chronoHandler != null) chronoHandler.stop();
        if(timerHandler != null) timerHandler.stop();
        if(utils.getMode() >= (working ? 1 : 2))
            chronoHandler = new chronoHandler(intervaltime);
        else
            timerHandler = new timerHandler(intervaltime
                    , working ? timerFile.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME) : timerFile.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME)
                    , working ? this::resting : this::working, this);
    }

    //Destroy, pause, resume and button stuff
    public void onDestroy() {
        super.onDestroy();
        buttonListener.stop();
        endActivity();
    }
    public void onPause() {
        this.hasResumed = false;
        buttonListener.stop();
        new Handler().postDelayed(() -> {
            if(hasResumed)
                return;
            endActivity();
        }, 15 * 1000);
        super.onPause();
    }
    public void onResume() {
        hasResumed = true;
        setupBtnListener();
        super.onResume();
    }
    public void onStop(){
        super.onStop();
        buttonListener.stop();
    }
    public void onStart(){
        super.onStart();
        setupBtnListener();
    }
    private void setupBtnListener(){
        buttonListener.start(this, ButtonEvent -> {
            if(ButtonEvent.getKey() == buttonEvent.KEY_DOWN)
                new Handler().post(() -> cancel.performLongClick());
            else if(ButtonEvent.getKey() == buttonEvent.KEY_UP)
                new Handler().post(() -> finishset.performClick());
        });
    }
}
package me.micrusa.amaztimer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.micrusa.amaztimer.TCX.Constants;
import me.micrusa.amaztimer.TCX.TCXUtils;
import me.micrusa.amaztimer.button.buttonEvent;
import me.micrusa.amaztimer.button.buttonListener;
import me.micrusa.amaztimer.utils.chronoHandler;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.hrSensor;
import me.micrusa.amaztimer.utils.timerHandler;
import me.micrusa.amaztimer.utils.utils;

public class TimerActivity extends AppCompatActivity {

    private file settingsFile, timerFile;
    private TextView time, status, intervaltime, heartrate;
    private Chronometer elapsedtime;
    private Button cancel, finishset;

    private timerHandler timerHandler;

    private boolean hasResumed;
    private boolean isWorking;
    private int currSet;

    private me.micrusa.amaztimer.utils.chronoHandler chronoHandler;
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
        cancel.setOnLongClickListener(view -> endTimer());
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
        final Handler timeHandler = new Handler(Looper.getMainLooper());
        timeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                time.setText(new SimpleDateFormat("HH:mm", Locale.US).format(new Date()));
                timeHandler.postDelayed(this, 1000);
            }
        }, 10);
        //Setup total time chrono
        elapsedtime.setBase(SystemClock.elapsedRealtime());
        elapsedtime.start();
        //Start hrSensor
        hrSensor.initialize(hr -> heartrate.setText(String.valueOf(hr)));
        hrSensor.getInstance().registerListener(this);
        //Set sets int (Text will be set on working())
        currSet = utils.isModeManualSets() ? 0 : timerFile.get(defValues.SETTINGS_SETS, defValues.DEF_SETS) + 1;
        //Start working
        working();
    }

    private boolean endTimer(){
        hrSensor.getInstance().unregisterListener(this);
        if (timerHandler != null)
            timerHandler.stop();
        if(chronoHandler != null)
            chronoHandler.stop();
        finish();
        return true;
    }

    //Statuses
    private void working(){
        if((utils.isModeManualSets() ? ++currSet : --currSet) == 0)
            endTimer();
        hrSensor.getInstance().newLap(Constants.STATUS_ACTIVE);
        updateStatus(true);
        if(utils.getMode() > 0){
            chronoHandler = new chronoHandler(intervaltime);
            updateButtons(true);
        } else {
            timerHandler = new timerHandler(intervaltime
                    , timerFile.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME)
                    , this::resting, this);
            updateButtons(false);
        }
    }
    private void resting(){
        hrSensor.getInstance().newLap(Constants.STATUS_ACTIVE);
        updateStatus(false);
        if(utils.getMode() >= 2){
            chronoHandler = new chronoHandler(intervaltime);
            updateButtons(true);
        } else {
            timerHandler = new timerHandler(intervaltime
                    , timerFile.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME)
                    , this::working, this);
            updateButtons(false);
        }
    }

    private void updateStatus(boolean working){
        isWorking = working;
        String text = working ? getResources().getString(R.string.work) : getResources().getString(R.string.rest);
        status.setBackground(getDrawable(working ? R.color.work : R.color.rest));
        status.setText(currSet + "|" + text);
        if(chronoHandler != null) chronoHandler.stop();
    }

    private void updateButtons(boolean manual){
        finishset.setVisibility(manual ? View.VISIBLE : View.GONE);
        cancel.setHeight(manual ? 70 : 35);
    }

    //Destroy, pause, resume and button stuff
    public void onDestroy() {
        super.onDestroy();
        buttonListener.stop();
        endTimer();
    }
    public void onPause() {
        this.hasResumed = false;
        new Handler().postDelayed(() -> {
            if(hasResumed())
                return;
            buttonListener.stop();
            endTimer();
        }, 15 * 1000);
        super.onPause();
    }
    private boolean hasResumed(){return this.hasResumed;}
    public void onResume() {
        this.hasResumed = true;
        setupBtnListener();
        super.onResume();
    }
    private void setupBtnListener(){
        //Create a Handler because buttonListener runs in a different thread
        final Handler btnListenerHandler = new Handler();
        final Runnable downBtn = () -> cancel.performLongClick();
        final Runnable upperBtn = () -> finishset.performClick();
        buttonListener.start(this, ButtonEvent -> {
            if(ButtonEvent.getKey() == buttonEvent.KEY_DOWN)
                btnListenerHandler.post(downBtn);
            else if(ButtonEvent.getKey() == buttonEvent.KEY_UP)
                btnListenerHandler.post(upperBtn);
        });
    }

}
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

package me.micrusa.amaztimer.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.saveworkout.SaveWorkout;
import me.micrusa.amaztimer.utils.sensors.repsCounter.RepsCounter;
import me.micrusa.amaztimer.utils.sensors.repsCounter.ui.dialog.NewRepExerciseDialog;
import me.micrusa.amaztimer.utils.tcx.TCXConstants;
import me.micrusa.amaztimer.utils.button.ButtonListener;
import me.micrusa.amaztimer.utils.handlers.chronoHandler;
import me.micrusa.amaztimer.utils.handlers.hrZoneHandler;
import me.micrusa.amaztimer.utils.handlers.timeHandler;
import me.micrusa.amaztimer.utils.handlers.timerHandler;
import me.micrusa.amaztimer.utils.sensors.heartrate.hrSensor;
import me.micrusa.amaztimer.utils.Utils;

public class TimerActivity extends AppCompatActivity {
    private View hrZoneView;
    private Chronometer elapsedtime;
    private Button cancel, finishset;
    private TextView time, status, intervaltime, heartrate, sets, reps;

    private timeHandler timeHandler;
    private timerHandler timerHandler;
    public hrZoneHandler hrZoneHandler;
    private chronoHandler chronoHandler;

    private int currSet;
    private boolean isWorking;
    private boolean hasResumed;
    private boolean hasFinished;
    
    public static boolean isRunning;

    private ButtonListener buttonListener = new ButtonListener();

    private void init(){
        Utils.setupLang(this);
        setContentView(R.layout.activity_timer);
        time = findViewById(R.id.time);
        status = findViewById(R.id.status);
        sets = findViewById(R.id.sets);
        intervaltime = findViewById(R.id.intervaltime);
        heartrate = findViewById(R.id.heartrate);
        reps = findViewById(R.id.repscounter);
        elapsedtime = findViewById(R.id.totaltime);
        cancel = findViewById(R.id.cancel);
        finishset = findViewById(R.id.finishset);
        hrZoneView = findViewById(R.id.hrzoneView);
        hrZoneHandler = new hrZoneHandler(hrZoneView);
        setupBtnListener();

        cancel.setOnLongClickListener(view -> {
            Utils.vibrate(Constants.HAPTIC_VIBRATION, view.getContext());
            endActivity();
            return true;
        });
        finishset.setOnClickListener(view -> {
            Utils.vibrate(Constants.HAPTIC_VIBRATION, view.getContext());
            updateStatus(!isWorking);
        });
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        startTimer();
    }

    private void startTimer(){
        isRunning = true;

        //Setup all widgets
        timeHandler = new timeHandler(time);
        elapsedtime.setBase(SystemClock.elapsedRealtime());
        elapsedtime.start();

        RepsCounter.startIfEnabled(i -> reps.setText(String.valueOf(i)), this);
        hrSensor.initialize(hr -> {
            heartrate.setText(String.valueOf(hr));
            hrZoneHandler.addHrValue(hr);
        }).registerListener(this);
        currSet = Utils.isModeManualSets() ? 0 : Prefs.getInt(Constants.KEY_SETS, Constants.DEF_SETS) + 1;
        updateStatus(true);
    }

    private void endActivity(){
        if(hasFinished) return;
        hasFinished = true;
        isRunning = false;
        if(Prefs.getBoolean(Constants.KEY_HRTOGGLE, true))
            hrSensor.getInstance().unregisterListener(this); //Unregister if hr enabled
        if(Prefs.getBoolean(Constants.KEY_REPSCOUNT, false))
            RepsCounter.stopCounting(this);
        stopHandlers(true);
        finish();
    }

    private void updateStatus(boolean working){
        stopHandlers(false);
        isWorking = working;
        isRunning = true;
        checkIfFinished(working);
        newSet(working);
        if(!working && Prefs.getBoolean(Constants.KEY_REPSCOUNT, false))
            new NewRepExerciseDialog(this).show();
        sets.setText(String.valueOf(currSet));
        status.setBackground(ContextCompat.getDrawable(this, working ? R.color.work : R.color.rest));
        status.setText(getResources().getString(working ? R.string.work : R.string.rest));
    }

    private void checkIfFinished(boolean working){
        if(working && (Utils.isModeManualSets() ? ++currSet : --currSet) == 0)
            endActivity();
    }

    private void newSet(boolean working){
        RepsCounter.newSet(working);
        SaveWorkout.endSet(!working, Prefs.getBoolean(Constants.KEY_REPSCOUNT, false) ? Integer.parseInt((String) reps.getText()) : -1);
        hrSensor.getInstance().newLap(working ? TCXConstants.STATUS_ACTIVE : TCXConstants.STATUS_RESTING);

        if(Utils.getMode() >= (working ? 1 : 2))
            chronoHandler = new chronoHandler(intervaltime);
        else
            timerHandler = new timerHandler(intervaltime
                    , working ? Prefs.getInt(Constants.KEY_WORK, Constants.DEF_WORKTIME) : Prefs.getInt(Constants.KEY_REST, Constants.DEF_RESTTIME)
                    , () -> updateStatus(!working), this);
    }
    
    private void stopHandlers(boolean isEnd){
        if(chronoHandler != null) chronoHandler.stop();
        if(timerHandler != null) timerHandler.stop();
        if(isEnd && timeHandler != null) timeHandler.stop();
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
            if(!hasResumed) endActivity();
        }, 9 * 1000);
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
        new Handler().postDelayed(() -> {
            if(!hasResumed) endActivity();
        }, 9 * 1000);
    }
    public void onStart(){
        super.onStart();
        hasResumed = true;
        setupBtnListener();
    }
    private void setupBtnListener(){
        Handler handler = new Handler();
        buttonListener.start(this, ButtonEvent -> {
            if(ButtonEvent.getKey() == me.micrusa.amaztimer.utils.button.ButtonEvent.KEY_DOWN)
                handler.post(() -> cancel.performLongClick());
            else if(ButtonEvent.getKey() == me.micrusa.amaztimer.utils.button.ButtonEvent.KEY_UP)
                handler.post(() -> finishset.performClick());
        });
    }
}
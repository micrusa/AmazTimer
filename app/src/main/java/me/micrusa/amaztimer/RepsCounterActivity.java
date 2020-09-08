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

package me.micrusa.amaztimer;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.saveworkout.SaveWorkout;
import me.micrusa.amaztimer.utils.button.buttonEvent;
import me.micrusa.amaztimer.utils.button.buttonListener;
import me.micrusa.amaztimer.utils.handlers.chronoHandler;
import me.micrusa.amaztimer.utils.handlers.hrZoneHandler;
import me.micrusa.amaztimer.utils.handlers.timeHandler;
import me.micrusa.amaztimer.utils.sensors.heartrate.hrSensor;
import me.micrusa.amaztimer.utils.sensors.repsCounter.RepsCounter;
import me.micrusa.amaztimer.utils.tcx.TCXConstants;
import me.micrusa.amaztimer.utils.utils;

public class RepsCounterActivity extends AppCompatActivity {

    private TextView time, status, intervaltime, heartrate, sets, reps;
    private Button cancel, finishset;
    private View hrZoneView;

    private me.micrusa.amaztimer.utils.handlers.timeHandler timeHandler;
    public me.micrusa.amaztimer.utils.handlers.hrZoneHandler hrZoneHandler;
    private me.micrusa.amaztimer.utils.handlers.chronoHandler chronoHandler;

    private boolean hasResumed;
    private boolean isWorking;
    private boolean hasFinished;
    private int currSet;

    public static boolean isRunning;

    private me.micrusa.amaztimer.utils.button.buttonListener buttonListener = new buttonListener();

    private void init(){
        utils.setupLang(this);
        setContentView(R.layout.activity_reps_counter);
        time = findViewById(R.id.time);
        status = findViewById(R.id.status);
        sets = findViewById(R.id.sets);
        intervaltime = findViewById(R.id.intervaltime);
        heartrate = findViewById(R.id.heartrate);
        reps = findViewById(R.id.repscounter);
        cancel = findViewById(R.id.cancel);
        finishset = findViewById(R.id.finishset);
        hrZoneView = findViewById(R.id.hrzoneView);
        hrZoneHandler = new hrZoneHandler(hrZoneView);
        setupBtnListener();
        //Setup onClickListeners
        cancel.setOnLongClickListener(view -> {
            utils.vibrate(Constants.HAPTIC_VIBRATION, view.getContext());
            endActivity();
            return true;
        });
        finishset.setOnClickListener(view -> {
            utils.vibrate(Constants.HAPTIC_VIBRATION, view.getContext());
            updateStatus(!isWorking);
        });

        RepsCounter.addRepsListener(i -> reps.setText(String.valueOf(i)));
        hrSensor.hrListener hrListener = hr -> {
            heartrate.setText(String.valueOf(hr));
            hrZoneHandler.addHrValue(hr);
        };
        if(hrSensor.getInstance() == null)
            hrSensor.initialize(hrListener);
        else
            hrSensor.getInstance().setListener(hrListener);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        startTimer();
    }

    private void startTimer(){
        isRunning = true;
        //Setup time
        timeHandler = new timeHandler(time);
        //Start counting
        RepsCounter.startCounting(this);

        if(Prefs.getBoolean(Constants.KEY_HRTOGGLE, true))
            hrSensor.getInstance().registerListener(this); //Register if hr enabled

        currSet = utils.isModeManualSets() ? 0 : Prefs.getInt(Constants.KEY_SETS, Constants.DEF_SETS) + 1;
        updateStatus(true);
    }

    private boolean endActivity(){
        if(hasFinished) return true;
        hasFinished = true;
        isRunning = false;
        if(Prefs.getBoolean(Constants.KEY_HRTOGGLE, true))
            hrSensor.getInstance().unregisterListener(this); //Unregister if hr enabled
        RepsCounter.stopCounting(this);
        stopHandlers(true);
        finish();
        return true;
    }

    private void updateStatus(boolean working){
        stopHandlers(false);
        isWorking = working;
        isRunning = true;
        if(working) {
            if ((utils.isModeManualSets() ? ++currSet : --currSet) == 0) endActivity();
        } else RepsCounter.showNewSetDialog(this);
        RepsCounter.newSet(working);
        SaveWorkout.endSet(!working);
        hrSensor.getInstance().newLap(working ? TCXConstants.STATUS_ACTIVE : TCXConstants.STATUS_RESTING);
        sets.setText(String.valueOf(currSet));
        status.setBackground(ContextCompat.getDrawable(this, working ? R.color.work : R.color.rest));
        status.setText(getResources().getString(working ? R.string.work : R.string.rest));
        chronoHandler = new chronoHandler(intervaltime);
    }

    private void stopHandlers(boolean isEnd){
        if(chronoHandler != null) chronoHandler.stop();
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
            if(ButtonEvent.getKey() == buttonEvent.KEY_DOWN)
                handler.post(() -> cancel.performLongClick());
            else if(ButtonEvent.getKey() == buttonEvent.KEY_UP)
                handler.post(() -> finishset.performClick());
        });
    }

}

package me.micrusa.amaztimer.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.hrSensor;
import me.micrusa.amaztimer.utils.utils;

public class RepsTimerActivity extends AppCompatActivity {

    private Button endSet, cancel;
    private TextView timer, hr, sets, status;
    private ConstraintLayout layout;
    private boolean timerStarted;
    private me.micrusa.amaztimer.defValues defValues = new defValues();
    private me.micrusa.amaztimer.utils.utils utils = new utils();
    private me.micrusa.amaztimer.utils.file file = new file(defValues.timerFile, this);
    private hrSensor hrSensor;
    private CountDownTimer restTimer = new CountDownTimer((long) file.get(defValues.sRest, defValues.defRestTime) * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            setTime(millisUntilFinished);
        }

        @Override
        public void onFinish() {
            work();
        }
    };

    private void setTime(long millis){
        if(millis < 4000){
            if(millis <1999){
                utils.vibrate(defValues.lVibration, this);
            }else{
                utils.vibrate(defValues.sVibration, this);
            }
        }
        if(!file.get(defValues.sBatterySaving, defValues.defBatterySaving)){
            timer.setText(utils.formatTime((int) millis / 1000));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reps_timer);
        this.init();
        //Set language
        utils.setLang(this, new file(defValues.settingsFile, this).get(defValues.sLang, defValues.LangDefault));
        //Start hr sensor
        setHrState(true, hrSensor, hr);
        work();
    }

    private void init(){
        endSet = findViewById(R.id.RepsEndSet);
        cancel = findViewById(R.id.cancel);
        timer = findViewById(R.id.RepsTimer);
        hr = findViewById(R.id.RepsHR);
        sets = findViewById(R.id.RepsSets);
        status = findViewById(R.id.RepsStatus);
        layout = findViewById(R.id.RepsLayout);
        //Define hrSensor
        hrSensor = new hrSensor(this, hr);
        //Set OnClickListeners
        cancel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Return if no timer has started
                if(!timerStarted){
                    finish();
                    return true;
                }
                restTimer.cancel();
                //Unregister hr sensor listener to avoid battery drain
                setHrState(false, hrSensor, hr);
                //Finish activity
                finish();
                return true;
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send toast
                Toast.makeText(view.getContext(), view.getResources().getString(R.string.canceltoast), Toast.LENGTH_SHORT).show();
            }
        });
        endSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sets.setText(String.valueOf(Integer.parseInt(sets.getText().toString()) - 1));
                rest();
            }
        });
    }

    private void work(){
        timerStarted = false;
        layout.setBackgroundColor(getResources().getColor(R.color.red));
        endSet.setVisibility(View.VISIBLE);
        status.setText(getResources().getString(R.string.work));
        timer.setVisibility(View.INVISIBLE);
    }

    private void rest(){
        if(Integer.parseInt(sets.getText().toString()) == 1){
            finish();
        }
        layout.setBackgroundColor(getResources().getColor(R.color.green));
        endSet.setVisibility(View.INVISIBLE);
        timer.setVisibility(View.VISIBLE);
        timerStarted = true;
        restTimer.start();
        status.setText(getResources().getString(R.string.rest));
    }

    private void setHrState(boolean state, hrSensor hrSensor, TextView hr) {
        if (state) {
            if (file.get(defValues.sHrSwitch, defValues.defHrSwitch)) {
                hrSensor.registerListener();
                if (hr.getVisibility() == View.INVISIBLE) {
                    hr.setVisibility(View.VISIBLE);
                }
            } else if (hr.getVisibility() == View.VISIBLE) {
                hr.setVisibility(View.INVISIBLE);
            }
        } else if (file.get(defValues.sHrSwitch, defValues.defHrSwitch)) {
            hrSensor.unregisterListener();
        }
    }


}

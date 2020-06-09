package me.micrusa.amaztimer.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.TCX.Constants;
import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.hrSensor;
import me.micrusa.amaztimer.utils.utils;

@SuppressWarnings("FieldCanBeLocal")
public class RepsTimerActivity extends AppCompatActivity {

    private Button endSet, cancel;
    private TextView timer, hr, sets, status;
    private ConstraintLayout layout;
    private boolean timerStarted;
    private final me.micrusa.amaztimer.defValues defValues = new defValues();
    private final me.micrusa.amaztimer.utils.utils utils = new utils();
    private me.micrusa.amaztimer.utils.file file;
    private hrSensor hrSensor;
    private CountDownTimer restTimer;

    private void setTime(long millis) {
        int v = (int) millis / 1000;
        this.init();
        if(!new file(defValues.SETTINGS_FILE, this)
                .get(defValues.SETTINGS_BATTERYSAVING, defValues.DEFAULT_BATTERYSAVING)) {
            timer.setText(utils.formatTime(v));
            if (v == 1){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        timer.setText(utils.formatTime(0));
                        utils.vibrate(defValues.LONG_VIBRATION, getContext());
                    }
                }, 950);
            }
        } else {
            if (!timer.getText().toString().equals("--:--")) {
                timer.setText("--:--");
            }
            if (new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_HRSWITCH, defValues.DEFAULT_HRSWITCH)){
                int latestHr = hrSensor.getLatestValue();
                if(latestHr == 0)
                    hr.setText(getResources().getString(R.string.nullinfo));
                else
                    hr.setText(hrSensor.getLatestValue());
            }
        }
        if (v < 4) {
            utils.vibrate(defValues.SHORT_VIBRATION, this);
        }
    }

    public Context getContext(){
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reps_timer);
        this.init();
        file = new file(defValues.TIMER_FILE, this);
        restTimer = new CountDownTimer((long) file.get(defValues.SETTINGS_REST, defValues.DEF_RESTTIME) * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setTime(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                sets.setText(String.valueOf(Integer.parseInt(sets.getText().toString()) - 1));
                work();
            }
        };
        //Set sets text
        sets.setText(String.valueOf(file.get(defValues.SETTINGS_SETS, defValues.DEF_SETS)));
        //Start hr sensor
        setHrState(true, hrSensor, hr);
        work();
    }

    private void init(){
        //Setup objects
        //Buttons
        endSet = findViewById(R.id.RepsEndSet);
        cancel = findViewById(R.id.RepsCancel);
        //TextViews
        timer = findViewById(R.id.RepsTimer);
        hr = findViewById(R.id.RepsHR);
        sets = findViewById(R.id.RepsSets);
        status = findViewById(R.id.RepsStatus);
        //Layout
        layout = findViewById(R.id.RepsLayout);
        //Define hrSensor
        hrSensor = new hrSensor(this, hr);
        //Set OnClickListeners
        cancel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Return if no timer has started
                if(timerStarted){
                    restTimer.cancel();
                }
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
                rest();
            }
        });
        //Set language and set again button texts
        utils.setLang(this, new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
        cancel.setText(getResources().getString(R.string.cancel));
        endSet.setText(getResources().getString(R.string.finishset));
    }

    private void work(){
        hrSensor.newLap(Constants.STATUS_ACTIVE);
        //Timer ended so set timerStarted to false
        timerStarted = false;
        //Set BG color to red
        layout.setBackgroundColor(getResources().getColor(R.color.red));
        //Change visibilities and status
        endSet.setVisibility(View.VISIBLE);
        status.setText(getResources().getString(R.string.work));
        timer.setVisibility(View.INVISIBLE);
    }

    private void rest(){
        hrSensor.newLap(Constants.STATUS_RESTING);
        //If 1 set left finish activity and unregister hr listener
        if(Integer.parseInt(sets.getText().toString()) == 1){
            setHrState(false, hrSensor, hr);
            finish();
            return;
        }
        //Set BG color to green
        layout.setBackgroundColor(getResources().getColor(R.color.green));
        //Change visibilities and status
        endSet.setVisibility(View.INVISIBLE);
        timer.setVisibility(View.VISIBLE);
        status.setText(getResources().getString(R.string.rest));
        //Timer is gonna start so set timerStarted to true and start timer
        timerStarted = true;
        restTimer.start();
    }

    private void setHrState(boolean state, hrSensor hrSensor, TextView hr) {
        file settingsFile = new file(defValues.SETTINGS_FILE, this);
        if (state) {
            if (settingsFile.get(defValues.SETTINGS_HRSWITCH, defValues.DEFAULT_HRSWITCH)) {
                hrSensor.registerListener();
                if (hr.getVisibility() == View.INVISIBLE) {
                    hr.setVisibility(View.VISIBLE);
                }
            } else if (hr.getVisibility() == View.VISIBLE) {
                hr.setVisibility(View.INVISIBLE);
            }
        } else if (settingsFile.get(defValues.SETTINGS_HRSWITCH, defValues.DEFAULT_HRSWITCH)) {
            hrSensor.unregisterListener();
        }
    }


}

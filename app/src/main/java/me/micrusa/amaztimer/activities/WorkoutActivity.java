package me.micrusa.amaztimer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.TCX.Constants;
import me.micrusa.amaztimer.button.buttonEvent;
import me.micrusa.amaztimer.button.buttonInterface;
import me.micrusa.amaztimer.button.buttonListener;
import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.SystemProperties;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.hrSensor;
import me.micrusa.amaztimer.utils.utils;

public class WorkoutActivity extends AppCompatActivity {

    private Button endSet, cancel;
    private TextView hr, status;
    private ConstraintLayout layout;
    private Chronometer chrono;
    private me.micrusa.amaztimer.utils.file file;
    private me.micrusa.amaztimer.utils.hrSensor hrSensor;
    private me.micrusa.amaztimer.button.buttonListener buttonListener = new buttonListener();
    private boolean isWorking;
    private boolean hasResumed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        this.init();
        setupBtnListener();
        file = new file(defValues.TIMER_FILE, this);
        //Start hr sensor
        setHrState(true, hrSensor, hr);
        work();
    }

    @Override
    public void onDestroy() {
        buttonListener.stop();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        buttonListener.stop();
        super.onStop();
    }

    @Override
    public void onPause() {
        this.hasResumed = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(hasResumed())
                    return;
                buttonListener.stop();
            }
        }, 15 * 1000);
        super.onPause();
    }

    private boolean hasResumed(){
        return hasResumed;
    }

    public void onResume() {
        this.hasResumed = true;
        setupBtnListener();
        super.onResume();
    }

    private void init(){
        //Setup objects
        //Buttons
        endSet = findViewById(R.id.RepsEndSet);
        cancel = findViewById(R.id.RepsCancel);
        //TextViews
        hr = findViewById(R.id.RepsHR);
        status = findViewById(R.id.RepsStatus);
        //Layout
        layout = findViewById(R.id.RepsLayout);
        //Chrono
        chrono = findViewById(R.id.workoutChrono);
        //Define hrSensor
        hrSensor = new hrSensor(this, hr);
        //Set OnClickListeners
        cancel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
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
                if(isWorking)
                    rest();
                else
                    work();
            }
        });
        //Set language and set again button texts
        utils.setLang(this, new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
        cancel.setText(getResources().getString(R.string.cancel));
        endSet.setText(getResources().getString(R.string.finishset));
    }

    private void work(){
        hrSensor.newLap(Constants.STATUS_ACTIVE);
        resetChrono();
        isWorking = true;
        layout.setBackgroundColor(getResources().getColor(R.color.red));
        status.setText(getResources().getString(R.string.work));
    }

    private void rest(){
        hrSensor.newLap(Constants.STATUS_RESTING);
        resetChrono();
        isWorking = false;
        layout.setBackgroundColor(getResources().getColor(R.color.green));
        status.setText(getResources().getString(R.string.rest));
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

    private void resetChrono(){
        chrono.stop();
        chrono.setBase(SystemClock.elapsedRealtime());
        chrono.start();
    }

    private void setupBtnListener(){
        final Handler btnListenerHandler = new Handler();
        final Runnable upBtnPressRunnable = new Runnable() {
            @Override
            public void run() {
                upBtnPress();
            }
        };
        final Runnable downBtnPressRunnable = new Runnable() {
            @Override
            public void run() {
                downBtnPress();
            }
        };
        if(!buttonListener.isListening())
            buttonListener.start(this, new buttonInterface() {
                @Override
                public void onKeyEvent(buttonEvent ButtonEvent) {
                    if((SystemProperties.isPace() || SystemProperties.isVerge()) && ButtonEvent.getKey() == buttonEvent.KEY_CENTER)
                        if(ButtonEvent.isLongPress())
                            btnListenerHandler.post(downBtnPressRunnable);
                        else
                            btnListenerHandler.post(upBtnPressRunnable);
                    else if(SystemProperties.isStratos())
                        if(ButtonEvent.getKey() == buttonEvent.KEY_DOWN)
                            btnListenerHandler.post(downBtnPressRunnable);
                        else if(ButtonEvent.getKey() == buttonEvent.KEY_UP)
                            btnListenerHandler.post(upBtnPressRunnable);
                    //else if(SystemProperties.isStratos3())
                    Log.i("AmazTimer", "Key " + ButtonEvent.getKey() + " has been pressed. isLongClick = " + ButtonEvent.isLongPress());
                }
            });
    }

    private void upBtnPress(){
        endSet.performClick();
    }

    private void downBtnPress(){
        cancel.performLongClick();
    }

}
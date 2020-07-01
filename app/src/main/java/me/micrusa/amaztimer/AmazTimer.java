package me.micrusa.amaztimer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import me.micrusa.amaztimer.TCX.Constants;
import me.micrusa.amaztimer.activities.RepsTimerActivity;
import me.micrusa.amaztimer.activities.SettingsActivity;
import me.micrusa.amaztimer.activities.WorkoutActivity;
import me.micrusa.amaztimer.button.buttonEvent;
import me.micrusa.amaztimer.button.buttonInterface;
import me.micrusa.amaztimer.button.buttonListener;
import me.micrusa.amaztimer.utils.SystemProperties;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.hrSensor;
import me.micrusa.amaztimer.utils.utils;

public class AmazTimer extends Activity {
    //Define items
    private Button plus, plus2, plus3, minus, minus2, minus3, start, cancel;
    private TextView sets, rest, work, time, hr, rSets, status, settingstext, setsText, workText, restText;
    private ConstraintLayout L1, L2;
    private Chronometer chrono;
    //Define timers and timer booleans
    private CountDownTimer workTimer;
    private CountDownTimer restTimer;
    private CountDownTimer prepareTimer;
    private boolean workStarted = false;
    private boolean restStarted = false;
    //Other classes
    private hrSensor hrSensor;
    private buttonListener buttonListener = new buttonListener();
    //Settings
    private boolean isTimerActive;
    private boolean hasResumed = false;

    private final View.OnClickListener plusMinusBtnListener = view -> {
        //Get values from file
        file file = new file(defValues.TIMER_FILE, view.getContext());
        int sets = file.get(defValues.SETTINGS_SETS, defValues.DEF_SETS);
        int workTime = file.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME);
        int restTime = file.get(defValues.SETTINGS_REST, defValues.DEF_RESTTIME);
        //Increase or decrease the value that user clicked
        switch(view.getId()){
            case R.id.plus:
                sets = utils.getUpdatedSets(sets, 1, view.getContext());
                break;
            case R.id.plus2:
                if(new file(defValues.SETTINGS_FILE, view.getContext())
                        .get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE)) {
                    utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
                    break;
                }
                workTime = utils.getUpdatedTime(workTime, 1, view.getContext());
                break;
            case R.id.plus3:
                restTime = utils.getUpdatedTime(restTime, 1, view.getContext());
                break;
            case R.id.minus2:
                sets = utils.getUpdatedSets(sets, -1, view.getContext());
                break;
            case R.id.minus:
                if(new file(defValues.SETTINGS_FILE, view.getContext())
                        .get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE)) {
                    utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
                    break;
                }
                workTime = utils.getUpdatedTime(workTime, -1, view.getContext());
                break;
            case R.id.minus3:
                restTime = utils.getUpdatedTime(restTime, -1, view.getContext());
                break;
            default:
                break;
        }
        setTexts(sets, workTime, restTime);
        utils.pushToFile(file, sets, workTime, restTime);
    };

    private final View.OnLongClickListener plusMinusBtnLongListener = view -> {
        //Get values from file
        file file = new file(defValues.TIMER_FILE, view.getContext());
        int sets = file.get(defValues.SETTINGS_SETS, defValues.DEF_SETS);
        int workTime = file.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME);
        int restTime = file.get(defValues.SETTINGS_REST, defValues.DEF_RESTTIME);
        //Increase or decrease the value that user clicked
        switch(view.getId()){
            case R.id.plus:
                sets = utils.getUpdatedSets(sets, 5, view.getContext());
                break;
            case R.id.plus2:
                if(new file(defValues.SETTINGS_FILE, view.getContext())
                        .get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE)) {
                    utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
                    break;
                }
                workTime = utils.getUpdatedTime(workTime, 60, view.getContext());
                break;
            case R.id.plus3:
                restTime = utils.getUpdatedTime(restTime, 60, view.getContext());
                break;
            case R.id.minus2:
                sets = utils.getUpdatedSets(sets, -5, view.getContext());
                break;
            case R.id.minus:
                if(new file(defValues.SETTINGS_FILE, view.getContext())
                        .get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE)) {
                    utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
                    break;
                }
                workTime = utils.getUpdatedTime(workTime, -60, view.getContext());
                break;
            case R.id.minus3:
                restTime = utils.getUpdatedTime(restTime, -60, view.getContext());
                break;
            default:
                break;
        }
        setTexts(sets, workTime, restTime);
        utils.pushToFile(file, sets, workTime, restTime);
        return true;
    };


    //Much like a fragment, getView returns the content view of the page. You can set up your layout here
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!SystemProperties.isDeviceSupported()){
            Toast.makeText(this, "Device not supported! Killing app...", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(this::finish, 1000);
        }
        this.isTimerActive = false;
        //Set layout depending on device
        setContentView(SystemProperties.isStratos3() || SystemProperties.isVerge() ? R.layout.round_amaztimer : R.layout.amaztimer);
        //Setup items
        this.init();
        //Register buttonListener
        setupBtnListener();
        //Set language to setting's language
        utils.setLang(this, new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
        //Set texts
        this.reloadTexts();
        this.setTimesTexts();
        //Setup hrSensor class
        hrSensor = new hrSensor(this, hr);
        //Plus and minus buttons
        plus.setOnClickListener(plusMinusBtnListener);
        plus2.setOnClickListener(plusMinusBtnListener);
        plus3.setOnClickListener(plusMinusBtnListener);
        minus.setOnClickListener(plusMinusBtnListener);
        minus2.setOnClickListener(plusMinusBtnListener);
        minus3.setOnClickListener(plusMinusBtnListener);
        plus.setOnLongClickListener(plusMinusBtnLongListener);
        plus2.setOnLongClickListener(plusMinusBtnLongListener);
        plus3.setOnLongClickListener(plusMinusBtnLongListener);
        minus.setOnLongClickListener(plusMinusBtnLongListener);
        minus2.setOnLongClickListener(plusMinusBtnLongListener);
        minus3.setOnLongClickListener(plusMinusBtnLongListener);
        //Start button
        start.setOnClickListener(view -> {
            final file file = new file(defValues.TIMER_FILE, view.getContext());
            final file settingsFile = new file(defValues.SETTINGS_FILE, view.getContext());
            if(settingsFile.get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE)){
                Intent intent = new Intent(view.getContext(), RepsTimerActivity.class);
                launchIntent(intent);
                return;
            } else if(settingsFile.get(defValues.SETTINGS_WORKOUTMODE, defValues.DEFAULT_WORKOUTMODE)){
                Intent intent = new Intent(view.getContext(), WorkoutActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchIntent(intent);
                return;
            }
            //Set language to setting's language
            utils.setLang(view.getContext(), new file(defValues.SETTINGS_FILE, view.getContext()).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
            //Move to second layout with timer's stuff and set all texts
            L1.setVisibility(View.GONE);
            L2.setVisibility(View.VISIBLE);
            L2.setBackgroundColor(view.getResources().getColor(R.color.yellow));
            rSets.setText(String.valueOf(file.get(defValues.SETTINGS_SETS, defValues.DEF_SETS)));
            status.setText(view.getResources().getString(R.string.prepare));
            //hrSensor stuff
            setHrState(true, hrSensor, hr);
            //Chrono stuff
            time.setVisibility(View.VISIBLE);
            chrono.setVisibility(View.GONE);
            if (settingsFile.get(defValues.SETTINGS_CHRONOMODE, defValues.DEFAULT_CHRONOMODE)){
                time.setVisibility(View.INVISIBLE);
                chrono.setVisibility(View.VISIBLE);
            }
            //Check if long prepare time option is enabled or disabled
            int prepareTime;
            if(!settingsFile.get(defValues.SETTINGS_ENABLEPREPARE, defValues.DEFAULT_ENABLEPREPARE) || settingsFile.get(defValues.SETTINGS_CHRONOMODE, defValues.DEFAULT_CHRONOMODE)){
                startTimer(view, file.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME), file.get(defValues.SETTINGS_REST, defValues.DEF_RESTTIME));
                chrono.setBase(SystemClock.elapsedRealtime());
                chrono.start();
                return;
            } else if(new file(defValues.SETTINGS_FILE, view.getContext()).get(defValues.SETTINGS_LONGPREPARE, defValues.DEFAULT_LONGPREPARE)){
                prepareTime = defValues.LONG_PREPARETIME;
            }else{
                prepareTime = defValues.SHORT_PREPARETIME;
            }
            prepareTimer = new CountDownTimer(prepareTime, 1000) {
                @Override
                public void onTick(long l) {
                    timerUpdate((int) l / 1000);
                }

                @Override
                public void onFinish() {
                    startTimer(view, file.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME), file.get(defValues.SETTINGS_REST, defValues.DEF_RESTTIME));
                }
            };
            prepareTimer.start();

        });
        //Start long press opens settings
        start.setOnLongClickListener(view -> {
            launchIntent(new Intent(view.getContext(), SettingsActivity.class));
            return true;
        });
        //Cancel button
        //To avoid accidental clicks, just a long click will cancel it
        cancel.setOnLongClickListener(view -> {
            //Return if no timer has started
            //Display start layout
            L1.setVisibility(View.VISIBLE);
            L2.setVisibility(View.GONE);
            //Stop timers
            stopTimers();
            //Stop chrono
            if (new file(defValues.SETTINGS_FILE, getContext()).get(defValues.SETTINGS_CHRONOMODE, defValues.DEFAULT_CHRONOMODE))
                chrono.stop();
            //Unregister hr sensor listener to avoid battery drain
            setHrState(false, hrSensor, hr);
            return true;
        });
        cancel.setOnClickListener(view -> {
            //Send toast
            Toast.makeText(view.getContext(), view.getResources().getString(R.string.canceltoast), Toast.LENGTH_SHORT).show();
        });
    }

    private void setTexts(int iSets, int iWork, int iRest){
        sets.setText(String.valueOf(iSets));
        rest.setText(utils.formatTime(iRest));
        //If reps mode is enabled dont show work time, else set work text
        if(new file(defValues.SETTINGS_FILE, getContext()).get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE)){
            work.setText(getResources().getString(R.string.nullinfo));
        } else {
            work.setText(utils.formatTime(iWork));
        }
    }

    private void setTimesTexts() {
        file file = new file(defValues.TIMER_FILE, this);
        setTexts(file.get(defValues.SETTINGS_SETS, defValues.DEF_SETS),
                file.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME),
                file.get(defValues.SETTINGS_REST, defValues.DEF_RESTTIME));
        //If reps mode is enabled dont show work time
        if(new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE)){
            work.setText(getResources().getString(R.string.nullinfo));
        }
    }

    private void setHrState(boolean state, hrSensor hrSensor, TextView hr) {
        if (state) {
            if (new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_HRSWITCH, defValues.DEFAULT_HRSWITCH)) {
                hrSensor.registerListener();
                if (hr.getVisibility() == View.INVISIBLE) {
                    hr.setVisibility(View.VISIBLE);
                }
            } else if (hr.getVisibility() == View.VISIBLE) {
                hr.setVisibility(View.INVISIBLE);
            }
        } else {
            this.isTimerActive = false;
            if (new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_HRSWITCH, defValues.DEFAULT_HRSWITCH))
                hrSensor.unregisterListener();
        }
    }

    private void init() {
        //Buttons
        plus = findViewById(R.id.plus);
        plus2 = findViewById(R.id.plus2);
        plus3 = findViewById(R.id.plus3);
        minus = findViewById(R.id.minus2);
        minus2 = findViewById(R.id.minus);
        minus3 = findViewById(R.id.minus3);
        start = findViewById(R.id.start);
        cancel = findViewById(R.id.cancel);
        //TextViews
        sets = findViewById(R.id.sets);
        rest = findViewById(R.id.rest);
        work = findViewById(R.id.work);
        time = findViewById(R.id.time);
        hr = findViewById(R.id.heartbeat);
        rSets = findViewById(R.id.remSets);
        status = findViewById(R.id.status);
        settingstext = findViewById(R.id.textView);
        setsText = findViewById(R.id.textView4);
        workText = findViewById(R.id.textView5);
        restText = findViewById(R.id.textView6);
        //Layouts
        L1 = findViewById(R.id.startScreen);
        L2 = findViewById(R.id.timerScreen);
        //Chrono
        chrono = findViewById(R.id.chrono);
    }

    private void reloadTexts() {
        Resources res = getResources();
        start.setText(res.getString(R.string.start));
        cancel.setText(res.getString(R.string.cancel));
        setsText.setText(res.getString(R.string.sets));
        workText.setText(res.getString(R.string.work));
        restText.setText(res.getString(R.string.rest));
        settingstext.setText(res.getString(R.string.startsettings));
    }

    private void timerUpdate(int v) {
        if (!new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_BATTERYSAVING, defValues.DEFAULT_BATTERYSAVING)
                || !new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_CHRONOMODE, defValues.DEFAULT_CHRONOMODE)) {
            time.setText(utils.formatTime(v));
            if (v == 1){
                new Handler().postDelayed(() -> time.setText(utils.formatTime(0)), 950 /*1s wouldn't be visible*/);
            }
        } else if (new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_BATTERYSAVING, defValues.DEFAULT_BATTERYSAVING)){
            if (!time.getText().toString().equals("--:--")) {
                time.setText("--:--");
            }
            //If battery saving && hr measurements enabled update values just once per second
            //to avoid more layout rendering
            if (new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_HRSWITCH, defValues.DEFAULT_HRSWITCH)){
                int latestHr = hrSensor.getLatestValue();
                if(latestHr == 0)
                    hr.setText(getResources().getString(R.string.nullinfo));
                else
                    hr.setText(hrSensor.getLatestValue());
            }
        }
        if (v < 4) {
            utils.vibrate(defValues.SHORT_VIBRATION, this, true);
            if (v == 1){
                new Handler().postDelayed(() -> utils.vibrate(defValues.LONG_VIBRATION, getContext(), true), 950);
            }
        }
    }

    private Context getContext(){
        return this;
    }

    private void stopTimers() {
        if (this.workStarted)
            this.workTimer.cancel();
        else if (this.restStarted)
            this.restTimer.cancel();
        else if(prepareTimer != null)
            prepareTimer.cancel();
    }

    private void startTimer(final View view, final int work, final int rest) {
        this.isTimerActive = true;
        hrSensor.newLap(Constants.STATUS_ACTIVE);
        this.workStarted = true;
        this.restStarted = false;
        status.setText(getResources().getString(R.string.work));
        L2.setBackgroundColor(view.getResources().getColor(R.color.red));
        this.workTimer = new CountDownTimer(work * 1000, 1000) {
            @Override
            public void onTick(long l) {
                timerUpdate((int) l / 1000);
            }

            @Override
            public void onFinish() {
                if (Integer.parseInt(rSets.getText().toString()) != 1) {
                    rSets.setText(String.valueOf(Integer.parseInt(rSets.getText().toString()) - 1));
                    restTimer(view, work, rest);
                } else {
                    //Unregister hrSensor listener and make visible initial screen again
                    setHrState(false, hrSensor, hr);
                    L1.setVisibility(View.VISIBLE);
                    L2.setVisibility(View.GONE);
                }
            }
        };
        this.workTimer.start();
    }

    private void restTimer(final View view, final int work, final int rest) {
        hrSensor.newLap(Constants.STATUS_RESTING);
        this.workStarted = false;
        this.restStarted = true;
        status.setText(getResources().getString(R.string.rest));
        L2.setBackgroundColor(view.getResources().getColor(R.color.green));
        this.restTimer = new CountDownTimer(rest * 1000, 1000) {
            @Override
            public void onTick(long l) {
                timerUpdate((int) l / 1000);
            }

            @Override
            public void onFinish() {
                startTimer(view, work, rest);
            }
        };
        this.restTimer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        buttonListener.stop();
        stopTimers();
    }
    @Override
    public void onStop() {
        buttonListener.stop();
        stopTimers();
        super.onStop();
    }

    @Override
    public void onPause() {
        this.hasResumed = false;
        new Handler().postDelayed(() -> {
            if(hasResumed())
                return;
            buttonListener.stop();
            stopTimers();
        }, 15 * 1000);
        super.onPause();
    }

    private boolean hasResumed(){return this.hasResumed;}

    public void onResume() {
        this.hasResumed = true;
        setupBtnListener();
        super.onResume();
        utils.setLang(this, new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
        this.reloadTexts();
    }

    private void btnPress(int i){
        switch(i){
            case 1:
                if(!this.isTimerActive)
                    start.performClick();
                else
                    cancel.performLongClick();
                break;
            case 2:
                if(!this.isTimerActive)
                    start.performLongClick();
                break;
            default:
                break;
        }
    }

    private void launchIntent(Intent intent){
        buttonListener.stop();
        this.startActivity(intent);
    }

    private void setupBtnListener(){
        //Create a Handler because buttonListener runs in a different thread
        final Handler btnListenerHandler = new Handler();
        final Runnable btnPressRunnable = () -> btnPress(1);
        final Runnable settingsRunnable = () -> btnPress(2);
        buttonListener.start(this, ButtonEvent -> {
            if(ButtonEvent.getKey() == buttonEvent.KEY_DOWN)
                btnListenerHandler.post(btnPressRunnable);
            else if(ButtonEvent.getKey() == buttonEvent.KEY_CENTER)
                btnListenerHandler.post(settingsRunnable);
        });
    }
}

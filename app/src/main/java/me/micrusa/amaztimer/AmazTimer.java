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
    private boolean hasLaunchedActivity = false;
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
        //Set texts and save to file
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
        //Set texts and save to file
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
        // Save Activity variables
        if (SystemProperties.isStratos3())
            setContentView(R.layout.round_amaztimer);
        else
            setContentView(R.layout.amaztimer);
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
            Intent intent = new Intent(view.getContext(), SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchIntent(intent);
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
        } else if (new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_HRSWITCH, defValues.DEFAULT_HRSWITCH)) {
            hrSensor.unregisterListener();
        }
        if(!state)
            this.isTimerActive = false;
    }

    private void init() {
        //Buttons
        plus = this.findViewById(R.id.plus);
        plus2 = this.findViewById(R.id.plus2);
        plus3 = this.findViewById(R.id.plus3);
        minus = this.findViewById(R.id.minus2);
        minus2 = this.findViewById(R.id.minus);
        minus3 = this.findViewById(R.id.minus3);
        start = this.findViewById(R.id.start);
        cancel = this.findViewById(R.id.cancel);
        //TextViews
        sets = this.findViewById(R.id.sets);
        rest = this.findViewById(R.id.rest);
        work = this.findViewById(R.id.work);
        time = this.findViewById(R.id.time);
        hr = this.findViewById(R.id.heartbeat);
        rSets = this.findViewById(R.id.remSets);
        status = this.findViewById(R.id.status);
        settingstext = this.findViewById(R.id.textView);
        setsText = this.findViewById(R.id.textView4);
        workText = this.findViewById(R.id.textView5);
        restText = this.findViewById(R.id.textView6);
        //Layouts
        L1 = this.findViewById(R.id.startScreen);
        L2 = this.findViewById(R.id.timerScreen);
        //Chrono
        chrono = this.findViewById(R.id.chrono);
    }

    private void reloadTexts() {
        Resources res = this.getResources();
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        time.setText(utils.formatTime(0));
                    }
                }, 950);
            }
        } else if (new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_BATTERYSAVING, defValues.DEFAULT_BATTERYSAVING)){
            if (!time.getText().toString().equals("--:--")) {
                time.setText("--:--");
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
        this.isTimerActive = true;
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
        this.hasLaunchedActivity = false;
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
        if(!this.hasLaunchedActivity){
            this.hasLaunchedActivity = true;
            this.startActivity(intent);
        }
    }

    private void setupBtnListener(){
        final Handler btnListenerHandler = new Handler();
        final Runnable btnPressRunnable = () -> btnPress(1);
        final Runnable settingsRunnable = () -> btnPress(2);
        if(!buttonListener.isListening())
            buttonListener.start(this, ButtonEvent -> {
                if((SystemProperties.isPace() || SystemProperties.isVerge()) && ButtonEvent.getKey() == buttonEvent.KEY_CENTER && ButtonEvent.isLongPress())
                    btnListenerHandler.post(btnPressRunnable);
                else if(SystemProperties.isStratos() && SystemProperties.isStratosNewKeys())
                    btnListenerHandler.post(btnPressRunnable);
                else if(SystemProperties.isStratos() && ButtonEvent.getKey() == buttonEvent.KEY_DOWN)
                    btnListenerHandler.post(btnPressRunnable);
                else if(SystemProperties.isStratos() && ButtonEvent.getKey() == buttonEvent.KEY_CENTER)
                    btnListenerHandler.post(settingsRunnable);
                //else if(SystemProperties.isStratos3())
                Log.i("AmazTimer", "Key " + ButtonEvent.getKey() + " has been pressed. isLongClick = " + ButtonEvent.isLongPress());
            });
    }
}

package me.micrusa.amaztimer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import me.micrusa.amaztimer.activities.SettingsActivity;
import me.micrusa.amaztimer.button.buttonEvent;
import me.micrusa.amaztimer.button.buttonListener;
import me.micrusa.amaztimer.utils.SystemProperties;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.hrSensor;
import me.micrusa.amaztimer.utils.utils;

public class AmazTimer extends Activity {
    //Define items
    private Button plus, plus2, plus3, minus, minus2, minus3, start;
    private TextView sets, rest, work, settingstext, setsText, workText, restText;
    //Other classes
    private buttonListener buttonListener = new buttonListener();
    //Settings
    private boolean hasResumed = false;

    private final View.OnClickListener plusMinusBtnListener = view -> {
        //Get values from file
        file file = new file(defValues.TIMER_FILE);
        int sets = file.get(defValues.SETTINGS_SETS, defValues.DEF_SETS);
        int workTime = file.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME);
        int restTime = file.get(defValues.SETTINGS_REST, defValues.DEF_RESTTIME);
        //Increase or decrease the value that user clicked
        switch(view.getId()){
            case R.id.plus:
                sets = utils.getUpdatedSets(sets, 1, view.getContext());
                break;
            case R.id.plus2:
                if(new file(defValues.SETTINGS_FILE)
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
                if(new file(defValues.SETTINGS_FILE)
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
        file file = new file(defValues.TIMER_FILE);
        int sets = file.get(defValues.SETTINGS_SETS, defValues.DEF_SETS);
        int workTime = file.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME);
        int restTime = file.get(defValues.SETTINGS_REST, defValues.DEF_RESTTIME);
        //Increase or decrease the value that user clicked
        switch(view.getId()){
            case R.id.plus:
                sets = utils.getUpdatedSets(sets, 5, view.getContext());
                break;
            case R.id.plus2:
                if(new file(defValues.SETTINGS_FILE)
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
                if(new file(defValues.SETTINGS_FILE)
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
        //Set layout depending on device
        setContentView(SystemProperties.isStratos3() || SystemProperties.isVerge() ? R.layout.round_amaztimer : R.layout.amaztimer);
        //Setup items
        this.init();
        //Register buttonListener
        setupBtnListener();
        //Set language to setting's language
        utils.setLang(this, new file(defValues.SETTINGS_FILE).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
        //Set texts
        this.reloadTexts();
        this.setTimesTexts();
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
            launchIntent(new Intent(view.getContext(), TimerActivity.class));
        });
        //Start long press opens settings
        start.setOnLongClickListener(view -> {
            launchIntent(new Intent(view.getContext(), SettingsActivity.class));
            return true;
        });
    }

    private void setTexts(int iSets, int iWork, int iRest){
        sets.setText(String.valueOf(iSets));
        rest.setText(utils.formatTime(iRest));
        //If reps mode is enabled dont show work time, else set work text
        if(new file(defValues.SETTINGS_FILE).get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE)){
            work.setText(getResources().getString(R.string.nullinfo));
        } else {
            work.setText(utils.formatTime(iWork));
        }
    }

    private void setTimesTexts() {
        file file = new file(defValues.TIMER_FILE);
        setTexts(file.get(defValues.SETTINGS_SETS, defValues.DEF_SETS),
                file.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME),
                file.get(defValues.SETTINGS_REST, defValues.DEF_RESTTIME));
        //If reps mode is enabled dont show work time
        if(new file(defValues.SETTINGS_FILE).get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE)){
            work.setText(getResources().getString(R.string.nullinfo));
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
        //TextViews
        sets = findViewById(R.id.sets);
        rest = findViewById(R.id.rest);
        work = findViewById(R.id.work);
        settingstext = findViewById(R.id.textView);
        setsText = findViewById(R.id.textView4);
        workText = findViewById(R.id.textView5);
        restText = findViewById(R.id.textView6);
    }

    private void reloadTexts() {
        Resources res = getResources();
        start.setText(res.getString(R.string.start));
        setsText.setText(res.getString(R.string.sets));
        workText.setText(res.getString(R.string.work));
        restText.setText(res.getString(R.string.rest));
        settingstext.setText(res.getString(R.string.startsettings));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        buttonListener.stop();
    }
    @Override
    public void onStop() {
        buttonListener.stop();
        super.onStop();
    }

    @Override
    public void onPause() {
        this.hasResumed = false;
        new Handler().postDelayed(() -> {
            if(hasResumed())
                return;
            buttonListener.stop();
        }, 15 * 1000);
        super.onPause();
    }

    private boolean hasResumed(){return this.hasResumed;}

    public void onResume() {
        this.hasResumed = true;
        setupBtnListener();
        super.onResume();
        utils.setLang(this, new file(defValues.SETTINGS_FILE).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
        this.reloadTexts();
    }

    private void btnPress(int i){
        switch(i){
            case 1:
                start.performClick();
                break;
            case 2:
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

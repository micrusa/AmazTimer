package me.micrusa.amaztimer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import me.micrusa.amaztimer.TCX.Constants;
import me.micrusa.amaztimer.activities.RepsTimerActivity;
import me.micrusa.amaztimer.activities.SettingsActivity;
import me.micrusa.amaztimer.activities.WorkoutActivity;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.hrSensor;
import me.micrusa.amaztimer.utils.utils;

public class AmazTimer extends Activity {

    //These get set up later
    private View mView;
    private boolean mHasActive = false;
    //Define items
    private Button plus, plus2, plus3, minus, minus2, minus3, start, cancel;
    private TextView sets, rest, work, time, hr, rSets, status, settingstext, setsText, workText, restText;
    private ConstraintLayout L1, L2;
    //Define timers and timer booleans
    private CountDownTimer workTimer;
    private CountDownTimer restTimer;
    private boolean workStarted = false;
    private boolean restStarted = false;
    //Classes
    private final me.micrusa.amaztimer.utils.utils utils = new utils();
    private hrSensor hrSensor;
    //Default values
    private final me.micrusa.amaztimer.defValues defValues = new defValues();
    //Settings
    private boolean batterySaving;
    private boolean hrEnabled;
    private boolean longPrepare;

    private final View.OnClickListener plusMinusBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Get values from file
            file file = new file(defValues.TIMER_FILE, view.getContext());
            int sets = file.get(defValues.SETTINGS_SETS, defValues.DEF_SETS);
            int workTime = file.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME);
            int restTime = file.get(defValues.SETTINGS_REST, defValues.DEF_RESTTIME);
            //Increase or decrease the value that user clicked
            switch(view.getId()){
                case R.id.plus:
                    sets++;
                    break;
                case R.id.plus2:
                    if(new file(defValues.SETTINGS_FILE, view.getContext())
                            .get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE)) {
                        utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
                        break;
                    }
                    workTime++;
                    break;
                case R.id.plus3:
                    restTime++;
                    break;
                case R.id.minus2:
                    sets--;
                    break;
                case R.id.minus:
                    if(new file(defValues.SETTINGS_FILE, view.getContext())
                            .get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE)) {
                        utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
                        break;
                    }
                    workTime--;
                    break;
                case R.id.minus3:
                    restTime--;
                    break;
                default:
                    break;
            }
            //If value is over max or under min, set max/min value and vibrate
            if(sets > defValues.MAX_SETS) {
                sets = defValues.MAX_SETS;
                utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
            }else if(sets < defValues.MIN_SETS){
                sets = defValues.MIN_SETS;
                utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
            }else if(workTime > defValues.MAX_TIME){
                workTime = defValues.MAX_TIME;
                utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
            }else if(workTime < defValues.MIN_TIME){
                workTime = defValues.MIN_TIME;
                utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
            }else if(restTime > defValues.MAX_TIME){
                restTime = defValues.MAX_TIME;
                utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
            }else if(restTime < defValues.MIN_TIME){
                restTime = defValues.MIN_TIME;
                utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
            }
            //Set texts and save to file
            setTexts(sets, workTime, restTime);
            utils.pushToFile(file, sets, workTime, restTime);
        }
    };

    private final View.OnLongClickListener plusMinusBtnLongListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            //Get values from file
            file file = new file(defValues.TIMER_FILE, view.getContext());
            int sets = file.get(defValues.SETTINGS_SETS, defValues.DEF_SETS);
            int workTime = file.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME);
            int restTime = file.get(defValues.SETTINGS_REST, defValues.DEF_RESTTIME);
            //Increase or decrease the value that user clicked
            switch(view.getId()){
                case R.id.plus:
                    sets = sets + 60;
                    break;
                case R.id.plus2:
                    if(new file(defValues.SETTINGS_FILE, view.getContext())
                            .get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE)) {
                        utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
                        break;
                    }
                    workTime = workTime + 60;
                    break;
                case R.id.plus3:
                    restTime = restTime + 60;
                    break;
                case R.id.minus2:
                    sets = sets - 60;
                    break;
                case R.id.minus:
                    if(new file(defValues.SETTINGS_FILE, view.getContext())
                            .get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE)) {
                        utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
                        break;
                    }
                    workTime = workTime - 60;
                    break;
                case R.id.minus3:
                    restTime = restTime - 60;
                    break;
                default:
                    break;
            }
            //If value is over max or under min, set max/min value and vibrate
            if(sets > defValues.MAX_SETS) {
                sets = defValues.MAX_SETS;
                utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
            }else if(sets < defValues.MIN_SETS){
                sets = defValues.MIN_SETS;
                utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
            }else if(workTime > defValues.MAX_TIME){
                workTime = defValues.MAX_TIME;
                utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
            }else if(workTime < defValues.MIN_TIME){
                workTime = defValues.MIN_TIME;
                utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
            }else if(restTime > defValues.MAX_TIME){
                restTime = defValues.MAX_TIME;
                utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
            }else if(restTime < defValues.MIN_TIME){
                restTime = defValues.MIN_TIME;
                utils.vibrate(defValues.SHORT_VIBRATION, view.getContext());
            }
            //Set texts and save to file
            setTexts(sets, workTime, restTime);
            utils.pushToFile(file, sets, workTime, restTime);
            return true;
        }
    };


    //Much like a fragment, getView returns the content view of the page. You can set up your layout here
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Save Activity variables
        setContentView(R.layout.amaztimer);
        this.mView = this.findViewById(android.R.id.content);
        //Setup items
        this.init();
        //Set language to setting's language
        utils.setLang(this, new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
        //Set texts
        this.reloadTexts();
        this.setTimesTexts();
        //Check if the view is already inflated (reloading)
        if ((!this.mHasActive) && (this.mView != null)) {
            //It is, simply refresh
            refreshView();
        }
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
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final file file = new file(defValues.TIMER_FILE, view.getContext());
                final file settingsFile = new file(defValues.SETTINGS_FILE, view.getContext());
                if(settingsFile.get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE)){
                    Intent intent = new Intent(view.getContext(), RepsTimerActivity.class);
                    view.getContext().startActivity(intent);
                    return;
                } else if(settingsFile.get(defValues.SETTINGS_WORKOUTMODE, defValues.DEFAULT_WORKOUTMODE)){
                    Intent intent = new Intent(view.getContext(), WorkoutActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
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
                //Get battery saving settings
                getSettings();
                //hrSensor stuff
                setHrState(true, hrSensor, hr);
                //Check if long prepare time option is enabled or disabled
                int prepareTime;
                if(isLongPrepare()){
                    prepareTime = defValues.LONG_PREPARETIME;
                }else{
                    prepareTime = defValues.SHORT_PREPARETIME;
                }
                final CountDownTimer PrepareTimer = new CountDownTimer(prepareTime, 1000) {
                    @Override
                    public void onTick(long l) {
                        timerUpdate((int) l / 1000);
                    }

                    @Override
                    public void onFinish() {
                        startTimer(view, view.getResources().getString(R.string.work), view.getResources().getString(R.string.rest), file.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME), file.get(defValues.SETTINGS_REST, defValues.DEF_RESTTIME));
                    }
                };
                PrepareTimer.start();

            }
        });
        //Start long press opens settings
        start.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(view.getContext(), SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
                return true;
            }
        });
        //Cancel button
        //To avoid accidental clicks, just a long click will cancel it
        cancel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Return if no timer has started
                if(!timersStarted()){
                    Toast.makeText(view.getContext(), view.getResources().getString(R.string.waitforstart), Toast.LENGTH_SHORT).show();
                    return true;
                }
                //Display start layout
                L1.setVisibility(View.VISIBLE);
                L2.setVisibility(View.GONE);
                //Stop timers
                stopTimers();
                //Unregister hr sensor listener to avoid battery drain
                setHrState(false, hrSensor, hr);
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
    }

    private void setTexts(int iSets, int iWork, int iRest){
        sets.setText(String.valueOf(iSets));
        rest.setText(utils.formatTime(iRest));
        //If reps mode is enabled dont show work time, else set work text
        if(new file(defValues.SETTINGS_FILE, this.mView.getContext()).get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE)){
            work.setText(this.mView.getResources().getString(R.string.nullinfo));
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

    private void getSettings() {
        file file = new file(defValues.SETTINGS_FILE, this);
        this.batterySaving = file.get(defValues.SETTINGS_BATTERYSAVING, defValues.DEFAULT_BATTERYSAVING);
        this.hrEnabled = file.get(defValues.SETTINGS_HRSWITCH, defValues.DEFAULT_HRSWITCH);
        this.longPrepare = file.get(defValues.SETTINGS_LONGPREPARE, defValues.DEFAULT_LONGPREPARE);
    }

    private boolean isLongPrepare(){
        return this.longPrepare;
    }

    private void setHrState(boolean state, hrSensor hrSensor, TextView hr) {
        if (state) {
            if (this.hrEnabled) {
                hrSensor.registerListener();
                if (hr.getVisibility() == View.INVISIBLE) {
                    hr.setVisibility(View.VISIBLE);
                }
            } else if (hr.getVisibility() == View.VISIBLE) {
                hr.setVisibility(View.INVISIBLE);
            }
        } else if (this.hrEnabled) {
            hrSensor.unregisterListener();
        }
    }

    private boolean timersStarted(){
        return this.restStarted || this.workStarted;
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
    }

    private void reloadTexts() {
        Resources res = this.getResources();
        this.init();
        start.setText(res.getString(R.string.start));
        cancel.setText(res.getString(R.string.cancel));
        setsText.setText(res.getString(R.string.sets));
        workText.setText(res.getString(R.string.work));
        restText.setText(res.getString(R.string.rest));
        settingstext.setText(res.getString(R.string.startsettings));
    }

    private void timerUpdate(int v) {
        this.init();
        if (!this.batterySaving) {
            time.setText(utils.formatTime(v));
        } else {
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
            if (v == 1)
                utils.vibrate(defValues.LONG_VIBRATION, this.mView.getContext());
            else
                utils.vibrate(defValues.SHORT_VIBRATION, this.mView.getContext());
        }
    }

    private void stopTimers() {
        if (this.workStarted) {
            this.workTimer.cancel();
        }
        if (this.restStarted) {
            this.restTimer.cancel();
        }
    }

    private void startTimer(final View view, final String sWork, final String sRest, final int work, final int rest) {
        hrSensor.newLap(Constants.STATUS_ACTIVE);
        this.init();
        this.workStarted = true;
        this.restStarted = false;
        status.setText(sWork);
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
                    restTimer(view, sWork, sRest, work, rest);
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

    private void restTimer(final View view, final String sWork, final String sRest, final int work, final int rest) {
        hrSensor.newLap(Constants.STATUS_RESTING);
        this.init();
        this.workStarted = false;
        this.restStarted = true;
        status.setText(sRest);
        L2.setBackgroundColor(view.getResources().getColor(R.color.green));
        this.restTimer = new CountDownTimer(rest * 1000, 1000) {
            @Override
            public void onTick(long l) {
                timerUpdate((int) l / 1000);
            }

            @Override
            public void onFinish() {
                startTimer(view, sWork, sRest, work, rest);
            }
        };
        this.restTimer.start();
    }

    private void refreshView() {
        //Set language to setting's language
        utils.setLang(this, new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
        //Set texts
        this.reloadTexts();
        this.setTimesTexts();
    }


    //Called when the page is destroyed completely (in app mode). Same as the onDestroy method of an activity
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    //Called when the page is shown again (in app mode)
    @Override
    public void onResume() {
        super.onResume();
        //Set language to setting's language
        utils.setLang(this, new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
        //Set texts
        this.reloadTexts();
        //Check if view already loaded
        if ((!this.mHasActive) && (this.mView != null)) {
            //It is, simply refresh
            this.mHasActive = true;
            refreshView();
        }
        //Store active state
        this.mHasActive = true;
    }

    //Called when the page is stopped (in app mode)
    @Override
    public void onStop() {
        super.onStop();
        this.mHasActive = false;
    }
}

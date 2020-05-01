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

import me.micrusa.amaztimer.activities.SettingsActivity;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.hrSensor;
import me.micrusa.amaztimer.utils.utils;

public class AmazTimer extends Activity {

    private static final String TAG = "me.micrusa.amaztimer.AmazTimer";
    //These get set up later
    private View mView;
    private boolean mHasActive = false;
    private int v;
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
    private me.micrusa.amaztimer.utils.utils utils = new utils();
    //Default values
    private me.micrusa.amaztimer.defValues defValues = new defValues();
    //Settings
    private boolean batterySaving;
    private boolean hrEnabled;
    private boolean longPrepare;

    private View.OnClickListener plusMinusBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Get values from file
            file file = new file(defValues.timerFile, view.getContext());
            int sets = file.get(defValues.sSets, defValues.defSets);
            int workTime = file.get(defValues.sWork, defValues.defWorkTime);
            int restTime = file.get(defValues.sRest, defValues.defRestTime);
            //Increase or decrease the value that user clicked
            switch(view.getId()){
                case R.id.plus:
                    sets++;
                    break;
                case R.id.plus2:
                    workTime++;
                    break;
                case R.id.plus3:
                    restTime++;
                    break;
                case R.id.minus2:
                    sets--;
                    break;
                case R.id.minus:
                    workTime--;
                    break;
                case R.id.minus3:
                    restTime--;
                    break;
                default:
                    break;
            }
            //If value is over max or under min, set max/min value and vibrate
            if(sets > defValues.maxSets) {
                sets = defValues.maxSets;
                utils.vibrate(defValues.sVibration, view.getContext());
            }else if(sets < defValues.minSets){
                sets = defValues.minSets;
                utils.vibrate(defValues.sVibration, view.getContext());
            }else if(workTime > defValues.maxTime){
                workTime = defValues.maxTime;
                utils.vibrate(defValues.sVibration, view.getContext());
            }else if(workTime < defValues.minTime){
                workTime = defValues.minTime;
                utils.vibrate(defValues.sVibration, view.getContext());
            }else if(restTime > defValues.maxTime){
                restTime = defValues.maxTime;
                utils.vibrate(defValues.sVibration, view.getContext());
            }else if(restTime < defValues.minTime){
                restTime = defValues.minTime;
                utils.vibrate(defValues.sVibration, view.getContext());
            }
            //Set texts and save to file
            setTexts(sets, workTime, restTime);
            utils.pushToFile(file, sets, workTime, restTime);
        }
    };


    //Much like a fragment, getView returns the content view of the page. You can set up your layout here
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Save Activity variables
        setContentView(R.layout.amaztimer);
        this.mView = this.findViewById(android.R.id.content);
        final file file = new file(defValues.timerFile, this);
        //Setup items
        this.init();
        //Set language to setting's language
        utils.setLang(this, new file(defValues.settingsFile, this).get(defValues.sLang, defValues.LangDefault));
        //Set texts
        this.reloadTexts();
        this.setTimesTexts();
        //Check if the view is already inflated (reloading)
        if ((!this.mHasActive) && (this.mView != null)) {
            //It is, simply refresh
            refreshView();
        }
        //Setup hrSensor class
        final hrSensor hrSensor = new hrSensor(this, hr);
        //Plus and minus buttons
        plus.setOnClickListener(plusMinusBtnListener);
        plus2.setOnClickListener(plusMinusBtnListener);
        plus3.setOnClickListener(plusMinusBtnListener);
        minus.setOnClickListener(plusMinusBtnListener);
        minus2.setOnClickListener(plusMinusBtnListener);
        minus3.setOnClickListener(plusMinusBtnListener);
        //Start button
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final file file = new file(defValues.timerFile, view.getContext());
                //Set language to setting's language
                utils.setLang(view.getContext(), new file(defValues.settingsFile, view.getContext()).get(defValues.sLang, defValues.LangDefault));
                //Move to second layout with timer's stuff and set all texts
                L1.setVisibility(View.GONE);
                L2.setVisibility(View.VISIBLE);
                L2.setBackgroundColor(view.getResources().getColor(R.color.yellow));
                rSets.setText(String.valueOf(file.get(defValues.sSets, defValues.defSets)));
                status.setText(view.getResources().getString(R.string.prepare));
                //Get battery saving settings
                getSettings();
                //hrSensor stuff
                setHrState(true, hrSensor, hr);
                //Check if long prepare time option is enabled or disabled
                int prepareTime;
                if(isLongPrepare()){
                    prepareTime = defValues.lPrepare;
                }else{
                    prepareTime = defValues.sPrepare;
                }
                final CountDownTimer PrepareTimer = new CountDownTimer(prepareTime, 1000) {
                    @Override
                    public void onTick(long l) {
                        timerUpdate((int) l / 1000);
                    }

                    @Override
                    public void onFinish() {
                        startTimer(view, view.getResources().getString(R.string.work), view.getResources().getString(R.string.rest), file.get(defValues.sWork, defValues.defWorkTime), file.get(defValues.sRest, defValues.defRestTime), hrSensor);
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
        work.setText(utils.formatTime(iWork));
        rest.setText(utils.formatTime(iRest));
    }

    private void setTimesTexts() {
        file file = new file(defValues.timerFile, this);
        sets.setText(String.valueOf(file.get(defValues.sSets, defValues.defSets)));
        work.setText(utils.formatTime(file.get(defValues.sWork, defValues.defWorkTime)));
        rest.setText(utils.formatTime(file.get(defValues.sRest, defValues.defRestTime)));
    }

    private void getSettings() {
        file file = new file(defValues.settingsFile, this);
        this.batterySaving = file.get(defValues.sBatterySaving, defValues.defBatterySaving);
        this.hrEnabled = file.get(defValues.sHrSwitch, defValues.defHrSwitch);
        this.longPrepare = file.get(defValues.sLongPrepare, defValues.defLongprep);
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
        } else if (!time.getText().toString().equals("--:--")) {
            time.setText("--:--");
        }
        if (v < 4) {
            if (v == 1) {
                utils.vibrate(defValues.lVibration, this);
            }
            if (v != 1) {
                utils.vibrate(defValues.sVibration, this);
            }
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

    private void startTimer(final View view, final String sWork, final String sRest, final int work, final int rest, final hrSensor hrSensor) {
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
                    restTimer(view, sWork, sRest, work, rest, hrSensor);
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

    private void restTimer(final View view, final String sWork, final String sRest, final int work, final int rest, final hrSensor hrSensor) {
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
                startTimer(view, sWork, sRest, work, rest, hrSensor);
            }
        };
        this.restTimer.start();
    }

    private void refreshView() {
        //Set language to setting's language
        utils.setLang(this, new file(defValues.settingsFile, this).get(defValues.sLang, defValues.LangDefault));
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
        utils.setLang(this, new file(defValues.settingsFile, this).get(defValues.sLang, defValues.LangDefault));
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

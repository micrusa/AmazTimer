package me.micrusa.amaztimer;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import clc.sliteplugin.flowboard.AbstractPlugin;
import clc.sliteplugin.flowboard.ISpringBoardHostStub;
import me.micrusa.amaztimer.activities.RepsTimerActivity;
import me.micrusa.amaztimer.activities.SettingsActivity;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.hrSensor;
import me.micrusa.amaztimer.utils.utils;

@SuppressWarnings("WeakerAccess")
public class widget extends AbstractPlugin {

    private static final String TAG = "AmazTimer";
    //As AbstractPlugin is not an Activity or Service, we can't just use "this" as a context or getApplicationContext, so Context is global to allow easier access
    private Context mContext;
    //These get set up later
    private View mView;
    private boolean mHasActive = false;
    private ISpringBoardHostStub mHost = null;
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
    private final defValues defValues = new defValues();
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

    //Much like a fragment, getView returns the content view of the page. You can set up your layout here
    @SuppressLint("InflateParams")
    @Override
    public View getView(final Context paramContext) {
        //Setup view
        //Log.d(TAG, "getView()" + paramContext.getPackageName() + " AmazTimer");
        this.mContext = paramContext;
        this.mView = LayoutInflater.from(paramContext).inflate(R.layout.amaztimer, null);
        //Setup items
        this.init();
        //Set language to setting's language
        utils.setLang(this.mView.getContext(), new file(defValues.SETTINGS_FILE, this.mView.getContext()).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
        //Set texts
        this.reloadTexts();
        //Setup hrSensor class
        hrSensor = new hrSensor(this.mView.getContext(), hr);
        setTimesTexts();
        //Plus and minus buttons
        plus.setOnClickListener(plusMinusBtnListener);
        plus2.setOnClickListener(plusMinusBtnListener);
        plus3.setOnClickListener(plusMinusBtnListener);
        minus.setOnClickListener(plusMinusBtnListener);
        minus2.setOnClickListener(plusMinusBtnListener);
        minus3.setOnClickListener(plusMinusBtnListener);
        //Start button
        start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                final file file = new file(defValues.TIMER_FILE, view.getContext());
                final file settingsFile = new file(defValues.SETTINGS_FILE, view.getContext());
                if(settingsFile.get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE)){
                    Intent intent = new Intent(view.getContext(), RepsTimerActivity.class);
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
                        startTimer(view, view.getResources().getString(R.string.work), view.getResources().getString(R.string.rest), file.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME), file.get(defValues.SETTINGS_REST, defValues.DEF_RESTTIME), hrSensor);
                    }
                };
                PrepareTimer.start();

            }
        });
        //Start long press opens settings
        start.setOnLongClickListener(new OnLongClickListener() {
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
        cancel.setOnLongClickListener(new OnLongClickListener() {
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
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send toast
                Toast.makeText(view.getContext(), view.getResources().getString(R.string.canceltoast), Toast.LENGTH_SHORT).show();
            }
        });
        return this.mView;
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
        file file = new file(defValues.TIMER_FILE, this.mView.getContext());
        setTexts(file.get(defValues.SETTINGS_SETS, defValues.DEF_SETS),
                file.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME),
                file.get(defValues.SETTINGS_REST, defValues.DEF_RESTTIME));
        //If reps mode is enabled dont show work time
        if(new file(defValues.SETTINGS_FILE, this.mView.getContext()).get(defValues.SETTINGS_REPSMODE, defValues.DEFAULT_REPSMODE)){
            work.setText(this.mView.getResources().getString(R.string.nullinfo));
        }
    }

    private void getSettings() {
        file file = new file(defValues.SETTINGS_FILE, this.mView.getContext());
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
        plus = this.mView.findViewById(R.id.plus);
        plus2 = this.mView.findViewById(R.id.plus2);
        plus3 = this.mView.findViewById(R.id.plus3);
        minus = this.mView.findViewById(R.id.minus2);
        minus2 = this.mView.findViewById(R.id.minus);
        minus3 = this.mView.findViewById(R.id.minus3);
        start = this.mView.findViewById(R.id.start);
        cancel = this.mView.findViewById(R.id.cancel);
        //TextViews
        sets = this.mView.findViewById(R.id.sets);
        rest = this.mView.findViewById(R.id.rest);
        work = this.mView.findViewById(R.id.work);
        time = this.mView.findViewById(R.id.time);
        hr = this.mView.findViewById(R.id.heartbeat);
        rSets = this.mView.findViewById(R.id.remSets);
        status = this.mView.findViewById(R.id.status);
        settingstext = this.mView.findViewById(R.id.textView);
        setsText = this.mView.findViewById(R.id.textView4);
        workText = this.mView.findViewById(R.id.textView5);
        restText = this.mView.findViewById(R.id.textView6);
        //Layouts
        L1 = this.mView.findViewById(R.id.startScreen);
        L2 = this.mView.findViewById(R.id.timerScreen);
    }

    private void reloadTexts() {
        Resources res = this.mView.getContext().getResources();
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
            int latestHr = hrSensor.getLatestValue();
            if(latestHr == 0)
                hr.setText(this.mView.getContext().getResources().getString(R.string.nullinfo));
            else
                hr.setText(hrSensor.getLatestValue());
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

    //Return the icon for this page, used when the page is disabled in the app list. In this case, the launcher icon is used
    @Override
    public Bitmap getWidgetIcon(Context paramContext) {
        return ((BitmapDrawable) this.mContext.getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
    }

    //Return the launcher intent for this page. This might be used for the launcher as well when the page is disabled?
    @Override
    public Intent getWidgetIntent() {
        return new Intent();
    }

    //Return the title for this page, used when the page is disabled in the app list. In this case, the app name is used
    @Override
    public String getWidgetTitle(Context paramContext) {
        return "AmazTimer";
    }

    //Called when the page is shown
    @Override
    public void onActive(Bundle paramBundle) {
        super.onActive(paramBundle);
        //Set language to setting's language
        utils.setLang(this.mView.getContext(), new file(defValues.SETTINGS_FILE, this.mView.getContext()).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
        //Set texts
        this.reloadTexts();
        this.setTimesTexts();
        //Check if the view is already inflated (reloading)
        if ((!this.mHasActive) && (this.mView != null)) {
            //It is, simply refresh
            refreshView();
        }
        //Store active state
        this.mHasActive = true;
    }

    private void refreshView() {
        //Set language to setting's language
        utils.setLang(this.mView.getContext(), new file(defValues.SETTINGS_FILE, this.mView.getContext()).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
        //Set texts
        this.reloadTexts();
        this.setTimesTexts();
    }

    //Returns the springboard host
    @SuppressWarnings("unused")
    public ISpringBoardHostStub getHost() {
        return this.mHost;
    }

    //Called when the page is loading and being bound to the host
    @Override
    public void onBindHost(ISpringBoardHostStub paramISpringBoardHostStub) {
        Log.d(TAG, "onBindHost");
        //Store host
        this.mHost = paramISpringBoardHostStub;
    }

    //Called when the page becomes inactive (the user has scrolled away)
    @Override
    public void onInactive(Bundle paramBundle) {
        super.onInactive(paramBundle);
        //Store active state
        this.mHasActive = false;
    }

    //Called when the page is paused (in app mode)
    @Override
    public void onPause() {
        super.onPause();
        this.mHasActive = false;
    }

    //Not sure what this does, can't find it being used anywhere. Best leave it alone
    @Override
    public void onReceiveDataFromProvider(int paramInt, Bundle paramBundle) {
        super.onReceiveDataFromProvider(paramInt, paramBundle);
    }

    //Called when the page is shown again (in app mode)
    @Override
    public void onResume() {
        super.onResume();
        //Set language to setting's language
        utils.setLang(this.mView.getContext(), new file(defValues.SETTINGS_FILE, this.mView.getContext()).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
        //Set texts
        this.reloadTexts();
        this.setTimesTexts();
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
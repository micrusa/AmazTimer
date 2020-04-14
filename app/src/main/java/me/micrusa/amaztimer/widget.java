package me.micrusa.amaztimer;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import clc.sliteplugin.flowboard.AbstractPlugin;
import clc.sliteplugin.flowboard.ISpringBoardHostStub;
import me.micrusa.app.amazwidgets.R;

public class widget extends AbstractPlugin {

    private static final String TAG = "AmazTimer";
    //As AbstractPlugin is not an Activity or Service, we can't just use "this" as a context or getApplicationContext, so Context is global to allow easier access
    private Context mContext;
    //These get set up later
    private View mView;
    private boolean mHasActive = false;
    private ISpringBoardHostStub mHost = null;
    private int v;
    //Define items
    private Button plus, plus2, plus3, minus, minus2, minus3, start, cancel;
    private TextView sets, rest, work, time, hr, rSets, status;
    private ConstraintLayout L1, L2;
    //Define timers and timer booleans
    private CountDownTimer workTimer;
    private CountDownTimer restTimer;
    private boolean workStarted = false;
    private boolean restStarted = false;
    //Classes
    private utils utils = new utils();
    //Default values
    private defValues defValues = new defValues();
    //Settings
    private boolean batterySaving;
    private boolean hrEnabled;


    //Much like a fragment, getView returns the content view of the page. You can set up your layout here
    @Override
    public View getView(final Context paramContext) {
        //Setup view
        Log.d(TAG, "getView()" + paramContext.getPackageName() + " AmazTimer");
        this.mContext = paramContext;
        this.mView = LayoutInflater.from(paramContext).inflate(R.layout.amaztimer, null);
        final file file = new file(defValues.timerFile, this.mView.getContext());
        //Setup items
        this.init();
        //Setup hrSensor class
        final hrSensor hrSensor = new hrSensor(this.mView.getContext(), hr);
        //Text default values
        sets.setText(String.valueOf(file.get(defValues.sSets, defValues.defSets)));
        work.setText(utils.sToMinS(file.get(defValues.sWork, defValues.defWorkTime)));
        rest.setText(utils.sToMinS(file.get(defValues.sRest, defValues.defRestTime)));
        //Plus and minus buttons
        plus.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                v = file.get(defValues.sSets, defValues.defSets) + 1;
                if(v>defValues.maxSets){
                    v = defValues.maxSets;
                    utils.vibrate(defValues.sVibration, view.getContext());
                }
                file.set(defValues.sSets, v);
                sets.setText(String.valueOf(v));
            }
        });
        minus.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                v = file.get(defValues.sSets, defValues.defSets) - 1;
                if(v<defValues.minSets){
                    v = defValues.minSets;
                    utils.vibrate(defValues.sVibration, view.getContext());
                }
                file.set(defValues.sSets, v);
                sets.setText(String.valueOf(v));
            }
        });
        //Work
        plus2.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                v = file.get(defValues.sWork, defValues.defWorkTime) + 1;
                if(v>defValues.maxTime){
                    v = defValues.maxTime;
                    utils.vibrate(defValues.sVibration, view.getContext());
                }
                file.set(defValues.sWork, v);
                work.setText(utils.sToMinS(v));
            }
        });
        minus2.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                v = file.get(defValues.sWork, defValues.defWorkTime) - 1;
                if(v<defValues.minTime){
                    v = defValues.minTime;
                    utils.vibrate(defValues.sVibration, view.getContext());
                }
                file.set(defValues.sWork, v);
                work.setText(utils.sToMinS(v));
            }
        });
        //Rest
        plus3.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                v = file.get(defValues.sRest, defValues.defRestTime) + 1;
                if(v>defValues.maxTime){
                    v = defValues.maxTime;
                    utils.vibrate(defValues.sVibration, view.getContext());
                }
                file.set(defValues.sRest, v);
                rest.setText(utils.sToMinS(v));
            }
        });
        minus3.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                v = file.get(defValues.sRest, defValues.defRestTime) - 1;
                if(v<defValues.minTime){
                    v = defValues.minTime;
                    utils.vibrate(defValues.sVibration, view.getContext());
                }
                file.set(defValues.sRest, v);
                rest.setText(utils.sToMinS(v));
            }
        });

        //Start button
        start.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(final View view) {
                //Move to second layout with timer's stuff and set all texts
                L1.setVisibility(View.GONE);
                L2.setVisibility(View.VISIBLE);
                L2.setBackgroundColor(view.getResources().getColor(R.color.yellow));
                rSets.setText(String.valueOf(file.get(defValues.sSets, defValues.defSets)));
                status.setText(view.getResources().getString(R.string.prepare));
                //Get battery saving settings
                getSettings();
                //hrSensor stuff
                hrState(true, hrSensor, hr);
                final CountDownTimer PrepareTimer = new CountDownTimer(5 * 1000, 1000) {
                    @Override
                    public void onTick(long l) {
                        timerUpdate((int) l / 1000);
                    }

                    @Override
                    public void onFinish() {
                        startTimer(view, view.getResources().getString(R.string.work), view.getResources().getString(R.string.rest), file.get(defValues.sWork, defValues.defWorkTime), file.get(defValues.sRest, defValues.defRestTime), hrSensor); }
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
                //Display start layout
                L1.setVisibility(View.VISIBLE);
                L2.setVisibility(View.GONE);
                //Stop timers
                stopTimers();
                //Unregister hr sensor listener to avoid battery drain
                hrState(false, hrSensor, hr);
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

    private void getSettings(){
        file file = new file(defValues.settingsFile, this.mView.getContext());
        this.batterySaving = file.get(defValues.sBatterySaving, defValues.BatterySavingDefault);
        this.hrEnabled = file.get(defValues.sHrSwitch, defValues.HrSwitchDefault);
    }

    private void hrState(boolean state, hrSensor hrSensor, TextView hr){
        if(state){
            if(this.hrEnabled){
                hrSensor.registerListener();
                if(hr.getVisibility() == View.INVISIBLE){
                    hr.setVisibility(View.VISIBLE);
                }
            }else{
                if(hr.getVisibility() == View.VISIBLE){
                    hr.setVisibility(View.INVISIBLE);
                }
            }
        }else{
            if(this.hrEnabled){
                hrSensor.unregisterListener();
            }
        }
    }

    private void init(){
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
        //Layouts
        L1 = this.mView.findViewById(R.id.startScreen);
        L2 = this.mView.findViewById(R.id.timerScreen);
    }

    private void timerUpdate(int v){
        this.init();
        if(!this.batterySaving){
            time.setText(utils.sToMinS(v));
        } else if(time.getText().toString() != "--:--"){
            time.setText("--:--");
        }
        if(v<4){
            if(v==1){
                utils.vibrate(defValues.lVibration, this.mView.getContext());}
            if(v!=1){
                utils.vibrate(defValues.sVibration, this.mView.getContext());}
        }
    }

    private void stopTimers(){
        if(this.workStarted){
            this.workTimer.cancel();
        }
        if(this.restStarted){
            this.restTimer.cancel();
        }
    }

    private void startTimer(final View view, final String sWork, final String sRest, final int work, final int rest, final hrSensor hrSensor){
        this.init();
        this.workStarted = true;
        this.restStarted = false;
        if(!this.mHasActive){
            this.workStarted = false;
            return;
        }
        status.setText(sWork);
        L2.setBackgroundColor(view.getResources().getColor(R.color.red));
        this.workTimer = new CountDownTimer(work * 1000, 1000) {
            @Override
            public void onTick(long l) {
                timerUpdate((int) l / 1000);
            }

            @Override
            public void onFinish() {
                restTimer(view, sWork, sRest, work, rest, hrSensor);
            }
        };
        this.workTimer.start();
    }

    private void restTimer(final View view, final String sWork, final String sRest, final int work, final int rest, final hrSensor hrSensor){
        this.init();
        this.workStarted = false;
        this.restStarted = true;
        if(!this.mHasActive){
            this.restStarted = false;
            return;
        }
        status.setText(sRest);
        L2.setBackgroundColor(view.getResources().getColor(R.color.green));
        this.restTimer = new CountDownTimer(rest * 1000, 1000) {
            @Override
            public void onTick(long l) {
                timerUpdate((int) l / 1000);
            }

            @Override
            public void onFinish() {
                if(Integer.parseInt(rSets.getText().toString())!=1){
                    rSets.setText(String.valueOf(Integer.parseInt(rSets.getText().toString()) - 1));
                    startTimer(view, sWork, sRest, work, rest, hrSensor);
                }else{
                    //Unregister hrSensor listener and make visible initial screen again
                    hrState(false, hrSensor, hr);
                    L1.setVisibility(View.VISIBLE);
                    L2.setVisibility(View.GONE);
                }
            }
        };
        this.restTimer.start();
    }

    //Return the icon for this page, used when the page is disabled in the app list. In this case, the launcher icon is used
    @Override
    public Bitmap getWidgetIcon(Context paramContext) {
        return ((BitmapDrawable) this.mContext.getResources().getDrawable(R.drawable.add)).getBitmap();
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
        //Check if the view is already inflated (reloading)
        if ((!this.mHasActive) && (this.mView != null)) {
            //It is, simply refresh
            refreshView();
        }
        //Store active state
        this.mHasActive = true;
    }

    private void refreshView() {
    }

    //Returns the springboard host
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

    //Called when the page is destroyed completely (in app mode). Same as the onDestroy method of an activity
    @Override
    public void onDestroy() {
        super.onDestroy();
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
package me.micrusa.amaztimer.utils.heartrate;


import android.content.Context;
import android.hardware.Sensor;
import android.os.Handler;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.Date;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.TCX.TCXConstants;
import me.micrusa.amaztimer.TCX.SaveTCX;
import me.micrusa.amaztimer.TCX.TCXUtils;
import me.micrusa.amaztimer.TCX.data.Lap;
import me.micrusa.amaztimer.TCX.data.TCXData;
import me.micrusa.amaztimer.TCX.data.Trackpoint;
import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.utils.heartrate.listeners.Listener;
import me.micrusa.amaztimer.utils.heartrate.listeners.experimentalListener;
import me.micrusa.amaztimer.utils.heartrate.listeners.mainListener;

@SuppressWarnings("CanBeFinal")
public class hrSensor {
    private hrListener listener;
    private final latestTraining latestTraining = new latestTraining();
    private long startTime;
    private String latestTrackpointTime;
    private boolean prevListening = false;
    public int latestHr = 0;

    private static hrSensor hrSensor;
    private Listener hrListener;

    //All tcx needed stuff
    private String currentLapStatus = TCXConstants.STATUS_RESTING;
    private Lap currentLap;
    private TCXData TCXData;

    public static hrSensor getInstance(){
        return hrSensor;
    }

    public static hrSensor initialize(hrListener listener){
        hrSensor = new hrSensor(listener);
        return hrSensor;
    }

    private hrSensor(hrListener listener) {
        //Setup sensor manager, sensor and textview
        this.TCXData = new TCXData();
        this.listener = listener;
    }

    public void setListener(hrListener listener){
        this.listener = listener;
    }

    public void newValue(int v){
        v = hrUtils.getFlattenedHr(v); //Hr will be flattened if the preference is enabled
        if(prevListening) return; //Don't use value if activity haven't started
        if(latestHr != v){
            listener.onHrChanged(v);
            latestHr = v;
        }
        latestTraining.addHrValue(v);
        //Set latest hr value
        String currentDate = TCXUtils.formatDate(new Date());
        //Create Trackpoint and add it to current Lap
        if (!currentDate.equals(this.latestTrackpointTime) && Prefs.getBoolean(Constants.KEY_TCX, Constants.DEFAULT_TCX)) { //This will limit trackpoints to 1/s
            currentLap.addTrackpoint(new Trackpoint(v, new Date()));
            this.latestTrackpointTime = currentDate;
        }
    }

    public void onAccuracyChanged(Sensor param1Sensor, int param1Int) {}

    public void onMainActCreate(Context context){
        if(Prefs.getBoolean(Constants.KEY_HRTOGGLE, true) && !prevListening
                && Prefs.getBoolean(Constants.KEY_HRONSTART, true)){
            prevListening = true;
            if(Prefs.getBoolean(Constants.KEY_HREXPERIMENT, false))
                hrListener = new experimentalListener();
            else
                hrListener = new mainListener();
            hrListener.register(context);
        }
    }

    public void onMainActDestroy(Context context){
        if(Prefs.getBoolean(Constants.KEY_HRTOGGLE, true)
                && Prefs.getBoolean(Constants.KEY_HRONSTART, true))
            hrListener.unregister(context);
    }

    public void registerListener(Context context) {
        latestTraining.cleanAllValues();
        this.startTime = System.currentTimeMillis();
        //Register listener taking into account experimental sensor
        if(!prevListening) {
            if (Prefs.getBoolean(Constants.KEY_HREXPERIMENT, false))
                hrListener = new experimentalListener();
            else
                hrListener = new mainListener();
            hrListener.register(context);
        } else prevListening = false;
    }

    public void unregisterListener(Context context) {
        //Unregister listener
        hrListener.unregister(context);
        //Save time and send it to latestTraining
        long endTime = System.currentTimeMillis();
        int totalTimeInSeconds = (int) (endTime - startTime) / 1000;
        latestTraining.saveDataToFile(context, totalTimeInSeconds);
        if (Prefs.getBoolean(Constants.KEY_TCX, Constants.DEFAULT_TCX)) {
            new Handler().postDelayed(() -> {
              addCurrentLap();
              if(SaveTCX.saveToFile(this.TCXData)){
                Toast.makeText(context, R.string.tcxexporting, Toast.LENGTH_SHORT).show();
              } else {
                Toast.makeText(context, R.string.tcxerror, Toast.LENGTH_SHORT).show();
              }
              resetTcxData();
            }, 1);
        } else {
            resetTcxData();
        }
    }

    private void resetTcxData(){
        this.currentLap = null;
        this.TCXData = new TCXData();
    }

    private void addCurrentLap(){
        if (this.currentLap != null) {
            this.currentLap.setIntensity(this.currentLapStatus);
            this.currentLap.endLap(System.currentTimeMillis());
            this.currentLap.calcCalories();
            this.TCXData.addLap(this.currentLap);
        }
    }

    public void newLap(String lapStatus){
        this.addCurrentLap();
        this.currentLapStatus = lapStatus;
        this.currentLap = new Lap();
    }

    public interface hrListener{
        void onHrChanged(int hr);
    }
}
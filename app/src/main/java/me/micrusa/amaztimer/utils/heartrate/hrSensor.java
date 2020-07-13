package me.micrusa.amaztimer.utils.heartrate;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.Date;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.TCX.Constants;
import me.micrusa.amaztimer.TCX.SaveTCX;
import me.micrusa.amaztimer.TCX.TCXUtils;
import me.micrusa.amaztimer.TCX.data.Lap;
import me.micrusa.amaztimer.TCX.data.TCXData;
import me.micrusa.amaztimer.TCX.data.Trackpoint;
import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.prefUtils;
import me.micrusa.amaztimer.utils.utils;

@SuppressWarnings("CanBeFinal")
public class hrSensor implements SensorEventListener {
    private hrListener listener;
    private final me.micrusa.amaztimer.utils.heartrate.latestTraining latestTraining = new latestTraining();
    private long startTime;
    private int accuracy = 2;
    private String latestHrTime;
    private int latestHr = 0;

    private static hrSensor hrSensor;
    private Thread experimentalThread;

    //All tcx needed stuff
    private String currentLapStatus = Constants.STATUS_RESTING;
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

    public void newValue(int v){
        if (isAccuracyValid() && v > 25 && v < 230 /*Limit to range 25-230 to avoid fake readings*/) {
            //Get hr value and set the text if battery saving mode is disabled
            if(latestHr != v){
                listener.onHrChanged(v);
                latestHr = v;
            }
            //Send hr value to latestTraining array
            latestTraining.addHrValue(v);
            //Set latest hr value
            String currentDate = TCXUtils.formatDate(new Date());
            //Create Trackpoint and add it to current Lap
            if (!currentDate.equals(this.latestHrTime)) //This will limit trackpoints to 1/s
                currentLap.addTrackpoint(new Trackpoint(v, new Date()));
            this.latestHrTime = currentDate;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        newValue((int) event.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor param1Sensor, int param1Int) {
        this.accuracy = param1Int;
    }

    public void registerListener(Context context) {
        utils.setupPrefs(context);
        //Clean all values to avoid merging other values
        latestTraining.cleanAllValues();
        //Register start time
        this.startTime = System.currentTimeMillis();
        //Register listener taking into account experimental sensor
        if(Prefs.getBoolean(defValues.KEY_HREXPERIMENT, false)){
            experimentalThread = new experimentalSensor(new Handler(), context);
            experimentalThread.start();
        } else {
            SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            sm.registerListener(this, sm.getDefaultSensor(defValues.HRSENSOR), defValues.HRSENSOR_DELAY);
        }
    }

    private boolean isAccuracyValid(){
        //Disabled for testing purposes
        return true; //this.accuracy >= defValues.ACCURACY_RANGE[0] && this.accuracy <= defValues.ACCURACY_RANGE[1];
    }

    public void unregisterListener(Context context) {
        //Unregister listener to avoid battery drain
        SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sm.unregisterListener(this);
        //Save time and send it to latestTraining
        long endTime = System.currentTimeMillis();
        int totalTimeInSeconds = (int) (endTime - startTime) / 1000;
        latestTraining.saveDataToFile(context, totalTimeInSeconds);
        if (Prefs.getBoolean(defValues.KEY_TCX, defValues.DEFAULT_TCX)) {
            addCurrentLap();
            boolean result = SaveTCX.saveToFile(this.TCXData);
            resetTcxData();
            utils.setLang(context, Prefs.getString(defValues.KEY_LANG, "en"));
            if (result)
                Toast.makeText(context, R.string.tcxexporting, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, R.string.tcxerror, Toast.LENGTH_SHORT).show();
        } else {
            resetTcxData();
        }
        if(Prefs.getBoolean(defValues.KEY_HREXPERIMENT, false) && experimentalThread != null && !experimentalThread.isInterrupted())
            experimentalThread.interrupt();
    }

    private void resetTcxData(){
        this.currentLap = null;
        this.TCXData = new TCXData();
    }

    private void addCurrentLap(){
        if (this.currentLap != null) {
            this.currentLap.setIntensity(this.currentLapStatus);
            this.currentLap.endLap(System.currentTimeMillis());
            this.currentLap.calcCalories(prefUtils.getAge(),
                    prefUtils.getWeight(),
                    prefUtils.isMale());
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

    private class experimentalSensor extends Thread implements SensorEventListener{

        private Handler handler;
        private Context context;
        private long lastTime;
        private long totalDataThisBatch = 0;
        private long currentBatchSize = 0;

        public experimentalSensor(Handler handler, Context context){
            this.handler = handler;
            this.lastTime = System.currentTimeMillis();
            this.context = context;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(Thread.currentThread().isInterrupted()){
                SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
                sm.unregisterListener(this);
            }
            long now = System.currentTimeMillis();
            totalDataThisBatch += (int) event.values[0] / 100;
            currentBatchSize++;
            if(now - lastTime >= 500) { //This sensor is SO fast so limit rate to a value every 500ms
                int v = (int) (totalDataThisBatch / currentBatchSize);
                handler.post(() -> hrSensor.this.newValue(v));
                totalDataThisBatch = 0;
                currentBatchSize = 0;
                lastTime = now;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            handler.post(() -> hrSensor.this.onAccuracyChanged(sensor, accuracy));
        }

        public void run(){
            SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            sm.registerListener(this, sm.getDefaultSensor(65538 /*PPG Sensor*/),
                    200_000 /*It's much faster*/, 500_000 /*500ms batching*/);
        }
    }
}
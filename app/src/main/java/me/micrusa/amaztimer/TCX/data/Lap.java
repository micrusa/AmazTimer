package me.micrusa.amaztimer.TCX.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.micrusa.amaztimer.TCX.Constants;
import me.micrusa.amaztimer.TCX.TCXUtils;
import me.micrusa.amaztimer.TCX.data.Trackpoint;
import me.micrusa.amaztimer.utils.latestTraining;

public class Lap {

    private List<Trackpoint> tps = new ArrayList<Trackpoint>();
    private String StartTime;
    private String intensity;
    private long longStartTime;
    private long timeInSecs;
    private int kcal = 0;

    public Lap(){
        this.longStartTime = System.currentTimeMillis();
        this.StartTime = TCXUtils.formatDate(new Date());
    }

    public void endLap(long currentTimeMillis){
        this.timeInSecs = (currentTimeMillis - this.longStartTime) / 1000;
    }

    public void addTrackpoint(Trackpoint tp){
        this.tps.add(tp);
    }

    public String getStartTime(){
        return this.StartTime;
    }

    public long getTimeInSeconds(){
        return this.timeInSecs;
    }

    public int getAvgHr(){
        int totalhr = 0;
        for (Trackpoint trackpoint : this.tps){
            totalhr = totalhr + trackpoint.getHr();
        }
        if(this.tps.size() != 0)
            return totalhr / this.tps.size();
        else
            return 0;

    }

    public void calcCalories(int age, int weight, boolean isMale){
        this.kcal = latestTraining.calculateKcal(getAvgHr(), (int) getTimeInSeconds(), age, weight, isMale);
    }

    public int getKcal(){
        return this.kcal;
    }

    public int getMaxHr(){
        int max = 0;
        for (Trackpoint trackpoint : this.tps){
            if(trackpoint.getHr() > max)
                max = trackpoint.getHr();
        }
        return max;
    }

    public List<Trackpoint> getTrackpoints(){
        return this.tps;
    }

    public boolean isLapEmpty(){
        return this.tps.size() == 0;
    }

    public void setIntensity(String Intensity){
        this.intensity = Intensity;
    }

    public String getIntensity(){
        return this.intensity;
    }

}

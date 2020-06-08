package me.micrusa.amaztimer.TCX.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.micrusa.amaztimer.TCX.data.Trackpoint;

public class Lap {

    private List<Trackpoint> tps = new ArrayList<Trackpoint>();

    private String StartTime;
    private long longStartTime;
    private long timeInSecs;
    private int kcal = 0;
    private String intensity;

    public Lap(){
        this.longStartTime = System.currentTimeMillis();
        Date d = new Date();
        this.StartTime = new SimpleDateFormat("yyyy-MM-dd").format(d) + "T"+ new SimpleDateFormat("HH-mm-ss").format(d) + "Z";
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
        if(getAvgHr()==0||getTimeInSeconds()==0||age==0||weight==0){
            this.kcal = 0;
            return;
        }
        double kcal;
        //Formula from https://www.calculatorpro.com/calculator/calories-burned-by-heart-rate/
        if(isMale){
            kcal = (-55.0969 + (0.6309 * getAvgHr()) + (0.1988 * weight) + (0.2017 * age)) / 4.184;
        }else{
            kcal = (-20.4022 + (0.4472 * getAvgHr()) + (0.1263 * weight) + (0.074) * age) / 4.184;
        }
        //Convert time to mins and then multiply by kcal/min
        this.kcal = (int) kcal * ((int) getTimeInSeconds() / 60);
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

    public void setIntensity(String INTENSITY){
        this.intensity = INTENSITY;
    }

    public String getIntensity(){
        return this.intensity;
    }

}

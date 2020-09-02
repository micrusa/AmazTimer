package me.micrusa.amaztimer.TCX.data;

import java.util.Date;

import me.micrusa.amaztimer.TCX.TCXUtils;

public class Trackpoint {

    private String time;
    private int hr;

    public Trackpoint(int Hr, Date Date){
        this.hr = Hr;
        this.time = TCXUtils.formatDate(Date);
    }

    public int getHr(){
        return this.hr;
    }

    public String getTime(){
        return this.time;
    }

}

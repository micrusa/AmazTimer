package me.micrusa.amaztimer.TCX.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Trackpoint {

    private String time;
    private int hr;

    public Trackpoint(int HR, Date DATE){
        this.hr = HR;
        this.time = new SimpleDateFormat("yyyy-MM-dd").format(DATE) + "T"+ new SimpleDateFormat("HH-mm-ss").format(DATE) + "Z";
    }

    public int getHr(){
        return this.hr;
    }

    public String getTime(){
        return this.time;
    }

}

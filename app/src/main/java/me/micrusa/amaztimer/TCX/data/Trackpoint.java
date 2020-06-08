package me.micrusa.amaztimer.TCX.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.micrusa.amaztimer.TCX.Constants;

public class Trackpoint {

    private String time;
    private int hr;

    public Trackpoint(int HR, Date DATE){
        this.hr = HR;
        this.time = new SimpleDateFormat(Constants.DATE_FORMAT).format(DATE) + Constants.CHAR_DATETIME + new SimpleDateFormat(Constants.TIME_FORMAT).format(DATE) + Constants.CHAR_AFTERTIME;
    }

    public int getHr(){
        return this.hr;
    }

    public String getTime(){
        return this.time;
    }

}

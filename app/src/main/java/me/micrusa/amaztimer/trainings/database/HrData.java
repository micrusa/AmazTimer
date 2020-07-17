package me.micrusa.amaztimer.trainings.database;

public class HrData {

    private long time;
    private int hr;

    public HrData(long time, int hr){
        this.time = time;
        this.hr = hr;
    }

    public long getTime() {
        return time;
    }

    public int getHr() {
        return hr;
    }

}

package me.micrusa.amaztimer.trainings.database;

import io.realm.RealmObject;

public class HrData extends RealmObject {

    private long time;
    private int hr;

    public HrData(long time, int hr){
        this.time = time;
        this.hr = hr;
    }
    
    public HrData(){} //Needed for Realm

    public long getTime() {
        return time;
    }

    public int getHr() {
        return hr;
    }

}

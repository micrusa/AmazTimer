package me.micrusa.amaztimer.TCX.data;

import java.util.ArrayList;
import java.util.LinkedList;

import me.micrusa.amaztimer.TCX.data.Lap;

public class TCXData {

    private LinkedList<Lap> laps = new LinkedList<>();

    public boolean isEmpty(){
        return laps.size() == 0;
    }

    public void addLap(Lap lap){
        laps.add(lap);
    }

    public LinkedList<Lap> getLaps(){
        return this.laps;
    }

    public String getTime(){
        if (!this.isEmpty())
            return this.laps.get(0).getStartTime();
        else
            return null;
    }

    public String getSportName(){
        return "Other";
    }

}

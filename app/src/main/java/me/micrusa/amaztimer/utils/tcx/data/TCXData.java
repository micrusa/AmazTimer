package me.micrusa.amaztimer.utils.tcx.data;

import java.util.LinkedList;

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

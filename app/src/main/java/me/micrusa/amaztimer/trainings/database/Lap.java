package me.micrusa.amaztimer.trainings.database;

public class Lap {

    private int lapDuration;
    private boolean isWorking;

    public int getLapDuration() {
        return lapDuration;
    }

    public void setLapDuration(int lapDuration) {
        this.lapDuration = lapDuration;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

}

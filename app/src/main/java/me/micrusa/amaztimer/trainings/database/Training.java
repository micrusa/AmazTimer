package me.micrusa.amaztimer.trainings.database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Training extends RealmObject {
    @PrimaryKey
    private long id;

    private long totalTimeSecs;
    private int Kcal;
    private int MaxHr;
    private int AvgHr;
    private int MinHr;
    private RealmList<Lap> laps;
    private RealmList<HrData> heartrate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTotalTimeSecs() {
        return totalTimeSecs;
    }

    public void setTotalTimeSecs(long totalTimeSecs) {
        this.totalTimeSecs = totalTimeSecs;
    }

    public int getKcal() {
        return Kcal;
    }

    public void setKcal(int kcal) {
        Kcal = kcal;
    }

    public int getMaxHr() {
        return MaxHr;
    }

    public void setMaxHr(int maxHr) {
        MaxHr = maxHr;
    }

    public int getAvgHr() {
        return AvgHr;
    }

    public void setAvgHr(int avgHr) {
        AvgHr = avgHr;
    }

    public int getMinHr() {
        return MinHr;
    }

    public void setMinHr(int minHr) {
        MinHr = minHr;
    }

    public RealmList<Lap> getLaps() {
        return laps;
    }

    public RealmList<HrData> getHeartrate() {
        return heartrate;
    }
}

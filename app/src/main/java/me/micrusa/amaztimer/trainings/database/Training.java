package me.micrusa.amaztimer.trainings.database;

import com.dbflow5.annotation.Column;
import com.dbflow5.annotation.PrimaryKey;
import com.dbflow5.annotation.Table;
import com.dbflow5.structure.BaseModel;

import java.util.ArrayList;

import me.micrusa.amaztimer.trainings.database.lists.HrDataList;
import me.micrusa.amaztimer.trainings.database.lists.LapList;
import me.micrusa.amaztimer.trainings.database.lists.converter.HrDataConverter;
import me.micrusa.amaztimer.trainings.database.lists.converter.LapConverter;

@Table(database = Database.class)
public class Training extends BaseModel {
    @Column
    @PrimaryKey
    private long id;

    @Column
    private long totalTimeSecs;
    @Column
    private int Kcal;
    @Column
    private int MaxHr;
    @Column
    private int AvgHr;
    @Column
    private int MinHr;
    @Column
    private String laps;
    @Column
    private String heartrate;

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

    public LapList getLaps() {
        return new LapConverter().getModelValue(laps);
    }

    private void setLaps(LapList lapList){
        laps = new LapConverter().getDBValue(lapList);
    }

    public void addLap(Lap lap){
        LapList dbLaps = getLaps();
        dbLaps.add(lap);
        setLaps(dbLaps);
    }

    public HrDataList getHeartrate() {
        return new HrDataConverter().getModelValue(heartrate);
    }

    private void setHeartrate(HrDataList hrDataList){
        heartrate = new HrDataConverter().getDBValue(hrDataList);
    }

    public void addHrData(HrData hrData){
        HrDataList dbHr = getHeartrate();
        dbHr.add(hrData);
        setHeartrate(dbHr);
    }
}

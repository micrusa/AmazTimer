package me.micrusa.amaztimer.trainings;

import java.util.Date;

import io.realm.Realm;
import me.micrusa.amaztimer.trainings.database.HrData;
import me.micrusa.amaztimer.trainings.database.Lap;
import me.micrusa.amaztimer.trainings.database.Training;
import me.micrusa.amaztimer.utils.heartrate.hrUtils;
import me.micrusa.amaztimer.utils.prefUtils;

public class TrainingUtil {
    private static Training training;
    private static Lap currentLap;
    private static Realm realm;
    private static long startTime;
    private static long lapStartTime = 0;

    public static void startWorkout(){
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        training = realm.createObject(Training.class);
        training.setId(new Date().getTime());
        realm.commitTransaction();
        startTime = System.currentTimeMillis();
    }

    public static void newHrData(int hr){
        realm.beginTransaction();
        HrData hrData = new HrData(new Date().getTime(), hr);
        hrData = realm.copyToRealm(hrData);
        training.getHeartrate().add(hrData);
        realm.commitTransaction();
    }

    public static void newLap(boolean isWorking){
        saveLap();
        realm.beginTransaction();
        currentLap = realm.createObject(Lap.class);
        currentLap.setWorking(isWorking);
        realm.commitTransaction();
        lapStartTime = System.currentTimeMillis();
    }

    public static void endWorkout(){
        try{
            saveLap();
            int timeSecs = (int) (System.currentTimeMillis() - startTime) / 1000;
            realm.beginTransaction();
            training.setTotalTimeSecs(timeSecs);

            //Max, min && avg hr
            int maxHr = 0;
            int minHr = 1000;
            int avgHr = 0;
            int hrValues = 0;
            for(HrData hrData : training.getHeartrate()) {
                if (hrData.getHr() > maxHr)
                    maxHr = hrData.getHr();
                if (hrData.getHr() < minHr)
                    minHr = hrData.getHr();
                avgHr += hrData.getHr();
                hrValues++;
            }
            avgHr = avgHr / hrValues;

            training.setMaxHr(maxHr);
            training.setMinHr(minHr);
            training.setAvgHr(avgHr);

            training.setKcal(hrUtils.calculateKcal(avgHr, timeSecs,
                    prefUtils.getAge(), prefUtils.getWeight(), prefUtils.isMale()));
            realm.commitTransaction();
        } finally {
            realm.close();
        }
        startTime = 0;
        lapStartTime = 0;
        currentLap = null;
    }

    private static void saveLap(){
        if(currentLap != null){
            int duration = (int) (System.currentTimeMillis() - lapStartTime) / 1000;
            realm.beginTransaction();
            currentLap.setLapDuration(duration);
            training.getLaps().add(currentLap);
            realm.commitTransaction();
        }
    }
}

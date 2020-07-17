package me.micrusa.amaztimer.trainings;

import com.dbflow5.config.FlowManager;

import java.util.Date;

import me.micrusa.amaztimer.trainings.database.Database;
import me.micrusa.amaztimer.trainings.database.HrData;
import me.micrusa.amaztimer.trainings.database.Lap;
import me.micrusa.amaztimer.trainings.database.Training;
import me.micrusa.amaztimer.utils.heartrate.hrUtils;
import me.micrusa.amaztimer.utils.prefUtils;

public class TrainingUtil {
    private static Training training;
    private static Lap currentLap;
    private static long startTime;
    private static long lapStartTime = 0;

    public static void startWorkout() {
        startTime = System.currentTimeMillis();
        training = new Training();
        training.setId(startTime);
    }

    public static void newHrData(int hr) {
        training.addHrData(new HrData(new Date().getTime(), hr));
    }

    public static void newLap(boolean isWorking) {
        saveLap();
        lapStartTime = System.currentTimeMillis();
        currentLap = new Lap();
        currentLap.setWorking(isWorking);
    }

    public static void endWorkout() {
        saveLap(); //Save latest lap

        int timeSecs = (int) (System.currentTimeMillis() - startTime) / 1000;
        training.setTotalTimeSecs(timeSecs);

        //Max, min && avg hr
        int maxHr = 0;
        int minHr = 1000;
        int avgHr = 0;
        int hrValues = 0;
        for (HrData hrData : training.getHeartrate()) {
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

        training.save(FlowManager.getDatabase(Database.class)); //Save after saving everything to the class

        currentLap = null;
        lapStartTime = 0;
        startTime = 0;
    }

    private static void saveLap() {
        if (currentLap != null) {
            int duration = (int) (System.currentTimeMillis() - lapStartTime) / 1000;
            currentLap.setLapDuration(duration);
            training.addLap(currentLap);
        }
    }
}

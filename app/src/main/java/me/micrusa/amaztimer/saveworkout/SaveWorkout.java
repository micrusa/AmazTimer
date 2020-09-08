/*
 * MIT License
 *
 * Copyright (c) 2020 Miguel Cruces
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.micrusa.amaztimer.saveworkout;

import android.content.Context;

import androidx.room.Room;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.Date;

import me.micrusa.amaztimer.AmazTimerApplication;
import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.saveworkout.database.AmazTimerDB;
import me.micrusa.amaztimer.saveworkout.database.DBConstants;
import me.micrusa.amaztimer.saveworkout.database.objects.Workout;
import me.micrusa.amaztimer.utils.sensors.heartrate.hrUtils;
import me.micrusa.amaztimer.utils.sensors.repsCounter.RepsCounter;

public class SaveWorkout {

    private static Workout workout;

    private static long setStartTime = 0;
    private static long startTime = 0;

    public static void startWorkout(){
        workout = new Workout();

        workout.time = new Date().getTime();
        workout.hr = new ArrayList<>();
        workout.sets = new ArrayList<>();
        workout.setsReps = new ArrayList<>();

        startTime = System.currentTimeMillis();
        setStartTime = System.currentTimeMillis();
    }

    public static void addHrValue(int hr){
        workout.hr.add(hr);
    }

    public static void endSet(boolean saveSets){
        int setTime = (int) (System.currentTimeMillis() - setStartTime);
        if(setTime >= 250)
            workout.sets.add(setTime / 1000);
        if(saveSets && Prefs.getBoolean(Constants.KEY_REPSCOUNT, false))
            workout.setsReps.add(RepsCounter.getReps());
    }

    public static void endWorkout(){
        workout.totalTime = (int) (System.currentTimeMillis() - startTime) / 1000;
        workout.kcal = hrUtils.calculateKcal(hrUtils.getAvg(workout.hr), workout.totalTime);

        //Save to DB in a second Thread to avoid an UI lock
        new Thread(() -> {
            AmazTimerDB db = Room.databaseBuilder(AmazTimerApplication.getContext(),
                    AmazTimerDB.class, DBConstants.DB_NAME).build();

            db.workoutDao().insertAll(workout);
        }).start();

    }
}

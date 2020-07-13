package me.micrusa.amaztimer.utils.heartrate;

import android.content.Context;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.Collections;
import java.util.LinkedList;

import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.prefUtils;

public class latestTraining {

    private LinkedList<Integer> hrArray = new LinkedList<>();

    void addHrValue(int value){
        this.hrArray.add(value);
    }

    void saveDataToFile(Context context, int time){
        if(this.hrArray.size() < 1)
            return;
        //Get min and max heart rate
        int minHr = this.hrArray.indexOf(Collections.min(hrArray));
        int maxHr = this.hrArray.indexOf(Collections.max(hrArray));
        //Calculate average heart rate
        int totalHr = 0;
        for (int value : this.hrArray) {
            totalHr = totalHr + value;
        }
        int avgHr = totalHr / this.hrArray.size();
        //Get body data from file
        int age = prefUtils.getAge();
        int weight = prefUtils.getWeight();
        boolean isMale = prefUtils.isMale();
        //Calculate kcal count
        int kcal = calculateKcal(avgHr, time, age, weight, isMale);
        //Save everything to file
        saveToFile(minHr, maxHr, avgHr, kcal);
        //Clear hrArray to avoid merging them when there is another training
        this.hrArray.clear();
    }

    public static int calculateKcal(int avgHr, int time, int age, int weight, boolean isMale){
        if(avgHr==0||time==0||age==0||weight==0){return 0;}
        double kcalPerMin;
        //Formula from https://www.calculatorpro.com/calculator/calories-burned-by-heart-rate/
        if(isMale){
            kcalPerMin = (-55.0969 + (0.6309 * avgHr) + (0.1988 * weight) + (0.2017 * age)) / 4.184;
        }else{
            kcalPerMin = (-20.4022 + (0.4472 * avgHr) + (0.1263 * weight) + (0.074) * age) / 4.184;
        }
        //Calculate kcal from kcal/min
        return (int) (kcalPerMin * time) / 60;
    }

    void cleanAllValues(){
        //Clear values and save default values to file, this way it wont conflict with other trainings
        this.hrArray.clear();
    }

    private void saveToFile(int minHr, int maxHr, int avgHr, int kcal){
        //Save data to file
        Prefs.putInt(defValues.KEY_MINHR, minHr);
        Prefs.putInt(defValues.KEY_MAXHR, maxHr);
        Prefs.putInt(defValues.KEY_AVGHR, avgHr);
        Prefs.putInt(defValues.KEY_KCAL, kcal);
    }

}

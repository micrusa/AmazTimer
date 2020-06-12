package me.micrusa.amaztimer.utils;

import android.content.Context;

import java.util.Arrays;

import me.micrusa.amaztimer.defValues;

public class latestTraining {
    private final defValues defValues = new defValues();

    private int[] hrArray = null;

    void addHrValue(int value){
        //If hrArray is null create it to avoid FC
        if(this.hrArray == null){
            this.hrArray = new int[0];
        }
        //Copy array to a bigger one
        int[] newArray = new int[this.hrArray.length + 1];
        System.arraycopy(this.hrArray, 0, newArray, 0, this.hrArray.length);
        this.hrArray = newArray;
        //Add new hr value to the array
        this.hrArray[newArray.length - 1] = value;
    }

    void saveDataToFile(Context context, int time){
        file dataFile = new file(defValues.LATEST_TRAIN_FILE, context);
        file bodyFile = new file(defValues.BODY_FILE, context);
        //Return if hrArray is null
        if(this.hrArray == null){
            return;
        }
        //Get min and max heart rate
        Arrays.sort(this.hrArray);
        int minHr = this.hrArray[0];
        int maxHr = this.hrArray[this.hrArray.length - 1];
        //Calculate average heart rate
        int totalHr = 0;
        for (int value : this.hrArray) {
            totalHr = totalHr + value;
        }
        int avgHr = totalHr / this.hrArray.length;
        //Get body data from file
        int age = bodyFile.get(defValues.SETTINGS_AGE, defValues.DEFAULT_AGE);
        int weight = bodyFile.get(defValues.SETTINGS_WEIGHT, defValues.DEFAULT_WEIGHT);
        boolean isMale = bodyFile.get(defValues.SETTINGS_MALE, defValues.DEFAULT_MALE);
        //Calculate kcal count
        int kcal = calculateKcal(avgHr, time, age, weight, isMale);
        //Save everything to file
        saveToFile(dataFile, minHr, maxHr, avgHr, kcal);
        //Clean hrArray to avoid merging them when there is another training
        this.hrArray = null;
    }

    public static int calculateKcal(int avgHr, int time, int age, int weight, boolean isMale){
        if(avgHr==0||time==0||age==0||weight==0){return 0;}
        double kcal;
        //Formula from https://www.calculatorpro.com/calculator/calories-burned-by-heart-rate/
        if(isMale){
            kcal = (-55.0969 + (0.6309 * avgHr) + (0.1988 * weight) + (0.2017 * age)) / 4.184;
        }else{
            kcal = (-20.4022 + (0.4472 * avgHr) + (0.1263 * weight) + (0.074) * age) / 4.184;
        }
        //Convert time to mins and then multiply by kcal/min
        return (int) kcal * (time / 60);
    }

    void cleanAllValues(Context context){
        //Clean values and save default values to file, this way it wont conflict with other trainings
        this.hrArray = null;
        file dataFile = new file(defValues.LATEST_TRAIN_FILE, context);
        saveToFile(dataFile, defValues.DEFAULT_HR_VALUES, defValues.DEFAULT_HR_VALUES, defValues.DEFAULT_HR_VALUES, defValues.DEFAULT_KCAL_VALUES);
    }

    private void saveToFile(file file, int minHr, int maxHr, int avgHr, int kcal){
        //Save data to file
        file.set(defValues.SETTINGS_MINHR, minHr);
        file.set(defValues.SETTINGS_MAXHR, maxHr);
        file.set(defValues.SETTINGS_AVGHR, avgHr);
        file.set(defValues.SETTINGS_KCAL, kcal);
    }

}

package me.micrusa.amaztimer.utils;

import android.content.Context;

import java.util.Arrays;

import me.micrusa.amaztimer.defValues;

class latestTraining {
    private defValues defValues = new defValues();

    private int[] hrArray = null;

    void addHrValue(int value){
        //If hrArray is null create it to avoid FC
        if(this.hrArray == null){
            this.hrArray = new int[0];
        }
        //Copy array to a bigger one
        int[] newArray = new int[this.hrArray.length + 1];
        for(int i=0; i<this.hrArray.length; i++){
            newArray[i] = this.hrArray[i];
        }
        this.hrArray = newArray;
        //Add new hr value to the array
        this.hrArray[newArray.length - 1] = value;
    }

    void saveDataToFile(Context context, int time){
        file dataFile = new file(defValues.latestTrainFile, context);
        file bodyFile = new file(defValues.bodyFile, context);
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
        int age = bodyFile.get(defValues.sAge, defValues.defAge);
        int weight = bodyFile.get(defValues.sWeight, defValues.defWeight);
        boolean isMale = bodyFile.get(defValues.sMale, defValues.defMale);
        //Calculate kcal count
        int kcal = calculateKcal(avgHr, time, age, weight, isMale);
        //Save everything to file
        saveToFile(dataFile, minHr, maxHr, avgHr, kcal);
        //Clean hrArray to avoid merging them when there is another training
        this.hrArray = null;
    }

    private int calculateKcal(int avgHr, int time, int age, int weight, boolean isMale){
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
        file dataFile = new file(defValues.latestTrainFile, context);
        saveToFile(dataFile, defValues.defHrValues, defValues.defHrValues, defValues.defHrValues, defValues.defKcalValues);
    }

    private void saveToFile(file file, int minHr, int maxHr, int avgHr, int kcal){
        //Save data to file
        file.set(defValues.sMinHr, minHr);
        file.set(defValues.sMaxHr, maxHr);
        file.set(defValues.sAvgHr, avgHr);
        file.set(defValues.sKcal, kcal);
    }

}

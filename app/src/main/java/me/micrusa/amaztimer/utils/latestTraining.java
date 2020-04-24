package me.micrusa.amaztimer.utils;

import android.content.Context;

import java.util.Arrays;

import me.micrusa.amaztimer.defValues;

public class latestTraining {
    private defValues defValues = new defValues();

    private int[] hrArray = new int[0];

    public void addHrValue(int value){
        int[] newArray = new int[this.hrArray.length + 1];
        for(int i=0; i<this.hrArray.length; i++){
            newArray[i] = this.hrArray[i];
        }
        this.hrArray = newArray;
        this.hrArray[newArray.length - 1] = value;
    }

    public void saveDataToFile(Context context, int time){
        file dataFile = new file(defValues.latestTrainFile, context);
        file bodyFile = new file(defValues.bodyFile, context);
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

    public int calculateKcal(int avgHr, int time, int age, int weight, boolean isMale){
        if(avgHr==0||time==0||age==0||weight==0){return 0;}
        //Convert time to mins
        time = time / 60;
        double kcal;
        //Formula from https://muyfitness.com/como-determinar-las-calorias-quemadas-conociendo-el-ritmo-cardiaco_13102596/
        //It's in spanish
        if(isMale){
            kcal = (-55.0969 + (0.6309 * avgHr) + (0.1988 * weight) + (0.2017 * age)) / 4.184;
        }else{
            kcal = (-20.4022 + (0.4472 * avgHr) + (0.1263 * weight) + (0.074) * age) / 4.184;
        }
        kcal = kcal * time;
        return (int) kcal;
    }

    private void saveToFile(file file, int minHr, int maxHr, int avgHr, int kcal){
        file.set(defValues.sMinHr, minHr);
        file.set(defValues.sMaxHr, maxHr);
        file.set(defValues.sAvgHr, avgHr);
        file.set(defValues.sKcal, kcal);
    }

}

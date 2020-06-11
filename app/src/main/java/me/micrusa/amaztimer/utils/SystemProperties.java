package me.micrusa.amaztimer.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.logging.Logger;

public class SystemProperties {

    //Everything inside this class is from AmazMod.

    public static String getSystemProperty(String name) {
        InputStreamReader in;
        BufferedReader reader;
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"/system/bin/getprop", name});
            in = new InputStreamReader(proc.getInputStream());
            reader = new BufferedReader(in);
            return reader.readLine();
        } catch (IOException e) {
            Log.e("AmazTimer", "SystemProperties getSystemProperty exception: {}" + e.getMessage());
            return null;
        }
    }

    public static boolean isPace(){
        return checkIfModel(new String[]{"A1602", "A1612"}, "Pace") || new File("/system/.pace_hybrid").exists();
    }

    public static boolean isStratos(){
        return checkIfModel(new String[]{"A1609", "A1619"}, "Stratos") && !new File("/system/.pace_hybrid").exists();
    }

    public static boolean isVerge(){
        return checkIfModel(new String[]{"A1801", "A1811"}, "Verge");
    }

    public static boolean isStratos3(){
        return checkIfModel(new String[]{"A1928", "A1929"}, "Stratos 3");
    }

    public static boolean checkIfModel(String[] targetModels, String Name){
        String model = getSystemProperty("ro.build.huami.model");
        boolean check = Arrays.asList(targetModels).contains(model);
        Log.i("AmazTimer", "[System Properties] Current model (" + model + ") is " + ((check) ? "" : "NOT ") + "a " + Name);
        return check;
    }

}

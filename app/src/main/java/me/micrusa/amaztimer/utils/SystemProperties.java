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
        String prop;
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"/system/bin/getprop", name});
            in = new InputStreamReader(proc.getInputStream());
            reader = new BufferedReader(in);
            prop = reader.readLine();
            reader.close();
            return prop;
        } catch (IOException e) {
            Log.e("AmazTimer", "SystemProperties getSystemProperty exception: {}" + e.getMessage());
            return null;
        }
    }

    public static boolean getBoolean(String key, boolean def) {
        try {
            return (Boolean) Class.forName("android.os.SystemProperties").getMethod("getBoolean", String.class, boolean.class)
                    .invoke(null, key, def);
        } catch (Exception e) {
            e.printStackTrace();
            return def;
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

    public static boolean isStratosNewKeys(){
        return getBoolean("prop.keyfeature.five", false);
    }

    public static boolean checkIfModel(String[] targetModels, String Name){
        String model = getSystemProperty("ro.build.huami.model");
        boolean check = Arrays.asList(targetModels).contains(model);
        Log.i("AmazTimer", "[System Properties] Current model (" + model + ") is " + ((check) ? "" : "NOT ") + "a " + Name);
        return check;
    }

    public static String getDeviceName(){
        if(isPace())
            return "Huami Amazfit Pace";
        else if(isStratos())
            return "Huami Amazfit Stratos";
        else if(isVerge())
            return "Huami Amazfit Verge";
        else if(isStratos3())
            return "Huami Amazfit Stratos 3";
        else
            return "Unknown device";
    }

    public static boolean isDeviceSupported(){
        return isPace() || isStratos() || isVerge() || isStratos3();
    }

}

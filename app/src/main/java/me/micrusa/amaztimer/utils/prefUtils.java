package me.micrusa.amaztimer.utils;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.defValues;

public class prefUtils {

    public static int getWeight(){
        return Integer.parseInt(Prefs.getString(defValues.KEY_WEIGHT, String.valueOf(defValues.DEFAULT_WEIGHT)));
    }

    public static int getAge(){
        return defValues.CURRENT_YEAR - Integer.parseInt(Prefs.getString(defValues.KEY_AGE, String.valueOf(defValues.DEFAULT_AGE)));
    }

    public static boolean isMale(){
        return Boolean.parseBoolean(Prefs.getString(defValues.KEY_GENDER, "true"));
    }

    public static int getVibration(int vibration){
        int multiplier = Prefs.getInt(defValues.KEY_VIBRATION, 2);
        switch(multiplier){
            case 1:
                vibration = vibration / 2;
                break;
            case 3:
                vibration = vibration * 2;
                break;
            default:
                break;
        }
        return vibration;
    }

}

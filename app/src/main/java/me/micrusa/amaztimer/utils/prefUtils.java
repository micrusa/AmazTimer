package me.micrusa.amaztimer.utils;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.Constants;

public class prefUtils {

    public static int getWeight(){
        return Integer.parseInt(Prefs.getString(Constants.KEY_WEIGHT, String.valueOf(Constants.DEFAULT_WEIGHT)));
    }

    public static int getAge(){
        return Constants.CURRENT_YEAR - Integer.parseInt(Prefs.getString(Constants.KEY_AGE, String.valueOf(Constants.DEFAULT_AGE)));
    }

    public static boolean isMale(){
        return Boolean.parseBoolean(Prefs.getString(Constants.KEY_GENDER, "true"));
    }

    public static int getVibration(int vibration){
        int multiplier = Prefs.getInt(Constants.KEY_VIBRATION, 2);
        
        //Disable multiplier on haptic vibrations
        if(vibration == Constants.HAPTIC_VIBRATION) return vibration;
        
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

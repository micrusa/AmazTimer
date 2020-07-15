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

}

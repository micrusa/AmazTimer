package me.micrusa.amaztimer.utils.heartrate;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.utils.prefUtils;

public class hrUtils {
    private static final int MAX_HR_FLUCTUATION = 3;

    public static int getFlattenedHr(int hr){
        if(!Prefs.getBoolean(Constants.KEY_FLATTENHR, false))
            return hr; //Don't flatten if flattening disabled
        int lastHr = hrSensor.getInstance().latestHr;
        int difference = Math.abs(lastHr - hr);
        if(hr - lastHr > MAX_HR_FLUCTUATION) //This is a very simple algorithm, needs work
            hr = hr - MAX_HR_FLUCTUATION;
        else if(lastHr - hr > MAX_HR_FLUCTUATION)
            hr = lastHr - MAX_HR_FLUCTUATION;
        return hr;
    }

    public static String hrZonePercentage(int hr){
        return hrZonePercentageInt(hr) + "%";
    }

    public static int hrZonePercentageInt(int hr){
        if(hr == 0 | prefUtils.getAge() == 0)
            return 0;

        //Source: https://www.ntnu.edu/cerg/hrmax#:~:text=Based%20on%20these%20tests%20we,211%20%2D%200.64*age%22.
        return (int) (hr * 100 / getMaxHr());
    }

    public static int calculateKcal(int avgHr, int time){
        int age = prefUtils.getAge();
        int weight = prefUtils.getWeight();
        boolean isMale = prefUtils.isMale();
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
    
    public static double getMaxHr(){
        return 211 - 0.64 * prefUtils.getAge();
    }
}

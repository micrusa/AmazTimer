package me.micrusa.amaztimer.utils.heartrate;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.prefUtils;

public class hrUtils {
    private static final int MAX_HR_FLUCTUATION = 3;

    public static int getFlattenedHr(int hr){
        if(!Prefs.getBoolean(defValues.KEY_FLATTENHR, false))
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
        return (int) (hr * 100 / (211 - 0.64 * prefUtils.getAge()));
    }
}

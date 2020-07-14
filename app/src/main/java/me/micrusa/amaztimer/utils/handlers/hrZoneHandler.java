package me.micrusa.amaztimer.utils.handlers;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.heartrate.hrUtils;

public class hrZoneHandler {

    private View hrView;
    private int latestHrZone = 0;
    private boolean enable;

    public hrZoneHandler(View hrView){
        this.hrView = hrView;
        enable = Prefs.getBoolean(defValues.KEY_HRZONE, true);
    }

    public void addHrValue(int value){
        if(!enable)
            return;
        int hrZone = hrUtils.hrZonePercentageInt(value);
        if (latestHrZone != hrZone){
            latestHrZone = hrZone;

            Drawable zoneColor;
            Resources res = hrView.getResources();

            if(hrZone < 60) zoneColor = res.getDrawable(R.color.zonelow);
            else if(hrZone > 60 && hrZone < 70) zoneColor = res.getDrawable(R.color.zonelowmid);
            else if(hrZone > 70 && hrZone < 80) zoneColor = res.getDrawable(R.color.zonemid);
            else if(hrZone > 80 && hrZone < 90) zoneColor = res.getDrawable(R.color.zonemidhigh);
            else if(hrZone > 90) zoneColor = res.getDrawable(R.color.zonehigh);
            else zoneColor = res.getDrawable(R.color.zonedefault);

            hrView.setBackground(zoneColor);
        }
    }
}

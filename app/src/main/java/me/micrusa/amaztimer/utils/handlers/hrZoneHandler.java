package me.micrusa.amaztimer.utils.handlers;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.HashMap;
import java.util.Map;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.utils.sensors.heartrate.hrUtils;

import static java.lang.Math.abs;


public class hrZoneHandler {

    private View hrView;
    private boolean enable;
    private static final Map<Object, Object> HR_ZONES = new HashMap<>();

    public hrZoneHandler(View hrView){
        this.hrView = hrView;
        enable = Prefs.getBoolean(Constants.KEY_HRZONE, true);
        setupZonesMap();
    }

    private void setupZonesMap(){
        Resources res = hrView.getResources();
        HR_ZONES.put(new int[]{0, 60}, res.getDrawable(R.color.zonelow));
        HR_ZONES.put(new int[]{60, 70}, res.getDrawable(R.color.zonelowmid));
        HR_ZONES.put(new int[]{70, 80}, res.getDrawable(R.color.zonemid));
        HR_ZONES.put(new int[]{80, 90}, res.getDrawable(R.color.zonemidhigh));
        HR_ZONES.put(new int[]{90, 130}, res.getDrawable(R.color.zonehigh));
    }

    public void addHrValue(int value){
        if(!enable) return;
        int hrZone = hrUtils.hrZonePercentageInt(value);

        for(Map.Entry<Object, Object> entry : HR_ZONES.entrySet()){
            int[] zones = (int[]) entry.getKey();
            Drawable drawable = (Drawable) entry.getValue();

            if(zones.length >= 2 && hrZone > zones[0] && hrZone < zones[1]){
                hrView.setBackground(drawable);
                return;
            }
        }
    }
}

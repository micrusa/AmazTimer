package me.micrusa.amaztimer;

import android.app.Application;

import com.dbflow5.config.FlowManager;

public class AmazTimerApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        //Setup realm
        FlowManager.init(this);
    }
}

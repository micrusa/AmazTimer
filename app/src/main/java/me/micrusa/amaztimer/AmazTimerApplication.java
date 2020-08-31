package me.micrusa.amaztimer;

import android.app.Application;

import me.micrusa.amaztimer.utils.utils;

public class AmazTimerApplication extends Application {

    public void onCreate() {
        super.onCreate();

        utils.setupPrefs(this);
        utils.setupLang(this);
    }

}

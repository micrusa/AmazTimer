package me.micrusa.amaztimer;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AmazTimerApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        //Setup realm
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("amaztimer.realm")
                .schemaVersion(1L)
                .build();
        Realm.setDefaultConfiguration(config);
    }
}

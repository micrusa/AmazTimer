package me.micrusa.amaztimer.utils.heartrate.listeners;

import android.content.Context;

public interface Listener {
    void register(Context context);
    void unregister(Context context);
}

package me.micrusa.amaztimer.utils;

import android.os.Handler;
import android.widget.Chronometer;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class chronoHandler {

    private boolean running;
    private TextView chrono;
    private int timeElapsed;
    private Thread thread;

    public chronoHandler(TextView chrono){
        this.chrono = chrono;
        running = true;
        final Handler handler = new Handler(chrono.getContext().getMainLooper());
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(Thread.currentThread().isInterrupted())
                            return;
                        chrono.setText(utils.formatTime(++timeElapsed));
                        handler.postDelayed(this, 1000);
                    }
                });
            }
        });
    }

    public void stop(){
        if(running){
            running = false;
            thread.interrupt();
        }
    }
}

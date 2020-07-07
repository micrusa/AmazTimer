package me.micrusa.amaztimer.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.Chronometer;
import android.widget.TextView;

import org.tinylog.Logger;

public class chronoHandler {

    private boolean running;
    private thread chronoThread;

    public chronoHandler(TextView chrono){
        chronoThread = new thread(chrono, new Handler());
        chronoThread.start();
        running = true;
    }

    public void stop(){
        if(running){
            running = false;
            chronoThread.interrupt();
        }
    }

    private static class thread extends Thread {

        private TextView chrono;
        private int timeElapsed;
        private Handler handler;

        public thread(TextView chrono, Handler handler){
            this.chrono = chrono;
            this.handler = handler;
        }

        public void run(){
            while(!Thread.currentThread().isInterrupted()){
                try {
                    handler.post(() -> chrono.setText(utils.formatTime(timeElapsed++)));
                    sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}

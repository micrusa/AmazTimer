package me.micrusa.amaztimer.utils.handlers;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;

import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.utils;
import me.micrusa.amaztimer.TimerActivity;

public class timerHandler {

    private TextView timerText;
    private CountDownTimer timer;
    private boolean running;
    private Context context;

    public timerHandler(TextView timerText, int seconds, timerInterface timerInterface, Context paramContext){
        this.timerText = timerText;
        this.context = paramContext;
        running = true;
        timer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long l) {
                updateTimer(l);
            }

            @Override
            public void onFinish() {
                if(!running) return;
                running = false;
                timerInterface.onFinish();
            }
        };
        timer.start();
    }

    public void stop(){
        if(running)
            timer.cancel();
        running = false;
    }

    private void updateTimer(long millis){
        int time = (int) millis / 1000;
        if(!running || !TimerActivity.isRunning) {
          timer.cancel();
          return;
        } //Avoid vibrations/changes when not running
        timerText.setText(utils.formatTime(time));
        if(time < 4){
            utils.vibrate(defValues.SHORT_VIBRATION, context, true);
            if(time == 1){
                new Handler().postDelayed(() -> {
                    utils.vibrate(defValues.LONG_VIBRATION, context, true);
                    timerText.setText(utils.formatTime(0));
                }, 950);
            }
        }
    }

    public static interface timerInterface{
        void onFinish();
    }
}

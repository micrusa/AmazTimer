/*
 * MIT License
 *
 * Copyright (c) 2020 Miguel Cruces
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.micrusa.amaztimer.utils.handlers;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;

import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.utils.Utils;
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
        timerText.setText(Utils.formatTime(time));
        if(time < 4){
            Utils.vibrate(Constants.SHORT_VIBRATION, context, true);
            if(time == 1){
                new Handler().postDelayed(() -> {
                    Utils.vibrate(Constants.LONG_VIBRATION, context, true);
                    timerText.setText(Utils.formatTime(0));
                }, 950);
            }
        }
    }

    public static interface timerInterface{
        void onFinish();
    }
}

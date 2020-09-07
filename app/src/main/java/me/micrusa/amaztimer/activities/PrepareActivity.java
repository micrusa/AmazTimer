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

package me.micrusa.amaztimer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.TimerActivity;
import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.utils.prefUtils;
import me.micrusa.amaztimer.utils.utils;

public class PrepareActivity extends AppCompatActivity {

    private TextView timer;
    private boolean finished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        startTimer();
    }

    private void init(){
        utils.setupLang(this);
        setContentView(R.layout.activity_prepare);
        timer = findViewById(R.id.prepareTime);
    }

    private void startTimer(){
        new CountDownTimer(6000, 1000){
            @Override
            public void onTick(long l) {
                int time = (int) l / 1000;
                timer.setText(String.valueOf(time));
                if(time < 4){
                    utils.vibrate(Constants.SHORT_VIBRATION, PrepareActivity.this);
                    if(time == 1)
                        new Handler().postDelayed(() -> {
                            timer.setText("0");
                            utils.vibrate(Constants.LONG_VIBRATION, PrepareActivity.this);
                            }, 950);
                }
            }
            @Override
            public void onFinish() {
                finished = true;
                startActivity(new Intent(PrepareActivity.this, prefUtils.getTimerClass()));
                finish();
            }
        }.start();
    }

    public void onResume() {
        super.onResume();
        if(finished)
            finish();
    }
}
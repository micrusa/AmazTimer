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

package me.micrusa.amaztimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import me.micrusa.amaztimer.activities.CreateNew;
import me.micrusa.amaztimer.activities.Saved;
import me.micrusa.amaztimer.activities.SettingsActivity;
import me.micrusa.amaztimer.utils.Utils;
import me.micrusa.amaztimer.utils.button.buttonEvent;
import me.micrusa.amaztimer.utils.button.buttonListener;

public class MainActivity extends AppCompatActivity {

    private Button saved, createNew, settings;
    private boolean hasLaunchedActivities = false;
    private buttonListener buttonListener = new buttonListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBtnListener();
        init();
    }

    private void init(){
        saved = findViewById(R.id.main_saved);
        createNew = findViewById(R.id.main_create_new);
        settings = findViewById(R.id.main_settings);

        saved.setOnClickListener(view -> launchActivity(new Intent(this, Saved.class)));
        createNew.setOnClickListener(view -> launchActivity(new Intent(this, CreateNew.class)));
        settings.setOnClickListener(view -> launchActivity(new Intent(this, SettingsActivity.class)));
    }

    private void setupBtnListener(){
        Handler handler = new Handler();
        buttonListener.start(this, ButtonEvent -> {
            if(ButtonEvent.getKey() == buttonEvent.KEY_UP)
                handler.post(() -> saved.performClick());
            else if(ButtonEvent.getKey() == buttonEvent.KEY_CENTER)
                handler.post(() -> createNew.performLongClick());
            else if(ButtonEvent.getKey() == buttonEvent.KEY_DOWN)
                handler.post(() -> settings.performLongClick());
        });
    }

    private void launchActivity(Intent intent){
        if(hasLaunchedActivities) return; //Avoid multiple activities launched
        Utils.vibrate(Constants.HAPTIC_VIBRATION, this);
        hasLaunchedActivities = true;
        buttonListener.stop();
        startActivity(intent);
    }

    public void onStop() {
        super.onStop();
        buttonListener.stop();
    }

    public void onPause() {
        buttonListener.stop();
        super.onPause();
    }

    public void onResume() {
        hasLaunchedActivities = false;
        setupBtnListener();
        super.onResume();
    }
}
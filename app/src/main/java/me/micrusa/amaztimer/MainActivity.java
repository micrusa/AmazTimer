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
import me.micrusa.amaztimer.activities.saved.Saved;
import me.micrusa.amaztimer.activities.SettingsActivity;
import me.micrusa.amaztimer.saveworkout.ui.SavedWorkoutsActivity;
import me.micrusa.amaztimer.utils.Utils;
import me.micrusa.amaztimer.utils.button.ButtonSelector;
import me.micrusa.amaztimer.utils.button.ButtonListener;

public class MainActivity extends AppCompatActivity {
    private Button saved, createNew, settings, workouts;
    private ButtonSelector buttonSelector;
    private boolean hasLaunchedActivities = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        buttonSelector.startListening();
    }

    private void init(){
        saved = findViewById(R.id.main_saved);
        createNew = findViewById(R.id.main_create_new);
        settings = findViewById(R.id.main_settings);
        workouts = findViewById(R.id.main_workouts);
        buttonSelector = new ButtonSelector(new Button[]{saved, createNew, workouts, settings}, this);

        saved.setOnClickListener(view -> launchActivity(new Intent(this, Saved.class)));
        createNew.setOnClickListener(view -> launchActivity(new Intent(this, CreateNew.class)));
        settings.setOnClickListener(view -> launchActivity(new Intent(this, SettingsActivity.class)));
        workouts.setOnClickListener(view -> launchActivity(new Intent(this, SavedWorkoutsActivity.class)));
    }

    private void launchActivity(Intent intent){
        if(hasLaunchedActivities) return; //Avoid multiple activities launched
        Utils.vibrate(Constants.HAPTIC_VIBRATION, this);
        hasLaunchedActivities = true;
        buttonSelector.stopListening();
        startActivity(intent);
    }

    public void onStop() {
        super.onStop();
        buttonSelector.stopListening();
    }

    public void onPause() {
        buttonSelector.stopListening();
        super.onPause();
    }

    public void onResume() {
        hasLaunchedActivities = false;
        buttonSelector.startListening();
        super.onResume();
    }
}
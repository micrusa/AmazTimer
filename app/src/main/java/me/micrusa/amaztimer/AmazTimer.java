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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.activities.SettingsActivity;
import me.micrusa.amaztimer.utils.button.buttonEvent;
import me.micrusa.amaztimer.utils.button.buttonListener;
import me.micrusa.amaztimer.utils.sensors.heartrate.hrSensor;
import me.micrusa.amaztimer.utils.sensors.repsCounter.ui.dialog.StartExerciseDialog;
import me.micrusa.amaztimer.utils.Utils;

public class AmazTimer extends Activity {
    private Button[] plusMinusBtns;
    private Button start;
    private TextView sets, rest, work;
    
    private buttonListener buttonListener = new buttonListener();
    
    private boolean hasLaunchedActivities = false;

    private final View.OnClickListener plusMinusBtnListener = view -> plusMinusUpdates(view.getId(), false);
    private final View.OnLongClickListener plusMinusBtnLongListener = view -> plusMinusUpdates(view.getId(), true);

    private boolean plusMinusUpdates(int id, boolean longClick){
        int sets = Prefs.getInt(Constants.KEY_SETS, Constants.DEF_SETS);
        int workTime = Prefs.getInt(Constants.KEY_WORK, Constants.DEF_WORKTIME);
        int restTime = Prefs.getInt(Constants.KEY_REST, Constants.DEF_RESTTIME);
        int[] data = Utils.getValues(new int[]{sets, workTime, restTime, id}, longClick, this);
        Prefs.putInt(Constants.KEY_SETS, data[0]);
        Prefs.putInt(Constants.KEY_WORK, data[1]);
        Prefs.putInt(Constants.KEY_REST, data[2]);
        setTexts();
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setupLang(this);
        setContentView(R.layout.amaztimer);
        this.init();
        //Start listening for hr to avoid some time without HR values when started
        hrSensor.initialize(listener -> {});
        hrSensor.getInstance().onMainActCreate(this);

        setupBtnListener();

        for(Button b : plusMinusBtns){
            b.setOnClickListener(plusMinusBtnListener);
            b.setOnLongClickListener(plusMinusBtnLongListener);
        }

        start.setOnClickListener(view -> {
            if(Prefs.getBoolean(Constants.KEY_REPSCOUNT, false))
                new StartExerciseDialog(view.getContext()).show();
            else
                Utils.startTimer(this, false);
        });

        start.setOnLongClickListener(view -> launchIntent(new Intent(view.getContext(), SettingsActivity.class)));
    }

    private void setTexts(){
        sets.setText(String.valueOf(Prefs.getInt(Constants.KEY_SETS, Constants.DEF_SETS)));
        work.setText(Utils.formatTime(Prefs.getInt(Constants.KEY_WORK, Constants.DEF_WORKTIME)));
        rest.setText(Utils.formatTime(Prefs.getInt(Constants.KEY_REST, Constants.DEF_RESTTIME)));
    }

    private void init() {
        plusMinusBtns = new Button[]{
                findViewById(R.id.plus),
                findViewById(R.id.plus2),
                findViewById(R.id.plus3),
                findViewById(R.id.minus2),
                findViewById(R.id.minus),
                findViewById(R.id.minus3)
        };
        start = findViewById(R.id.start);
        sets = findViewById(R.id.sets);
        rest = findViewById(R.id.rest);
        work = findViewById(R.id.work);
    }

    public void onStart() {
        super.onStart();
        hasLaunchedActivities = false;
        setTexts();
    }

    public void onStop() {
        super.onStop();
        buttonListener.stop();
    }

    public void onPause() {
        buttonListener.stop();
        super.onPause();
    }

     public void onDestroy(){
        super.onDestroy();
        hrSensor.getInstance().onMainActDestroy(this);
     }

    public void onResume() {
        hasLaunchedActivities = false;
        hrSensor.getInstance().onMainActCreate(this);
        setupBtnListener();
        super.onResume();
    }

    private boolean launchIntent(Intent intent){
        if(hasLaunchedActivities) return true; //Avoid multiple activities launched
        Utils.vibrate(Constants.HAPTIC_VIBRATION, this);
        hasLaunchedActivities = true;
        buttonListener.stop();
        startActivity(intent);
        return true;
    }

    private void setupBtnListener(){
        Handler handler = new Handler();
        buttonListener.start(this, ButtonEvent -> {
            if(ButtonEvent.getKey() == buttonEvent.KEY_DOWN)
                handler.post(() -> start.performClick());
            else if(ButtonEvent.getKey() == buttonEvent.KEY_CENTER)
                handler.post(() -> start.performLongClick());
        });
    }
}

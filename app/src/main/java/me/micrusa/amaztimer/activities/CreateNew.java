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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.View.OnLongClickListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.activities.saved.SavedTimerRun;
import me.micrusa.amaztimer.database.AmazTimerDB;
import me.micrusa.amaztimer.database.DBUtils;
import me.micrusa.amaztimer.database.objects.Timer;
import me.micrusa.amaztimer.utils.Utils;

public class CreateNew extends AppCompatActivity {

    private Button[] plusMinusBtns;
    private Button save, start;
    private SwitchCompat hr;
    private TextView sets, work, rest;
    private final OnClickListener plusMinusBtnListener = view -> plusMinusUpdates(view.getId(), false);
    private final OnLongClickListener plusMinusBtnLongListener = view -> plusMinusUpdates(view.getId(), true);
    private int setsCount = 8, workTime = 30, restTime = 20;

    private boolean plusMinusUpdates(int id, boolean longClick){
        int[] data = Utils.getValues(new int[]{setsCount, workTime, restTime, id}, longClick, this);
        setsCount = data[0];
        workTime = data[1];
        restTime = data[2];
        setTexts();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new);
        init();
        setClickListeners();
        setTexts();
    }

    private void init(){
        plusMinusBtns = new Button[]{
                findViewById(R.id.minus1),
                findViewById(R.id.minus2),
                findViewById(R.id.minus3),
                findViewById(R.id.plus1),
                findViewById(R.id.plus2),
                findViewById(R.id.plus3)
        };
        start = findViewById(R.id.create_new_start);
        save = findViewById(R.id.create_new_save);
        hr = findViewById(R.id.create_new_hr_switch);

        sets = findViewById(R.id.sets_count);
        work = findViewById(R.id.work_count);
        rest = findViewById(R.id.rest_count);
    }

    private void setClickListeners(){
        for(Button btn : plusMinusBtns){
            btn.setOnClickListener(plusMinusBtnListener);
            btn.setOnLongClickListener(plusMinusBtnLongListener);
        }
        start.setOnClickListener(view -> {
            SavedTimerRun t = new SavedTimerRun();
            t.sets = setsCount;
            t.work = workTime;
            t.rest = restTime;
            t.heartrate = hr.isChecked();
            Utils.start(this, t);
        });
        save.setOnClickListener(view -> {
            AmazTimerDB db = DBUtils.createInstance();
            List<Timer> timers = db.timerDao().getAll();
            Timer t = new Timer();
            t.name = getString(R.string.custom) + " " + (timers.size() + 1);
            t.sets = setsCount;
            t.work = workTime;
            t.rest = restTime;
            t.heartrate = hr.isChecked();
            db.timerDao().insertAll(t);
            db.close();
        });
    }

    private void setTexts(){
        sets.setText(String.valueOf(setsCount));
        work.setText(Utils.formatTime(workTime));
        rest.setText(Utils.formatTime(restTime));
    }
}
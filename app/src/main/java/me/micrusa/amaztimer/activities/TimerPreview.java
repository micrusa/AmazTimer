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

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.activities.saved.SavedTimerRun;
import me.micrusa.amaztimer.utils.Utils;
import me.micrusa.amaztimer.utils.button.selector.ButtonSelector;

public class TimerPreview extends AppCompatActivity {
    private TextView name, sets, work, rest, hr;
    private ButtonSelector buttonSelector;
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_preview);
        init(SavedTimerRun.fromIntent(getIntent()));
    }

    @SuppressLint("SetTextI18n")
    private void init(SavedTimerRun t){
        name = findViewById(R.id.timer_preview_name);
        sets = findViewById(R.id.timer_preview_sets);
        work = findViewById(R.id.timer_preview_work);
        rest = findViewById(R.id.timer_preview_rest);
        hr = findViewById(R.id.timer_preview_hr);
        start = findViewById(R.id.timer_preview_start);
        buttonSelector = new ButtonSelector(new Button[]{start}, this);
        buttonSelector.startListening();

        name.setText(t.name);
        sets.setText(String.valueOf(t.sets));
        work.setText(Utils.formatTime(t.work));
        rest.setText(Utils.formatTime(t.rest));
        hr.setText(t.heartrate ? R.string.enabled : R.string.disabled);
        start.setOnClickListener(view -> Utils.start(this, t));
    }

    public void onStop() {
        buttonSelector.stopListening();
        super.onStop();
    }

    public void onPause() {
        buttonSelector.stopListening();
        super.onPause();
    }

    public void onResume() {
        buttonSelector.startListening();
        super.onResume();
    }
}
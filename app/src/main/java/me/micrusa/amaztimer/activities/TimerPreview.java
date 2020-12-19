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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.activities.saved.SavedTimerRun;
import me.micrusa.amaztimer.utils.Utils;

public class TimerPreview extends AppCompatActivity {
    private TextView name, sets, work, rest, hr;
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

        name.setText(t.name);
        sets.setText(getString(R.string.sets) + ": " + t.sets);
        work.setText(getString(R.string.work) + ": " + t.work);
        rest.setText(getString(R.string.rest) + ": " + t.rest);
        hr.setText(getString(R.string.hr) + ": " + getString(t.heartrate ? R.string.enabled : R.string.disabled));
        start.setOnClickListener(view -> Utils.start(this, t));
    }
}
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

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.utils.sensors.heartrate.hrUtils;
import me.micrusa.amaztimer.utils.utils;

public class LatestTrainActivity extends AppCompatActivity {

    private TextView avghr, minhr, maxhr, kcal, hrzone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils.setupLang(this);
        setContentView(R.layout.activity_latest_train);
        this.init();
    }

    private void init(){
        avghr = findViewById(R.id.avghr);
        minhr = findViewById(R.id.minhr);
        maxhr = findViewById(R.id.maxhr);
        kcal = findViewById(R.id.kcal);
        hrzone = findViewById(R.id.hrzone);

        //Set all texts
        avghr.setText(String.valueOf(Prefs.getInt(Constants.KEY_AVGHR, 0)));
        maxhr.setText(String.valueOf(Prefs.getInt(Constants.KEY_MAXHR, 0)));
        minhr.setText(String.valueOf(Prefs.getInt(Constants.KEY_MINHR, 0)));
        kcal.setText(String.valueOf(Prefs.getInt(Constants.KEY_KCAL, 0)));
        hrzone.setText(hrUtils.hrZonePercentage(Prefs.getInt(Constants.KEY_AVGHR, 0)));
    }
}

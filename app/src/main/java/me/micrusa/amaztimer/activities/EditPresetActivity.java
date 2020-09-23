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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.utils.utils;

public class EditPresetActivity extends AppCompatActivity {

    private Button[] plusMinusBtns;
    private Button edit;
    private TextView settingstext, sets, rest, work;
    private OnClickListener plusMinusBtn;
    private OnLongClickListener longPlusMinusBtn;
    private OnClickListener editBtn;
    private int PresetID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils.setupLang(this);
        setContentView(R.layout.amaztimer);
        PresetID = getIntent().getIntExtra("ID", 0);
        this.init();
        this.createOnClickListeners();
        this.setOnClickListeners();
        this.startActivity();
    }

    private void createOnClickListeners() {
        plusMinusBtn = v -> click(v.getId(), false);
        longPlusMinusBtn = v -> click(v.getId(), true);
        editBtn = v -> finish();
    }

    private boolean click(int btnId, boolean isLongPress){
        String presetKey = PresetID == 1 ? Constants.KEY_PRESET1 : Constants.KEY_PRESET2;
        String[] valuesStr = Prefs.getString(presetKey, "8:30:20").split(":");
        int sets = Integer.parseInt(valuesStr[0]);
        int work = Integer.parseInt(valuesStr[1]);
        int rest = Integer.parseInt(valuesStr[2]);
        int[] data = utils.getValues(new int[]{sets, work, rest, btnId}, isLongPress, this);
        Prefs.putString(presetKey, data[0] + ":" + data[1] + ":" + data[2]);
        setTimeTexts(data[0], data[1], data[2]);
        return true; //For a simpler code on long click
    }

    private void setOnClickListeners() {
        for(Button b : plusMinusBtns){
            b.setOnClickListener(plusMinusBtn);
            b.setOnLongClickListener(longPlusMinusBtn);
        }
        edit.setOnClickListener(editBtn);
    }

    private void init() {
        //Buttons
        plusMinusBtns = new Button[]{
                this.findViewById(R.id.plus),
                this.findViewById(R.id.plus2),
                this.findViewById(R.id.plus3),
                this.findViewById(R.id.minus2),
                this.findViewById(R.id.minus),
                this.findViewById(R.id.minus3)
        };
        edit = this.findViewById(R.id.start);
        //TextViews
        sets = this.findViewById(R.id.sets);
        rest = this.findViewById(R.id.rest);
        work = this.findViewById(R.id.work);
        settingstext = this.findViewById(R.id.textView);
        settingstext.setVisibility(View.GONE);
    }

    private void startActivity() {
        String presetKey = PresetID == 1 ? Constants.KEY_PRESET1 : Constants.KEY_PRESET2;
        String[] valuesStr = Prefs.getString(presetKey, "8:30:20").split(":");
        setTimeTexts(Integer.parseInt(valuesStr[0]),
                Integer.parseInt(valuesStr[1]),
                Integer.parseInt(valuesStr[2]));
    }

    private void setTimeTexts(int intSets, int intWork, int intRest) {
        sets.setText(String.valueOf(intSets));
        work.setText(utils.formatTime(intWork));
        rest.setText(utils.formatTime(intRest));
    }
}

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

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.utils.utils;

public class AppInfo extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils.setupLang(this);
        setContentView(R.layout.activity_app_info);
        this.init();
    }

    @SuppressLint("SetTextI18n")
    private void init(){
        TextView appText = findViewById(R.id.amaztimer);
        TextView appCredits = findViewById(R.id.appcredit);
        TextView translationCredits = findViewById(R.id.translationcredit);
        TextView thanksto = findViewById(R.id.thanksto);

        String NEWLINE = "\n- ";
        String FOR = getString(R.string.thanksfor);
        //Set texts
        appText.setText(appText.getText()
                + " " + Constants.VERSION_NAME + " (" + Constants.VERSION_CODE + ")");
        appCredits.setText(getString(R.string.appcredit));
        translationCredits.setText(getString(R.string.translationcredit));
        thanksto.setText(getString(R.string.thanksto)
                + NEWLINE + "@Quinny899 " + FOR + " Springboard Plugin Example"
                + NEWLINE + "@GreatApo " + FOR + " Widget Calendar"
                + NEWLINE + "@1immortal " + FOR + " AmazTimer installer"
                + NEWLINE + "AmazMod team"
                + NEWLINE + getString(R.string.allcontributors));
    }
}

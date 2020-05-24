package me.micrusa.amaztimer.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.utils;

public class EditPresetActivity extends AppCompatActivity {

    private Button plus, plus2, plus3, minus, minus2, minus3, edit;
    private TextView sets, rest, work, settingstext, setsText, workText, restText;
    private OnClickListener plusMinusBtn;
    private OnClickListener editBtn;
    private final me.micrusa.amaztimer.defValues defValues = new defValues();
    private final me.micrusa.amaztimer.utils.utils utils = new utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amaztimer);
        final int PresetID = getIntent().getIntExtra("ID", 0);
        //Finish activity if received wrong ID (<=0 or >=3)
        if(PresetID <= 0 | PresetID >= 3){
            finish();
        }
        this.init();
        this.createOnClickListeners(PresetID);
        this.setOnClickListeners();
        this.startActivity(PresetID);
    }

    private void createOnClickListeners(final int PresetID) {
        final me.micrusa.amaztimer.utils.file finalFile = new file("preset" + PresetID, this);
        plusMinusBtn = new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get values from file
                int sets = finalFile.get(defValues.SETTINGS_SETS, defValues.DEF_SETS);
                int work = finalFile.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME);
                int rest = finalFile.get(defValues.SETTINGS_REST, defValues.DEF_RESTTIME);
                //Increase or decrease values
                switch (v.getId()) {
                    case R.id.plus:
                        sets++;
                        break;
                    case R.id.plus2:
                        work++;
                        break;
                    case R.id.plus3:
                        rest++;
                        break;
                    case R.id.minus2:
                        sets--;
                        break;
                    case R.id.minus:
                        work--;
                        break;
                    case R.id.minus3:
                        rest--;
                        break;
                    default:
                        break;
                }
                //Save to file and set texts
                utils.pushToFile(finalFile, sets, work, rest);
                setTimeTexts(sets, work, rest);
            }
        };
        //Edit button finishes activity
        editBtn = new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
    }

    private void setOnClickListeners() {
        plus.setOnClickListener(plusMinusBtn);
        plus2.setOnClickListener(plusMinusBtn);
        plus3.setOnClickListener(plusMinusBtn);
        minus.setOnClickListener(plusMinusBtn);
        minus2.setOnClickListener(plusMinusBtn);
        minus3.setOnClickListener(plusMinusBtn);
        edit.setOnClickListener(editBtn);
    }

    private void init() {
        //Buttons
        plus = this.findViewById(R.id.plus);
        plus2 = this.findViewById(R.id.plus2);
        plus3 = this.findViewById(R.id.plus3);
        minus = this.findViewById(R.id.minus2);
        minus2 = this.findViewById(R.id.minus);
        minus3 = this.findViewById(R.id.minus3);
        edit = this.findViewById(R.id.start);
        //TextViews
        sets = this.findViewById(R.id.sets);
        rest = this.findViewById(R.id.rest);
        work = this.findViewById(R.id.work);
        settingstext = this.findViewById(R.id.textView);
        setsText = this.findViewById(R.id.textView4);
        workText = this.findViewById(R.id.textView5);
        restText = this.findViewById(R.id.textView6);
    }

    private void startActivity(int PresetID) {
        //Set lang
        utils.setLang(this, new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
        //Make settings text invisible
        settingstext.setVisibility(View.GONE);
        //Set all texts again
        Resources res = this.getResources();
        setsText.setText(res.getString(R.string.sets));
        workText.setText(res.getString(R.string.work));
        restText.setText(res.getString(R.string.rest));
        edit.setText(res.getString(R.string.edit));
        //Set times to values in file
        file file = new file("preset" + PresetID, this);
        setTimeTexts(file.get(defValues.SETTINGS_SETS, defValues.DEF_SETS),
                file.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME),
                file.get(defValues.SETTINGS_REST, defValues.DEF_RESTTIME));
    }

    private void setTimeTexts(int intSets, int intWork, int intRest) {
        sets.setText(String.valueOf(intSets));
        work.setText(utils.formatTime(intWork));
        rest.setText(utils.formatTime(intRest));
    }
}

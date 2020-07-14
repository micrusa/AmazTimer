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
import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.utils;

public class EditPresetActivity extends AppCompatActivity {

    private Button plus, plus2, plus3, minus, minus2, minus3, edit;
    private TextView settingstext, sets, rest, work;
    private OnClickListener plusMinusBtn;
    private OnLongClickListener longPlusMinusBtn;
    private OnClickListener editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils.setupPrefs(this);
        utils.setLang(this, Prefs.getString(defValues.KEY_LANG, "en"));
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
        plusMinusBtn = v -> {
            String presetKey = PresetID == 1 ? defValues.KEY_PRESET1 : defValues.KEY_PRESET2;
            String valuesStr = Prefs.getString(presetKey, "8:30:20");
            int sets = Integer.parseInt(valuesStr.split(":")[0]);
            int work = Integer.parseInt(valuesStr.split(":")[1]);
            int rest = Integer.parseInt(valuesStr.split(":")[2]);
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
            Prefs.putString(presetKey, sets + ":" + work + ":" + rest);
            setTimeTexts(sets, work, rest);
        };

        longPlusMinusBtn = v -> {
            String presetKey = PresetID == 1 ? defValues.KEY_PRESET1 : defValues.KEY_PRESET2;
            String valuesStr = Prefs.getString(presetKey, "8:30:20");
            int sets = Integer.parseInt(valuesStr.split(":")[0]);
            int work = Integer.parseInt(valuesStr.split(":")[1]);
            int rest = Integer.parseInt(valuesStr.split(":")[2]);
            //Increase or decrease values
            switch (v.getId()) {
                case R.id.plus:
                    sets = sets + 5;
                    break;
                case R.id.plus2:
                    work = work + 60;
                    break;
                case R.id.plus3:
                    rest = rest + 60;
                    break;
                case R.id.minus2:
                    sets = sets - 5;
                    break;
                case R.id.minus:
                    work = work - 60;
                    break;
                case R.id.minus3:
                    rest = rest - 60;
                    break;
                default:
                    break;
            }
            //Save to file and set texts
            Prefs.putString(presetKey, sets + ":" + work + ":" + rest);
            setTimeTexts(sets, work, rest);
            return true;
        };
        //Edit button finishes activity
        editBtn = v -> finish();
    }

    private void setOnClickListeners() {
        plus.setOnClickListener(plusMinusBtn);
        plus2.setOnClickListener(plusMinusBtn);
        plus3.setOnClickListener(plusMinusBtn);
        minus.setOnClickListener(plusMinusBtn);
        minus2.setOnClickListener(plusMinusBtn);
        minus3.setOnClickListener(plusMinusBtn);
        plus.setOnLongClickListener(longPlusMinusBtn);
        plus2.setOnLongClickListener(longPlusMinusBtn);
        plus3.setOnLongClickListener(longPlusMinusBtn);
        minus.setOnLongClickListener(longPlusMinusBtn);
        minus2.setOnLongClickListener(longPlusMinusBtn);
        minus3.setOnLongClickListener(longPlusMinusBtn);
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
    }

    private void startActivity(int PresetID) {
        //Make settings text invisible
        settingstext.setVisibility(View.GONE);
        //Set times to values in file
        setTimeTexts(Prefs.getInt(defValues.KEY_SETS, defValues.DEF_SETS),
                Prefs.getInt(defValues.KEY_WORK, defValues.DEF_WORKTIME),
                Prefs.getInt(defValues.KEY_REST, defValues.DEF_RESTTIME));
    }

    private void setTimeTexts(int intSets, int intWork, int intRest) {
        sets.setText(String.valueOf(intSets));
        work.setText(utils.formatTime(intWork));
        rest.setText(utils.formatTime(intRest));
    }
}

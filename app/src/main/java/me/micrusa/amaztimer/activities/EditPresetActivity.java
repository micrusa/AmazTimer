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
    private int PresetID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils.setupPrefs(this);
        utils.setLang(this, Prefs.getString(defValues.KEY_LANG, "en"));
        setContentView(R.layout.amaztimer);
        PresetID = getIntent().getIntExtra("ID", 0);
        if(PresetID <= 0 | PresetID >= 3)
            finish();
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

    private boolean click(int res, boolean isLongPress){
        String presetKey = PresetID == 1 ? defValues.KEY_PRESET1 : defValues.KEY_PRESET2;
        String valuesStr = Prefs.getString(presetKey, "8:30:20");
        int sets = Integer.parseInt(valuesStr.split(":")[0]);
        int work = Integer.parseInt(valuesStr.split(":")[1]);
        int rest = Integer.parseInt(valuesStr.split(":")[2]);
        int[] data = utils.getValues(new int[]{sets, work, rest, res}, isLongPress, this);
        Prefs.putString(presetKey, data[0] + ":" + data[1] + ":" + data[2]);
        setTimeTexts(data[0], data[1], data[2]);
        return true; //For a simpler code on long click
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
        settingstext.setVisibility(View.GONE);
    }

    private void startActivity() {
        String presetKey = PresetID == 1 ? defValues.KEY_PRESET1 : defValues.KEY_PRESET2;
        String valuesStr = Prefs.getString(presetKey, "8:30:20");
        int sets = Integer.parseInt(valuesStr.split(":")[0]);
        int work = Integer.parseInt(valuesStr.split(":")[1]);
        int rest = Integer.parseInt(valuesStr.split(":")[2]);
        setTimeTexts(sets, work, rest);
    }

    private void setTimeTexts(int intSets, int intWork, int intRest) {
        sets.setText(String.valueOf(intSets));
        work.setText(utils.formatTime(intWork));
        rest.setText(utils.formatTime(intRest));
    }
}

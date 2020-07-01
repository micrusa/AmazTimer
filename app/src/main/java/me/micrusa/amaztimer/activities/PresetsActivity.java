package me.micrusa.amaztimer.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.micrusa.amaztimer.AmazTimer;
import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.utils;

@SuppressWarnings("FieldCanBeLocal")
public class PresetsActivity extends AppCompatActivity {

    private file file1, file2;

    private TextView preset1, preset2;
    private Button start1, start2, edit1, edit2;
    private final String textFormat = "SETS" + ": %s\n"
            + "WORK" + ": %t "
            + "REST" + ": %r";
    private int[] firstArray;
    private int[] secondArray;

    private final View.OnClickListener startClickListener = v -> {
        int[] array = null;
        //Get correct array by the id
        switch (v.getId()) {
            case R.id.start1:
                array = getValues(1);
                break;
            case R.id.start2:
                array = getValues(2);
                break;
            default:
                break;
        }
        //If array is null return
        if(array == null) return;
        file f = new file(defValues.TIMER_FILE, v.getContext());
        //Save values to timer file
        utils.pushToFile(f, array[0], array[1], array[2]);
        //Open main class
        Intent intent = new Intent(v.getContext(), AmazTimer.class);
        v.getContext().startActivity(intent);
    };

    private final View.OnClickListener editClickListener = v -> {
        Intent intent = new Intent(v.getContext(), EditPresetActivity.class);
        switch (v.getId()) {
            case R.id.edit1:
                intent.putExtra("ID", 1);
                break;
            case R.id.edit2:
                intent.putExtra("ID", 2);
                break;
            default:
                break;
        }
        v.getContext().startActivity(intent);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presets);
        this.firstArray = new int[3];
        this.secondArray = new int[3];
        this.init();
        setupValues();
    }

    private void init() {
        //Setup files
        this.file1 = new file("preset1", this);
        this.file2 = new file("preset2", this);
        //Setup objects
        preset1 = this.findViewById(R.id.preset1);
        preset2 = this.findViewById(R.id.preset2);
        start1 = this.findViewById(R.id.start1);
        start2 = this.findViewById(R.id.start2);
        edit1 = this.findViewById(R.id.edit1);
        edit2 = this.findViewById(R.id.edit2);
        //Set onClickListeners
        start1.setOnClickListener(startClickListener);
        start2.setOnClickListener(startClickListener);
        edit1.setOnClickListener(editClickListener);
        edit2.setOnClickListener(editClickListener);
    }

    private void setupValues() {
        //Set values
        this.firstArray[0] = file1.get(defValues.SETTINGS_SETS, defValues.DEF_SETS);
        this.firstArray[1] = file1.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME);
        this.firstArray[2] = file1.get(defValues.SETTINGS_REST, defValues.DEF_RESTTIME);
        this.secondArray[0] = file2.get(defValues.SETTINGS_SETS, defValues.DEF_SETS);
        this.secondArray[1] = file2.get(defValues.SETTINGS_WORK, defValues.DEF_WORKTIME);
        this.secondArray[2] = file2.get(defValues.SETTINGS_REST, defValues.DEF_RESTTIME);
        //Set texts from format
        Resources res = this.getResources();
        String text1 = textFormat
                .replace("SETS", res.getString(R.string.sets))
                .replace("WORK", res.getString(R.string.work))
                .replace("REST", res.getString(R.string.rest))
                .replace("%s", String.valueOf(this.firstArray[0]))
                .replace("%t", utils.formatTime(this.firstArray[1]))
                .replace("%r", utils.formatTime(this.firstArray[2]));
        preset1.setText(text1);
        String text2 = textFormat
                .replace("SETS", res.getString(R.string.sets))
                .replace("WORK", res.getString(R.string.work))
                .replace("REST", res.getString(R.string.rest))
                .replace("%s", String.valueOf(this.secondArray[0]))
                .replace("%t", utils.formatTime(this.secondArray[1]))
                .replace("%r", utils.formatTime(this.secondArray[2]));
        preset2.setText(text2);
    }

    private int[] getValues(int i) {
        if(i == 1) {
            return this.firstArray;
        } else {
            return this.secondArray;
        }
    }

}

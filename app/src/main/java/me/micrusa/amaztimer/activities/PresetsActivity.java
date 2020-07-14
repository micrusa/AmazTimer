package me.micrusa.amaztimer.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.AmazTimer;
import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.utils;

@SuppressWarnings("FieldCanBeLocal")
public class PresetsActivity extends AppCompatActivity {

    private TextView preset1, preset2;
    private Button start1, start2, edit1, edit2;
    private final String textFormat = "SETS" + ": %s\n"
            + "WORK" + ": %t "
            + "REST" + ": %r";
    private int[] firstArray = new int[3];
    private int[] secondArray = new int[3];

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
        //Save data
        Prefs.putInt(defValues.KEY_SETS, array[0]);
        Prefs.putInt(defValues.KEY_WORK, array[1]);
        Prefs.putInt(defValues.KEY_REST, array[2]);
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
        this.init();
        setupValues();
    }

    private void init() {
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
        String valuesStr = Prefs.getString(defValues.KEY_PRESET1, "8:30:20");
        this.firstArray[0] = Integer.parseInt(valuesStr.split(":")[0]);
        this.firstArray[1] = Integer.parseInt(valuesStr.split(":")[1]);;
        this.firstArray[2] = Integer.parseInt(valuesStr.split(":")[2]);
        valuesStr = Prefs.getString(defValues.KEY_PRESET1, "8:30:20");
        this.secondArray[0] = Integer.parseInt(valuesStr.split(":")[0]);
        this.secondArray[1] = Integer.parseInt(valuesStr.split(":")[1]);
        this.secondArray[2] = Integer.parseInt(valuesStr.split(":")[2]);
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

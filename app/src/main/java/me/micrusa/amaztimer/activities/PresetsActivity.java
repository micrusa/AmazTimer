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
import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.utils.utils;

@SuppressWarnings("FieldCanBeLocal")
public class PresetsActivity extends AppCompatActivity {

    private static final String textFormat = "SETS" + ": %s\n"
            + "WORK" + ": %t "
            + "REST" + ": %r";

    private TextView preset1, preset2;
    private Button start1, start2, edit1, edit2;
    private int[] firstArray, secondArray;

    private final View.OnClickListener startClickListener = v -> {
        //Get correct array by the id
        int[] array = getArrayFromInt(v.getId() == R.id.edit1 ? 1 : 2);
        if(array == null) return;
        //Save data
        Prefs.putInt(Constants.KEY_SETS, array[0]);
        Prefs.putInt(Constants.KEY_WORK, array[1]);
        Prefs.putInt(Constants.KEY_REST, array[2]);
        //Open main class
        Intent intent = new Intent(v.getContext(), AmazTimer.class);
        v.getContext().startActivity(intent);
    };

    private final View.OnClickListener editClickListener = v -> {
        Intent intent = new Intent(v.getContext(), EditPresetActivity.class);
        intent.putExtra("ID", v.getId() == R.id.edit1 ? 1 : 2);
        v.getContext().startActivity(intent);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presets);
        this.init();
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

        this.firstArray = getArrayFromPref(Constants.KEY_PRESET1);
        this.secondArray = getArrayFromPref(Constants.KEY_PRESET2);
        preset1.setText(getText(this.firstArray));
        preset2.setText(getText(this.secondArray));
    }

    private int[] getArrayFromPref(String preference){
        String prefValues[] = Prefs.getString(preference, "8:30:20").split(":");
        return new int[]{
                Integer.parseInt(prefValues[0]),
                Integer.parseInt(prefValues[1]),
                Integer.parseInt(prefValues[2])
        };
    }

    private String getText(int[] array){
        Resources res = this.getResources();
        return textFormat
                .replace("SETS", res.getString(R.string.sets))
                .replace("WORK", res.getString(R.string.work))
                .replace("REST", res.getString(R.string.rest))
                .replace("%s", String.valueOf(array[0]))
                .replace("%t", utils.formatTime(array[1]))
                .replace("%r", utils.formatTime(array[2]));
    }

    private int[] getArrayFromInt(int i) {
        return i == 1 ? this.firstArray : this.secondArray;
    }

}

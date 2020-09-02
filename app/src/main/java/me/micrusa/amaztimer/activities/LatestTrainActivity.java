package me.micrusa.amaztimer.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.utils.heartrate.hrUtils;
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

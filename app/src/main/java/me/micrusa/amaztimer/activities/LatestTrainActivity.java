package me.micrusa.amaztimer.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.heartrate.hrUtils;
import me.micrusa.amaztimer.utils.utils;

public class LatestTrainActivity extends AppCompatActivity {

    private TextView avghr, minhr, maxhr, kcal, hrzone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils.setupPrefs(this);
        utils.setLang(this, Prefs.getString(defValues.KEY_LANG, "en"));
        setContentView(R.layout.activity_latest_train);
        this.init();
        setTexts();
    }

    private void init(){
        //Setup all textviews
        avghr = findViewById(R.id.avghr);
        minhr = findViewById(R.id.minhr);
        maxhr = findViewById(R.id.maxhr);
        kcal = findViewById(R.id.kcal);
        hrzone = findViewById(R.id.hrzone);
    }

    private void setTexts(){
        //Set all values texts
        avghr.setText(String.valueOf(Prefs.getInt(defValues.KEY_AVGHR, defValues.DEFAULT_HR_VALUES)));
        maxhr.setText(String.valueOf(Prefs.getInt(defValues.KEY_MAXHR, defValues.DEFAULT_HR_VALUES)));
        minhr.setText(String.valueOf(Prefs.getInt(defValues.KEY_MINHR, defValues.DEFAULT_HR_VALUES)));
        kcal.setText(String.valueOf(Prefs.getInt(defValues.KEY_KCAL, defValues.DEFAULT_HR_VALUES)));
        hrzone.setText(hrUtils.hrZonePercentage(Prefs.getInt(defValues.KEY_AVGHR, defValues.DEFAULT_HR_VALUES)));
    }


}

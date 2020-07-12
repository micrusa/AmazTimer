package me.micrusa.amaztimer.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.utils;

public class LatestTrainActivity extends AppCompatActivity {

    private TextView avghr, minhr, maxhr, kcal, hrzone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils.setLang(this, new file(defValues.SETTINGS_FILE).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
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
        file file = new file(defValues.LATEST_TRAIN_FILE);
        avghr.setText(String.valueOf(file.get(defValues.SETTINGS_AVGHR, defValues.DEFAULT_HR_VALUES)));
        maxhr.setText(String.valueOf(file.get(defValues.SETTINGS_MAXHR, defValues.DEFAULT_HR_VALUES)));
        minhr.setText(String.valueOf(file.get(defValues.SETTINGS_MINHR, defValues.DEFAULT_HR_VALUES)));
        kcal.setText(String.valueOf(file.get(defValues.SETTINGS_KCAL, defValues.DEFAULT_HR_VALUES)));
        hrzone.setText(utils.hrZonePercentage(file.get(defValues.SETTINGS_AVGHR, defValues.DEFAULT_HR_VALUES)));
    }


}

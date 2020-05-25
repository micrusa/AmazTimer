package me.micrusa.amaztimer.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.utils;

public class LatestTrainActivity extends AppCompatActivity {
    private final me.micrusa.amaztimer.defValues defValues = new defValues();
    private final me.micrusa.amaztimer.utils.utils utils = new utils();

    private TextView avghrText, maxhrText, minhrText, latestTrainText, hrzoneText, avghr, minhr, maxhr, kcal, hrzone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_train);
        this.init();
        setupLang();
        setTexts();
    }

    private void init(){
        //Setup all textviews
        avghrText = findViewById(R.id.avghrText);
        maxhrText = findViewById(R.id.maxhrText);
        minhrText = findViewById(R.id.minhrText);
        latestTrainText = findViewById(R.id.latestTrainText);
        hrzoneText = findViewById(R.id.hrzoneText);
        avghr = findViewById(R.id.avghr);
        minhr = findViewById(R.id.minhr);
        maxhr = findViewById(R.id.maxhr);
        kcal = findViewById(R.id.kcal);
        hrzone = findViewById(R.id.hrzone);
    }

    private void setupLang(){
        //Set language and set all texts again
        utils.setLang(this, new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
        avghrText.setText(getResources().getString(R.string.averagehr));
        maxhrText.setText(getResources().getString(R.string.maxhr));
        minhrText.setText(getResources().getString(R.string.minhr));
        latestTrainText.setText(getResources().getString(R.string.latesttrain));
        hrzoneText.setText(getResources().getString(R.string.hrzone));
    }

    private void setTexts(){
        //Set all values texts
        file file = new file(defValues.LATEST_TRAIN_FILE, this);
        avghr.setText(String.valueOf(file.get(defValues.SETTINGS_AVGHR, defValues.DEFAULT_HR_VALUES)));
        maxhr.setText(String.valueOf(file.get(defValues.SETTINGS_MAXHR, defValues.DEFAULT_HR_VALUES)));
        minhr.setText(String.valueOf(file.get(defValues.SETTINGS_MINHR, defValues.DEFAULT_HR_VALUES)));
        kcal.setText(String.valueOf(file.get(defValues.SETTINGS_KCAL, defValues.DEFAULT_HR_VALUES)));
        hrzone.setText(hrZonePercentage(file.get(defValues.SETTINGS_AVGHR, defValues.DEFAULT_HR_VALUES)));
    }

    private String hrZonePercentage(int avgHr){
        file bodyFile = new file(defValues.BODY_FILE, this);
        if(avgHr == 0 | bodyFile.get(defValues.SETTINGS_AGE, defValues.DEFAULT_AGE) == 0){
            return "--";
        }
        //noinspection UnnecessaryCallToStringValueOf
        return String.valueOf(avgHr * 100 /
                (220 - bodyFile.get(defValues.SETTINGS_AGE, defValues.DEFAULT_AGE)))
                + "%";
    }
}

package me.micrusa.amaztimer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.utils;
import me.micrusa.app.amazwidgets.R;

public class LatestTrainActivity extends AppCompatActivity {
    private me.micrusa.amaztimer.defValues defValues = new defValues();
    private me.micrusa.amaztimer.utils.utils utils = new utils();

    private TextView avghrText, maxhrText, minhrText, latestTrainText, avghr, minhr, maxhr, kcal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_train);
        this.init();
        setupLang();
    }

    private void init(){
        avghrText = findViewById(R.id.avghrText);
        maxhrText = findViewById(R.id.maxhrText);
        minhrText = findViewById(R.id.minhrText);
        latestTrainText = findViewById(R.id.latestTrainText);
        avghr = findViewById(R.id.avghr);
        minhr = findViewById(R.id.minhr);
        maxhr = findViewById(R.id.maxhr);
        kcal = findViewById(R.id.kcal);
    }

    private void setupLang(){
        utils.setLang(this, new file(defValues.settingsFile, this).get(defValues.sLang, defValues.LangDefault));
        Resources res = this.getResources();
        avghrText.setText(res.getString(R.string.averagehr));
        maxhrText.setText(res.getString(R.string.maxhr));
        minhrText.setText(res.getString(R.string.minhr));
        latestTrainText.setText(res.getString(R.string.latesttrain));
    }
}

package me.micrusa.amaztimer.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.utils;

public class AppInfo extends AppCompatActivity {

    private me.micrusa.amaztimer.utils.utils utils = new utils();
    private me.micrusa.amaztimer.defValues defValues = new defValues();

    private TextView appText, appCredits, translationCredits, thanksto;

    private String NEWLINE = "\n";
    private String SPACE = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        utils.setLang(this, new file(defValues.settingsFile, this).get(defValues.sLang, defValues.LangDefault));
        this.init();
    }

    @SuppressLint("SetTextI18n")
    private void init(){
        //TextViews
        appText = findViewById(R.id.amaztimer);
        appCredits = findViewById(R.id.appcredit);
        translationCredits = findViewById(R.id.translationcredit);
        thanksto = findViewById(R.id.thanksto);
        //Set texts
        appText.setText(appText.getText()
                + SPACE + utils.getVersionName(this));
        appCredits.setText(getResources().getString(R.string.appcredit));
        translationCredits.setText(getResources().getString(R.string.translationcredit));
        thanksto.setText(getResources().getString(R.string.thanksto)
                + NEWLINE + "@Quinny899" + SPACE + getResources().getString(R.string.thanksfor) + SPACE + "Springboard Plugin Example"
                + NEWLINE + "@GreatApo" + SPACE + getResources().getString(R.string.thanksfor) + SPACE + "Widget Calendar");
    }
}

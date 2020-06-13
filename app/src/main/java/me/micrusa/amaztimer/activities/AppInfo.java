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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        utils.setLang(this, new file(defValues.SETTINGS_FILE, this).get(defValues.SETTINGS_LANG, defValues.DEFAULT_LANG));
        this.init();
    }

    @SuppressLint("SetTextI18n")
    private void init(){
        //TextViews
        TextView appText = findViewById(R.id.amaztimer);
        TextView appCredits = findViewById(R.id.appcredit);
        TextView translationCredits = findViewById(R.id.translationcredit);
        TextView thanksto = findViewById(R.id.thanksto);
        //Define SPACE and NEWLINE
        String SPACE = " ";
        String NEWLINE = "\n";
        //Set texts
        appText.setText(appText.getText()
                + SPACE + defValues.VERSION_NAME + SPACE + "(" + defValues.VERSION_CODE + ")");
        appCredits.setText(getResources().getString(R.string.appcredit));
        translationCredits.setText(getResources().getString(R.string.translationcredit));
        thanksto.setText(getResources().getString(R.string.thanksto)
                + NEWLINE + "@Quinny899" + SPACE + getResources().getString(R.string.thanksfor) + SPACE + "Springboard Plugin Example"
                + NEWLINE + "@GreatApo" + SPACE + getResources().getString(R.string.thanksfor) + SPACE + "Widget Calendar"
                + NEWLINE + getResources().getString(R.string.allcontributors));
    }
}

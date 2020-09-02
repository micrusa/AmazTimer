package me.micrusa.amaztimer.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.utils.utils;

public class AppInfo extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils.setupLang(this);
        setContentView(R.layout.activity_app_info);
        this.init();
    }

    @SuppressLint("SetTextI18n")
    private void init(){
        TextView appText = findViewById(R.id.amaztimer);
        TextView appCredits = findViewById(R.id.appcredit);
        TextView translationCredits = findViewById(R.id.translationcredit);
        TextView thanksto = findViewById(R.id.thanksto);

        String SPACE = " ";
        String NEWLINE = "\n- ";
        String FOR = getResources().getString(R.string.thanksfor);
        //Set texts
        appText.setText(appText.getText()
                + SPACE + Constants.VERSION_NAME + SPACE + "(" + Constants.VERSION_CODE + ")");
        appCredits.setText(getResources().getString(R.string.appcredit));
        translationCredits.setText(getResources().getString(R.string.translationcredit));
        thanksto.setText(getResources().getString(R.string.thanksto)
                + NEWLINE + "@Quinny899" + SPACE + FOR + SPACE + "Springboard Plugin Example"
                + NEWLINE + "@GreatApo" + SPACE + FOR + SPACE + "Widget Calendar"
                + NEWLINE + "@1immortal" + SPACE + FOR + SPACE + "AmazTimer installer"
                + NEWLINE + "AmazMod team"
                + NEWLINE + getResources().getString(R.string.allcontributors));
    }
}

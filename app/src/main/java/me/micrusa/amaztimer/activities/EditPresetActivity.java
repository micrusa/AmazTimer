package me.micrusa.amaztimer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import me.micrusa.app.amazwidgets.R;

public class EditPresetActivity extends AppCompatActivity {

    private Button plus, plus2, plus3, minus, minus2, minus3, edit;
    private TextView sets,rest,work,settingstext,setsText,workText,restText;
    private ConstraintLayout L1, L2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amaztimer);
        final int PresetID = getIntent().getIntExtra("ID", 0);
        this.init();
    }

    private void init(){
        //Buttons
        plus = this.findViewById(R.id.plus);
        plus2 = this.findViewById(R.id.plus2);
        plus3 = this.findViewById(R.id.plus3);
        minus = this.findViewById(R.id.minus2);
        minus2 = this.findViewById(R.id.minus);
        minus3 = this.findViewById(R.id.minus3);
        edit = this.findViewById(R.id.start);
        //TextViews
        sets = this.findViewById(R.id.sets);
        rest = this.findViewById(R.id.rest);
        work = this.findViewById(R.id.work);
        settingstext = this.findViewById(R.id.textView);
        setsText = this.findViewById(R.id.textView4);
        workText = this.findViewById(R.id.textView5);
        restText = this.findViewById(R.id.textView6);
        //Layouts
        L1 = this.findViewById(R.id.startScreen);
        L2 = this.findViewById(R.id.timerScreen);
    }
}

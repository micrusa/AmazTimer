package me.micrusa.amaztimer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import me.micrusa.amaztimer.defValues;
import me.micrusa.amaztimer.utils.file;
import me.micrusa.amaztimer.utils.utils;
import me.micrusa.app.amazwidgets.R;

public class PresetsActivity extends AppCompatActivity {

    private file file, file1, file2, file3;
    private me.micrusa.amaztimer.defValues defValues = new defValues();
    private me.micrusa.amaztimer.utils.utils utils = new utils();

    private TextView preset1, preset2, preset3;
    private Button start1, start2, start3, edit1, edit2, edit3;
    private int sets1, sets2, sets3, work1, work2, work3, rest1, rest2, rest3;
    private String textFormat = this.getResources().getString(R.string.sets) + ": %s\\n"
            + this.getResources().getString(R.string.work) + ": %t "
            + this.getResources().getString(R.string.rest) + ": %r";

    private View.OnClickListener startClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = 0;
            if(v.getId()==R.id.start1){
                id = 1;
            }else if(v.getId()==R.id.start2){
                id = 2;
            }else if(v.getId()==R.id.start3){
                id = 3;
            }
            file f = new file(defValues.timerFile, v.getContext());
            int[] array = getValues(id);
            pushToFile(f, array[0], array[1], array[2]);
            finishActivity();
        }
    };

    private View.OnClickListener editClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = 0;
            if(v.getId()==R.id.edit1){
                id = 1;
            }else if(v.getId()==R.id.edit2){
                id = 2;
            }else if(v.getId()==R.id.edit3){
                id = 3;
            }
            Intent intent = new Intent(v.getContext(), PresetsActivity.class);
            intent.putExtra("ID", id);
            v.getContext().startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presets);
        this.init();
        start1.setOnClickListener(startClickListener);
        start2.setOnClickListener(startClickListener);
        start3.setOnClickListener(startClickListener);
        edit1.setOnClickListener(editClickListener);
        edit2.setOnClickListener(editClickListener);
        edit3.setOnClickListener(editClickListener);
        setupValues();
    }

    private void init(){
        preset1 = this.findViewById(R.id.preset1);
        preset2 = this.findViewById(R.id.preset2);
        preset3 = this.findViewById(R.id.preset3);
        start1 = this.findViewById(R.id.start1);
        start2 = this.findViewById(R.id.start2);
        start3 = this.findViewById(R.id.start3);
        edit1 = this.findViewById(R.id.edit1);
        edit2 = this.findViewById(R.id.edit2);
        edit3 = this.findViewById(R.id.edit3);
    }

    private void setupValues(){
        //Setup files
        this.file = new file(defValues.timerFile, this);
        this.file1 = new file("preset1", this);
        this.file2 = new file("preset2", this);
        this.file3 = new file("preset3", this);
        //Set values
        this.sets1 = file1.get(defValues.sSets, defValues.defSets);
        this.sets2 = file2.get(defValues.sSets, defValues.defSets);
        this.sets3 = file3.get(defValues.sSets, defValues.defSets);
        this.work1 = file1.get(defValues.sWork, defValues.defWorkTime);
        this.work2 = file2.get(defValues.sWork, defValues.defWorkTime);
        this.work3 = file3.get(defValues.sWork, defValues.defWorkTime);
        this.rest1 = file1.get(defValues.sRest, defValues.defRestTime);
        this.rest2 = file2.get(defValues.sRest, defValues.defRestTime);
        this.rest3 = file3.get(defValues.sRest, defValues.defRestTime);
        //Set texts
        String text1 = this.textFormat
                .replace("%s", String.valueOf(this.sets1))
                .replace("%t", utils.sToMinS(this.work1))
                .replace("%r", utils.sToMinS(this.rest1));
        preset1.setText(text1);
        String text2 = this.textFormat
                .replace("%s", String.valueOf(this.sets2))
                .replace("%t", utils.sToMinS(this.work2))
                .replace("%r", utils.sToMinS(this.rest2));
        preset2.setText(text2);
        String text3 = this.textFormat
                .replace("%s", String.valueOf(this.sets3))
                .replace("%t", utils.sToMinS(this.work3))
                .replace("%r", utils.sToMinS(this.rest3));
        preset3.setText(text3);
    }

    private void pushToFile(file f, int sets, int work, int rest){
        f.set(defValues.sSets, sets);
        f.set(defValues.sWork, work);
        f.set(defValues.sRest, rest);
    }

    private int[] getValues(int i){
        int[] array = new int[2];
        if(i == 1){
            array[0] = this.sets1;
            array[1] = this.work1;
            array[2] = this.rest1;
        } else if(i == 2){
            array[0] = this.sets2;
            array[1] = this.work2;
            array[2] = this.rest2;
        } else if(i == 3){
            array[0] = this.sets3;
            array[1] = this.work3;
            array[2] = this.rest3;
        }
        return array;
    }

    private void finishActivity(){
        this.finishAndRemoveTask();
    }

}

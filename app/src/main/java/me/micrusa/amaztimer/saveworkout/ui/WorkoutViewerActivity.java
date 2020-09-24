/*
 * MIT License
 *
 * Copyright (c) 2020 Miguel Cruces
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.micrusa.amaztimer.saveworkout.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.saveworkout.database.AmazTimerDB;
import me.micrusa.amaztimer.saveworkout.database.DBUtils;
import me.micrusa.amaztimer.saveworkout.database.objects.Workout;
import me.micrusa.amaztimer.utils.sensors.heartrate.hrUtils;

public class WorkoutViewerActivity extends AppCompatActivity {

    private TextView duration, kcal, minhr, maxhr, avghr;
    private LineChart hrGraph, repsGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_viewer);
        init();

        Bundle bundle = getIntent().getExtras();

        long time = bundle.getLong("id");
        final Handler handler = new Handler();
        new Thread(() -> {
            AmazTimerDB database = DBUtils.createDBInstance();

            Workout workout = database.workoutDao().findByTime(time);

            database.close();

            handler.post(() -> setDataFromWorkout(workout));
        }).start();
    }

    private void init(){
        duration = findViewById(R.id.workout_duration);
        kcal = findViewById(R.id.workout_kcal);
        minhr = findViewById(R.id.workout_minhr);
        maxhr = findViewById(R.id.workout_maxhr);
        avghr = findViewById(R.id.workout_avghr);
        hrGraph = findViewById(R.id.HrGraphView);
        repsGraph = findViewById(R.id.SetsGraphView);
    }

    private void setDataFromWorkout(Workout workout){
        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss", Locale.US);
        format1.setTimeZone(TimeZone.getTimeZone("GMT"));
        String durationText = format1.format(new Date(workout.totalTime * 1000));
        duration.setText(durationText);

        if(workout.hr.size() >= 1){
            kcal.setText(String.valueOf(workout.kcal));
            minhr.setText(String.valueOf(Collections.min(workout.hr)));
            maxhr.setText(String.valueOf(Collections.max(workout.hr)));
            avghr.setText(String.valueOf(hrUtils.getAvg(workout.hr)));
            setupHrGraph(workout.hr);
        }

        setupRepsGraph(workout.setsReps, workout.sets);
    }

    private void setupHrGraph(List<Integer> hr){
        ArrayList<Entry> hrArray = new ArrayList<>();
        for(int i = 0; i < hr.size(); i++)
            hrArray.add(new Entry(i, hr.get(i)));

        LineDataSet line = new LineDataSet(hrArray, getString(R.string.heartrate));
        line.setDrawIcons(false);
        line.setDrawCircles(false);
        line.setDrawValues(false);
        line.setColor(Color.RED);
        line.setLineWidth(1f);
        line.setDrawFilled(true);
        line.setFillColor(Color.RED);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(line);

        setupGraph(hrGraph, false, new LineData(dataSets));
    }

    private void setupRepsGraph(List<Integer> reps, List<Integer> setsDuration){
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        if(reps.size() >= 1){
            ArrayList<Entry> repsArray = new ArrayList<>();
            for(int i = 0; i < reps.size(); i++)
                repsArray.add(new Entry(i, reps.get(i)));

            LineDataSet RepsLine = new LineDataSet(repsArray, getString(R.string.reps));
            setupLineDataSet(RepsLine, Color.BLUE);
            dataSets.add(RepsLine);
        }

        if(setsDuration.size() >= 1){
            LineDataSet WorkSetsDurationLine = new LineDataSet(getSetsDurationArray(setsDuration, true), getString(R.string.worktime));
            setupLineDataSet(WorkSetsDurationLine, Color.RED);
            dataSets.add(WorkSetsDurationLine);

            LineDataSet RestSetsDurationLine = new LineDataSet(getSetsDurationArray(setsDuration, false), getString(R.string.resttime));
            setupLineDataSet(RestSetsDurationLine, Color.GREEN);
            dataSets.add(RestSetsDurationLine);
        }

        if(dataSets.size() >= 1)
            setupGraph(repsGraph, true, new LineData(dataSets));
    }

    private ArrayList<Entry> getSetsDurationArray(List<Integer> setsDuration, boolean work){
        ArrayList<Entry> workValues = new ArrayList<>();
        ArrayList<Entry> restValues = new ArrayList<>();
        for(int i = 0; i < setsDuration.size(); i++){
            if(i % 2 == 0) //If it's a work set
                workValues.add(new Entry((float) i / 2, setsDuration.get(i)));
            else
                restValues.add(new Entry((float) i / 2, setsDuration.get(i)));
        }
        return work ? workValues : restValues;
    }

    private void setupGraph(LineChart graph, boolean enableX, LineData data){
        graph.setData(data);
        graph.getLegend().setTextColor(Color.WHITE);
        graph.getAxisLeft().setTextColor(Color.WHITE);
        graph.getXAxis().setTextColor(Color.WHITE);
        graph.getDescription().setEnabled(false);
        graph.getXAxis().setEnabled(enableX);
        graph.getAxisRight().setEnabled(false);
        graph.setDragEnabled(false);
        graph.setTouchEnabled(false);
        if(enableX) graph.setScaleX(1f);
    }

    private void setupLineDataSet(LineDataSet line, int color){
        line.setDrawIcons(false);
        line.setDrawCircles(false);
        line.setDrawValues(false);
        line.setColor(color);
        line.setLineWidth(1f);
    }
}
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
import androidx.room.Room;

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

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.saveworkout.database.AmazTimerDB;
import me.micrusa.amaztimer.saveworkout.database.DBConstants;
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
            AmazTimerDB database = Room
                    .databaseBuilder(getApplicationContext(), AmazTimerDB.class, DBConstants.DB_NAME)
                    .build();

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
        ArrayList<Entry> values = new ArrayList<>();
        for(int i = 0; i < hr.size(); i++)
            values.add(new Entry(i, hr.get(i)));

        LineDataSet line = new LineDataSet(values, getString(R.string.heartrate));
        line.setDrawIcons(false);
        line.setDrawCircles(false);
        line.setDrawValues(false);
        line.setColor(Color.RED);
        line.setLineWidth(1f);
        line.setDrawFilled(true);
        line.setFillColor(Color.RED);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(line);

        hrGraph.setData(new LineData(dataSets));

        hrGraph.getLegend().setTextColor(Color.WHITE);
        hrGraph.getAxisLeft().setTextColor(Color.WHITE);
        hrGraph.getDescription().setEnabled(false);
        hrGraph.getXAxis().setEnabled(false);
        hrGraph.getAxisRight().setEnabled(false);
        hrGraph.setDragEnabled(false);
        hrGraph.setTouchEnabled(false);
    }

    private void setupRepsGraph(List<Integer> reps, List<Integer> setsDuration){
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        if(reps.get(0) != 0){
            ArrayList<Entry> values = new ArrayList<>();
            for(int i = 0; i < reps.size(); i++)
                values.add(new Entry(i, reps.get(i)));

            LineDataSet RepsLine = new LineDataSet(values, getString(R.string.reps));
            RepsLine.setDrawIcons(false);
            RepsLine.setDrawCircles(false);
            RepsLine.setDrawValues(false);
            RepsLine.setColor(Color.BLUE);
            RepsLine.setLineWidth(1f);
            dataSets.add(RepsLine);
        }

        ArrayList<Entry> workValues = new ArrayList<>();
        ArrayList<Entry> restValues = new ArrayList<>();
        for(int i = 0; i < setsDuration.size(); i++){
            if(i % 2 == 0) //If it's a work set
                workValues.add(new Entry((float) i / 2, setsDuration.get(i)));
            else
                restValues.add(new Entry((float) i / 2, setsDuration.get(i)));
        }

        LineDataSet WorkSetsDurationLine = new LineDataSet(workValues, getString(R.string.worktime));
        WorkSetsDurationLine.setDrawIcons(false);
        WorkSetsDurationLine.setDrawCircles(false);
        WorkSetsDurationLine.setDrawValues(false);
        WorkSetsDurationLine.setColor(Color.RED);
        WorkSetsDurationLine.setLineWidth(1f);
        dataSets.add(WorkSetsDurationLine);

        LineDataSet RestSetsDurationLine = new LineDataSet(workValues, getString(R.string.resttime));
        RestSetsDurationLine.setDrawIcons(false);
        RestSetsDurationLine.setDrawCircles(false);
        RestSetsDurationLine.setDrawValues(false);
        RestSetsDurationLine.setColor(Color.GREEN);
        RestSetsDurationLine.setLineWidth(1f);
        dataSets.add(RestSetsDurationLine);

        repsGraph.setData(new LineData(dataSets));

        repsGraph.getLegend().setTextColor(Color.WHITE);
        repsGraph.getAxisLeft().setTextColor(Color.WHITE);
        repsGraph.getXAxis().setTextColor(Color.WHITE);
        repsGraph.getDescription().setEnabled(false);
        repsGraph.getAxisRight().setEnabled(false);
        repsGraph.setDragEnabled(false);
        repsGraph.setTouchEnabled(false);
    }
}
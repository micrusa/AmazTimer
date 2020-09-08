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

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.List;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.saveworkout.database.AmazTimerDB;
import me.micrusa.amaztimer.saveworkout.database.DBConstants;
import me.micrusa.amaztimer.saveworkout.database.objects.Workout;

public class SavedWorkoutsActivity extends AppCompatActivity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_workouts);

        lv = findViewById(R.id.saved_workouts_lv);

        final Handler handler = new Handler();
        new Thread(() -> {
            AmazTimerDB db = Room
                    .databaseBuilder(getApplicationContext(), AmazTimerDB.class, DBConstants.DB_NAME)
                    .build();

            List<Workout> workouts = db.workoutDao().getAll();

            handler.post(() -> {
                lv.setAdapter(new WorkoutAdapter(this, workouts));

                lv.setOnItemClickListener((adapterView, view, i, l) -> {
                    Workout workout = (Workout) adapterView.getItemAtPosition(i);
                    Intent intent = new Intent(this, WorkoutViewerActivity.class);
                    intent.putExtra("id", workout.time);
                    startActivity(intent);
                });

                lv.setOnItemLongClickListener((adapterView, view, i, l) -> {
                    Workout workout = (Workout) adapterView.getItemAtPosition(i);
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.deleteworkout)
                            .setPositiveButton(R.string.yes, (di, i1) -> {
                                new Thread(() -> {
                                    AmazTimerDB database = Room
                                            .databaseBuilder(getApplicationContext(), AmazTimerDB.class, DBConstants.DB_NAME)
                                            .build();

                                    database.workoutDao().delete(workout);

                                    database.close();
                                }).start();
                            })
                            .setNegativeButton(R.string.no, (dialogI, i1) -> dialogI.dismiss())
                            .create().show();
                    return true;
                });
            });
        }).start();
    }
}
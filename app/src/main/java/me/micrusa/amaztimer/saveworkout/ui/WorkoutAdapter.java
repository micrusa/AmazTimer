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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.saveworkout.database.objects.Workout;
import me.micrusa.amaztimer.utils.sensors.heartrate.hrUtils;

public class WorkoutAdapter extends ArrayAdapter<Workout> {

    public WorkoutAdapter(Context context, List<Workout> workouts){
        super(context, 0, workouts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Workout workout = getItem(position);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_workout, parent, false);

        TextView title = convertView.findViewById(R.id.workout_item_title);
        TextView duration = convertView.findViewById(R.id.workout_item_duration);
        TextView hr = convertView.findViewById(R.id.workout_item_hr);

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US);
        String titleText = format.format(new Date(workout.time));
        title.setText(titleText);

        SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss", Locale.US);
        format1.setTimeZone(TimeZone.getTimeZone("GMT"));
        String durationText = format1.format(new Date(workout.totalTime * 1000));
        duration.setText(durationText);

        String hrText = hrUtils.getAvg(workout.hr) + "bpm";
        hr.setText(hrText);

        return convertView;
    }

}

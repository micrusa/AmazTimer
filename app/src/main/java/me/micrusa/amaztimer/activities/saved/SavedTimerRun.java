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

package me.micrusa.amaztimer.activities.saved;

import android.content.Intent;

import androidx.room.ColumnInfo;

import me.micrusa.amaztimer.database.objects.Timer;

public class SavedTimerRun {
    public String name;
    public int sets;
    public int work;
    public int rest;
    public int mode;
    public boolean heartrate;
    public boolean gps;

    public static SavedTimerRun fromTimer(Timer t){
        SavedTimerRun r = new SavedTimerRun();
        r.name = t.name;
        r.sets = t.sets;
        r.work = t.work;
        r.rest = t.rest;
        r.mode = t.mode;
        r.heartrate = t.heartrate;
        r.gps = t.gps;
        return r;
    }

    public static SavedTimerRun fromIntent(Intent i){
        SavedTimerRun r = new SavedTimerRun();
        r.name = i.getStringExtra("name");
        r.sets = i.getIntExtra("sets", 8);
        r.work = i.getIntExtra("work", 30);
        r.rest = i.getIntExtra("rest", 20);
        r.mode = i.getIntExtra("mode", 0);
        r.heartrate = i.getBooleanExtra("hr", true);
        r.gps = i.getBooleanExtra("gps", false);
        return r;
    }

    public Intent toIntent(Intent i){
        i.putExtra("name", name);
        i.putExtra("sets", sets);
        i.putExtra("work", work);
        i.putExtra("rest", rest);
        i.putExtra("mode", mode);
        i.putExtra("hr", heartrate);
        i.putExtra("gps", gps);
        return i;
    }
}

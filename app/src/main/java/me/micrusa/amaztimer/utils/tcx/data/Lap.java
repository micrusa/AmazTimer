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

package me.micrusa.amaztimer.utils.tcx.data;

import java.util.Date;
import java.util.LinkedList;

import me.micrusa.amaztimer.utils.tcx.TCXUtils;
import me.micrusa.amaztimer.utils.sensors.heartrate.hrUtils;

public class Lap {

    private LinkedList<Trackpoint> tps = new LinkedList<>();
    private String StartTime;
    private String intensity;
    private long longStartTime;
    private long timeInSecs;
    private int kcal = 0;

    public Lap(){
        this.longStartTime = System.currentTimeMillis();
        this.StartTime = TCXUtils.formatDate(new Date());
    }

    public void endLap(long currentTimeMillis){
        this.timeInSecs = (currentTimeMillis - this.longStartTime) / 1000;
    }

    public void addTrackpoint(Trackpoint tp){
        this.tps.add(tp);
    }

    public String getStartTime(){
        return this.StartTime;
    }

    public long getTimeInSeconds(){
        return this.timeInSecs;
    }

    public int getAvgHr(){
        int totalhr = 0;
        for (Trackpoint trackpoint : this.tps){
            totalhr = totalhr + trackpoint.getHr();
        }
        if(this.tps.size() != 0)
            return totalhr / this.tps.size();
        else
            return 0;

    }

    public void calcCalories(){
        this.kcal = hrUtils.calculateKcal(getAvgHr(), (int) getTimeInSeconds());
    }

    public int getKcal(){
        return this.kcal;
    }

    public int getMaxHr(){
        int max = 0;
        for (Trackpoint trackpoint : this.tps){
            if(trackpoint.getHr() > max)
                max = trackpoint.getHr();
        }
        return max;
    }

    public LinkedList<Trackpoint> getTrackpoints(){
        return this.tps;
    }

    public boolean isLapEmpty(){
        return this.tps.size() == 0;
    }

    public void setIntensity(String Intensity){
        this.intensity = Intensity;
    }

    public String getIntensity(){
        return this.intensity;
    }

}

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

package me.micrusa.amaztimer.utils.sensors.heartrate;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.utils.prefUtils;

public class hrUtils {
    public static String hrZonePercentage(int hr){
        return hrZonePercentageInt(hr) + "%";
    }

    public static int hrZonePercentageInt(int hr){
        if(hr == 0 | prefUtils.getAge() == 0)
            return 0;

        //Source: https://www.ntnu.edu/cerg/hrmax#:~:text=Based%20on%20these%20tests%20we,211%20%2D%200.64*age%22.
        return (int) (hr * 100 / getMaxHr());
    }

    public static int calculateKcal(int avgHr, int time){
        int age = prefUtils.getAge();
        int weight = prefUtils.getWeight();
        boolean isMale = prefUtils.isMale();
        if(avgHr==0||time==0||age==0||weight==0){return 0;}
        double kcalPerMin;
        //Formula from https://www.calculatorpro.com/calculator/calories-burned-by-heart-rate/
        if(isMale){
            kcalPerMin = (-55.0969 + (0.6309 * avgHr) + (0.1988 * weight) + (0.2017 * age)) / 4.184;
        }else{
            kcalPerMin = (-20.4022 + (0.4472 * avgHr) + (0.1263 * weight) + (0.074) * age) / 4.184;
        }
        //Calculate kcal from kcal/min
        return (int) (kcalPerMin * time) / 60;
    }
    
    public static double getMaxHr(){
        return 211 - 0.64 * prefUtils.getAge();
    }

    public static int getAvg(List<Integer> hr){
        int totalHr = 0;
        for (int value : hr) {
            totalHr += value;
        }
        if(hr.size() != 0)
            return totalHr / hr.size();
        else
            return 0;
    }
}

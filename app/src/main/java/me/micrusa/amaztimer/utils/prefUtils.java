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

package me.micrusa.amaztimer.utils;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.Constants;
import me.micrusa.amaztimer.TimerActivity;

public class prefUtils {

    public static int getWeight(){
        return Integer.parseInt(Prefs.getString(Constants.KEY_WEIGHT, String.valueOf(Constants.DEFAULT_WEIGHT)));
    }

    public static int getAge(){
        return Constants.CURRENT_YEAR - Integer.parseInt(Prefs.getString(Constants.KEY_AGE, String.valueOf(Constants.DEFAULT_AGE)));
    }

    public static boolean isMale(){
        return Boolean.parseBoolean(Prefs.getString(Constants.KEY_GENDER, "true"));
    }

    public static Class getTimerClass(){
        return TimerActivity.class;
    }

    public static int getVibration(int vibration){
        int multiplier = Prefs.getInt(Constants.KEY_VIBRATION, 2);
        
        //Disable multiplier on haptic vibrations
        if(vibration == Constants.HAPTIC_VIBRATION) return vibration;
        
        switch(multiplier){
            case 1:
                vibration = vibration / 2;
                break;
            case 3:
                vibration = vibration * 2;
                break;
            default:
                break;
        }
        return vibration;
    }

}

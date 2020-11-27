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

package me.micrusa.amaztimer.utils.devices;

import android.os.Build;

import org.tinylog.Logger;

import me.micrusa.amaztimer.utils.devices.AmazfitUtils;

public class SystemProperties {

    public static String getSystemProperty(String name) {
        return System.getProperty(name);
    }

    public static boolean getBoolean(String key, boolean def) {
        try {
            return Boolean.parseBoolean(System.getProperty(key, String.valueOf(def)));
        } catch (Exception e) {
            Logger.error(e);
            return def;
        }
    }

    public static String getDeviceName(){
        String device;
        if(AmazfitUtils.isPace())
            device = "Huami Amazfit Pace";
        else if(AmazfitUtils.isStratos())
            device = "Huami Amazfit Stratos";
        else if(AmazfitUtils.isVerge())
            device = "Huami Amazfit Verge";
        else if(AmazfitUtils.isStratos3())
            device = "Huami Amazfit Stratos 3";
        else
            device = Build.MODEL;
        Logger.debug("getDeviceName() detected device " + device);
        return device;
    }

}

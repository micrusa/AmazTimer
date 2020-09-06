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

import org.tinylog.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class SystemProperties {

    //Everything inside this class is from AmazMod.

    public static String getSystemProperty(String name) {
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"/system/bin/getprop", name});
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))){
                return reader.readLine();
            }
        } catch (IOException e) {
            Logger.error(e);
            return null;
        }
    }

    public static boolean getBoolean(String key, boolean def) {
        try {
            return (Boolean) Class.forName("android.os.SystemProperties").getMethod("getBoolean", String.class, boolean.class)
                    .invoke(null, key, def);
        } catch (Exception e) {
            Logger.error(e);
            return def;
        }
    }

    public static boolean isPace(){
        return checkIfModel(new String[]{"A1602", "A1612"}, "Pace") || new File("/system/.pace_hybrid").exists();
    }

    public static boolean isStratos(){
        return checkIfModel(new String[]{"A1609", "A1619"}, "Stratos") && !new File("/system/.pace_hybrid").exists();
    }

    public static boolean isVerge(){
        return checkIfModel(new String[]{"A1801", "A1811"}, "Verge");
    }

    public static boolean isStratos3(){
        return checkIfModel(new String[]{"A1928", "A1929"}, "Stratos 3");
    }

    public static boolean isStratosNewKeys(){
        return isStratos() && getBoolean("prop.keyfeature.five", false);
    }

    public static boolean checkIfModel(String[] targetModels, String Name){
        String model = getSystemProperty("ro.build.huami.model");
        boolean check = Arrays.asList(targetModels).contains(model);
        //Logger.info("Current model (" + model + ") is " + ((check) ? "" : "NOT ") + "a " + Name);
        return check;
    }

    public static String getDeviceName(){
        String device;
        if(isPace())
            device = "Huami Amazfit Pace";
        else if(isStratos())
            device = "Huami Amazfit Stratos";
        else if(isVerge())
            device = "Huami Amazfit Verge";
        else if(isStratos3())
            device = "Huami Amazfit Stratos 3";
        else
            device = "Unknown device";
        Logger.debug("getDeviceName() detected device " + device);
        return device;
    }

}

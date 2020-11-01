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

import java.io.File;
import java.util.Arrays;

public class AmazfitUtils {
    public static boolean isAmazfit(){
        return isPace() || isStratos() || isVerge() || isStratos3();
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
        return isStratos() && SystemProperties.getBoolean("prop.keyfeature.five", false);
    }

    public static boolean checkIfModel(String[] targetModels, String Name){
        String model = SystemProperties.getSystemProperty("ro.build.huami.model");
        boolean check = Arrays.asList(targetModels).contains(model);
        //Logger.info("Current model (" + model + ") is " + ((check) ? "" : "NOT ") + "a " + Name);
        return check;
    }
}

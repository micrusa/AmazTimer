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

package me.micrusa.amaztimer.utils.sensors.repsCounter.utils;

import java.util.HashMap;
import java.util.Scanner;

import me.micrusa.amaztimer.utils.sensors.repsCounter.RepsCounter;

public class PeaksChecker {

    public static HashMap<Double, Integer> get(double[] arr) {
        HashMap<Double, Integer> peaks = new HashMap<>();

        for (int i = 1; i < arr.length; i++)
            if (isPeak(arr, i, RepsCounter.PEAKS_POSITIONS_CHECK))
                peaks.put(arr[i], i);

        return peaks;
    }

    private static boolean isPeak(double[] arr, int i, int checkPositions){
        for(int x = 1; x <= checkPositions; x++){
            boolean peak = isPeakLoop(arr, i, x);
            if (!peak) return false;
        }
        return true;
    }

    private static boolean isPeakLoop(double[] arr, int i, int checkPos){
        return arr[i - checkPos] <= arr[i] && arr[i] >= arr[i + checkPos];
    }
}

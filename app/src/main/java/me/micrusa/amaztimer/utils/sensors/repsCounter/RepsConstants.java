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

package me.micrusa.amaztimer.utils.sensors.repsCounter;

import android.content.Context;

import java.util.ArrayList;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.utils.sensors.repsCounter.objects.Exercise;

public class RepsConstants {

    public static final int PEAK_CHECKING_INTERVAL = 1000; //1s

    public static final Exercise[] EXERCISES = new Exercise[]{
            new Exercise(R.string.bicepscurl, 13, 29, 2, 'X'),
            new Exercise(R.string.benchpress, 13, 27, 2, 'X'),
            new Exercise(R.string.crunches, 13, 27, 2, 'X'),
            new Exercise(R.string.pullups, 13, 25, 1, 'X'),
            new Exercise(R.string.jjacks, 13, 29, 2, 'X'),
            new Exercise(R.string.other, 13, 27, 2, 'X')
    };

}

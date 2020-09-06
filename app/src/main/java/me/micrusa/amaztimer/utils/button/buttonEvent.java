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

package me.micrusa.amaztimer.utils.button;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.Constants;

import static me.micrusa.amaztimer.utils.SystemProperties.isPace;
import static me.micrusa.amaztimer.utils.SystemProperties.isStratos;
import static me.micrusa.amaztimer.utils.SystemProperties.isStratos3;
import static me.micrusa.amaztimer.utils.SystemProperties.isStratosNewKeys;
import static me.micrusa.amaztimer.utils.SystemProperties.isVerge;

public class buttonEvent {

    public static final int KEY_UP = 0;
    public static final int KEY_CENTER = 1;
    public static final int KEY_DOWN = 2;

    static final int S3_KEY_UP = 3;
    static final int S3_KEY_MIDDLE_UP = 4;
    static final int S3_KEY_MIDDLE_DOWN = 5;

    private boolean isLongPress;
    private boolean isInverted;
    private int key;

    public buttonEvent(boolean IsLongPress, int Key){
        this.isLongPress = IsLongPress;
        this.key = Key;
        this.isInverted = Prefs.getBoolean(Constants.KEY_INVERTKEYS, false);
    }

    public boolean isLongPress(){
        return this.isLongPress;
    }

    public int getKey(){
        if(isPace() || isVerge() || (isStratosNewKeys() && key == KEY_CENTER)){ //Pace / Verge / Stratos new keys center button
            if(isLongPress()) //Long press = key down
                return isInverted ? KEY_UP : KEY_DOWN;
            else //Short press = upper key
                return isInverted ? KEY_DOWN : KEY_UP;
        } else if(isStratos3())
            switch(key){
                case S3_KEY_UP:
                    return isInverted ? KEY_UP : KEY_DOWN;
                case S3_KEY_MIDDLE_UP:
                    return isInverted ? KEY_DOWN : KEY_UP;
                case S3_KEY_MIDDLE_DOWN:
                    return KEY_CENTER;
            }
        else if(isStratos() && !isStratosNewKeys())
            switch(key){
                case KEY_UP:
                    return isInverted ? KEY_DOWN : KEY_UP;
                case KEY_CENTER:
                    return KEY_CENTER;
                case KEY_DOWN:
                    return isInverted ? KEY_UP : KEY_DOWN;
            }
        return -5; //If nothing returned return -5 to not trigger anything
    }

}

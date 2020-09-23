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
        int finalKey = -5; //If nothing changed return -5 to not trigger anything
        if(isSingleKey()){
            if(isLongPress())
                finalKey = KEY_DOWN;
            else
                finalKey = KEY_UP;
        } else if(isStratos3())
            switch(key){
                case S3_KEY_UP:
                    finalKey = KEY_DOWN;
                    break;
                case S3_KEY_MIDDLE_UP:
                    finalKey = KEY_UP;
                    break;
                case S3_KEY_MIDDLE_DOWN:
                    finalKey = KEY_CENTER;
                    break;
                default:
                    break;
            }
        else if(isStratos2Old())
            finalKey = key;

        return getInverted(finalKey);
    }

    private int getInverted(int key){
        if(!isInverted || key == KEY_CENTER)
            return key;

        if(key == KEY_UP)
            key = KEY_DOWN;
        else if(key == KEY_DOWN)
            key = KEY_UP;

        return key;
    }

    private boolean isSingleKey(){ //Stratos new layout can just use center button
        return isPace() || isVerge() || (isStratosNewKeys() && key == KEY_CENTER);
    }

    private boolean isStratos2Old(){
        return isStratos() && !isStratosNewKeys();
    }

}

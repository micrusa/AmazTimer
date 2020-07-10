package me.micrusa.amaztimer.button;

import static me.micrusa.amaztimer.utils.SystemProperties.isPace;
import static me.micrusa.amaztimer.utils.SystemProperties.isStratos;
import static me.micrusa.amaztimer.utils.SystemProperties.isStratos3;
import static me.micrusa.amaztimer.utils.SystemProperties.isStratosNewKeys;
import static me.micrusa.amaztimer.utils.SystemProperties.isVerge;

public class buttonEvent {

    public static final int KEY_UP = 0;
    public static final int KEY_CENTER = 1;
    public static final int KEY_DOWN = 2;

    public static final int S3_KEY_UP = 3;
    public static final int S3_KEY_MIDDLE_UP = 4;
    public static final int S3_KEY_MIDDLE_DOWN = 5;

    private boolean isLongPress;
    private int key;

    public buttonEvent(boolean IsLongPress, int Key){
        this.isLongPress = IsLongPress;
        this.key = Key;
    }

    public boolean isLongPress(){
        return this.isLongPress;
    }

    public int getKey(){
        if(isPace() || isVerge() || (isStratosNewKeys() && key == KEY_CENTER)){ //Pace / Verge / Stratos new keys center button
            if(isLongPress()) //Long press = key down
                return KEY_DOWN;
            else //Short press = upper key
                return KEY_UP;
        } else if(isStratos3())
            switch(key){
                case S3_KEY_UP:
                    return KEY_DOWN;
                case S3_KEY_MIDDLE_UP:
                    return KEY_UP;
                case S3_KEY_MIDDLE_DOWN:
                    return KEY_CENTER;
            }
        return this.key; //If nothing was returned return just the key, also old layout will return true key
    }

}

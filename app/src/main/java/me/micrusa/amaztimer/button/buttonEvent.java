package me.micrusa.amaztimer.button;

import static me.micrusa.amaztimer.utils.SystemProperties.isPace;
import static me.micrusa.amaztimer.utils.SystemProperties.isStratos;
import static me.micrusa.amaztimer.utils.SystemProperties.isStratosNewKeys;
import static me.micrusa.amaztimer.utils.SystemProperties.isVerge;

public class buttonEvent {

    public static final int KEY_UP = 0;
    public static final int KEY_CENTER = 1;
    public static final int KEY_DOWN = 2;

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
        if(isPace() || isVerge()){ //Pace / Verge
            if(isLongPress()) //Long press = key down
                return KEY_DOWN;
            else //Short press = upper key
                return KEY_UP;
        } else if(isStratos() && isStratosNewKeys()){ //Stratos with new keys
            if (this.key == KEY_CENTER) {
                if (this.isLongPress())
                    return KEY_DOWN;
                else
                    return KEY_UP;
            }
        }
        return this.key; //If nothing was returned return just the key, also old layout will return true key
    }

}

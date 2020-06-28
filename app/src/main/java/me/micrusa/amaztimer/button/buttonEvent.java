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
        if(!isStratos() || isStratosNewKeys()){
            if(this.key == KEY_CENTER){
                if(isLongPress())
                    return KEY_DOWN;
                else
                    return KEY_UP;
            } else if(this.key == KEY_DOWN)
                return KEY_CENTER;
            else
                return this.key;
        } else {
            if(this.key == KEY_DOWN) {
                if (isLongPress())
                    return KEY_DOWN;
                else
                    return KEY_UP;
            } else
                return this.key;
        }
    }

}

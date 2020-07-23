package me.micrusa.amaztimer.button;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.defValues;

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
        this.isInverted = Prefs.getBoolean(defValues.KEY_INVERTKEYS, false);
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

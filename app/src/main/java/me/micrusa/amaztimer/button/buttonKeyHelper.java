package me.micrusa.amaztimer.button;

import com.pixplicity.easyprefs.library.Prefs;

import me.micrusa.amaztimer.defValues;

import static me.micrusa.amaztimer.button.buttonListener.KEY_CENTER;
import static me.micrusa.amaztimer.button.buttonListener.KEY_DOWN;
import static me.micrusa.amaztimer.button.buttonListener.KEY_UP;
import static me.micrusa.amaztimer.button.buttonListener.S3_KEY_MIDDLE_DOWN;
import static me.micrusa.amaztimer.button.buttonListener.S3_KEY_MIDDLE_UP;
import static me.micrusa.amaztimer.button.buttonListener.S3_KEY_UP;
import static me.micrusa.amaztimer.utils.SystemProperties.isPace;
import static me.micrusa.amaztimer.utils.SystemProperties.isStratos;
import static me.micrusa.amaztimer.utils.SystemProperties.isStratos3;
import static me.micrusa.amaztimer.utils.SystemProperties.isStratosNewKeys;
import static me.micrusa.amaztimer.utils.SystemProperties.isVerge;

public class buttonKeyHelper {

    private static boolean isInverted;
    private static int finalKey;

    public static int getKey(buttonListener.KeyEvent event){
        int key = event.getCode();
        boolean longPress = event.isLongPress();
        isInverted = Prefs.getBoolean(defValues.KEY_INVERTKEYS, false);
        finalKey = -5;
        if(isMiddleKeyControl() && key == KEY_CENTER){
            setKeyMiddleControl(key, longPress);
        } else if(isStratos3())
            setKeyStratos3(key);
        else if(isStratosOldKeys())
            setKeyStratosOld(key);
        return finalKey;
    }

    private static boolean isMiddleKeyControl(){
        return isPace() || isVerge() || (isStratosNewKeys());
    }

    private static boolean isStratosOldKeys(){
        return isStratos() && !isStratosNewKeys();
    }

    private static void setKeyMiddleControl(int key, boolean longPress){
        if(longPress) //Long press = key down
            finalKey = isInverted ? KEY_UP : KEY_DOWN;
        else //Short press = upper key
            finalKey = isInverted ? KEY_DOWN : KEY_UP;
    }

    private static void setKeyStratos3(int key){
        switch(key){
            case S3_KEY_UP:
                finalKey = isInverted ? KEY_UP : KEY_DOWN;
            case S3_KEY_MIDDLE_UP:
                finalKey = isInverted ? KEY_DOWN : KEY_UP;
            case S3_KEY_MIDDLE_DOWN:
                finalKey = KEY_CENTER;
        }
    }

    private static void setKeyStratosOld(int key){
        switch(key){
            case KEY_UP:
                finalKey = isInverted ? KEY_DOWN : KEY_UP;
            case KEY_CENTER:
                finalKey = KEY_CENTER;
            case KEY_DOWN:
                finalKey = isInverted ? KEY_UP : KEY_DOWN;
        }
    }

}

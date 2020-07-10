package me.micrusa.amaztimer.button;

public class buttonStore {

    private static boolean isLocked = false;

    public static boolean isLocked(){
        return isLocked;
    }

    public static void lock(){
        isLocked = true;
    }

    public static void unlock(){
        isLocked = false;
    }

}

package me.micrusa.amaztimer.button;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

import org.tinylog.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import static me.micrusa.amaztimer.utils.SystemProperties.isStratos3;

import static android.content.Context.POWER_SERVICE;

//Big thanks to AmazMod team for this way of getting button presses

public class buttonListener {
    private final int TYPE_KEYBOARD = 1;

    public static final int KEY_DOWN = 64; //S3 key middle up
    public static final int KEY_CENTER = 116; //S3 key up
    public static final int KEY_UP = 63; //S3 key middle down

    private final int KEY_EVENT_UP = 0;
    private final int KEY_EVENT_PRESS = 1;

    private final int TRIGGER = 600;
    private final int LONG_TRIGGER = TRIGGER * 6;
    private final int LONG_TRIGGER_MAX = TRIGGER * 10;

    private PowerManager powerManager;
    PowerManager.WakeLock wakeLock;



    private boolean listening;

    Thread thread;

    public void start(Context context, final buttonInterface buttonInterface) {
        if(listening)
            return;

        powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);

        listening = true;
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "AmazTimer:buttonListener");
        wakeLock.acquire();
        thread = new listenerThread(buttonInterface);
        thread.start();
    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
            thread = null;
            listening = false;
        }
        if (wakeLock != null && wakeLock.isHeld())
            wakeLock.release();
    }

    private class listenerThread extends Thread{
        private String FILE_PATH;

        private long lastKeyDownKeyDown = 0;
        private long lastKeyCenterKeyUp = 0;
        private long lastKeyUpKeyUp = 0;

        private buttonInterface buttonInterface;

        private listenerThread(buttonInterface bInterface){
            if(isStratos3())
                FILE_PATH = "/dev/input/event1";
            else
                FILE_PATH = "/dev/input/event2";

            buttonInterface = bInterface;
        }

        public void run(){
            File file = new File(FILE_PATH);

            try {
                FileInputStream fileInputStream = new FileInputStream(file);

                while (true) {
                    if(Thread.currentThread().isInterrupted()){
                        fileInputStream.close();
                        break;
                    }
                    byte[] buffer = new byte[24];
                    fileInputStream.read(buffer, 0, 24);

                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(24);
                    byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                    byteBuffer.put(buffer);

                    int timeS = byteBuffer.getInt(0);
                    int timeMS = byteBuffer.getInt(4);
                    short type = byteBuffer.getShort(8);
                    short code = byteBuffer.getShort(10);
                    short value = byteBuffer.getShort(12);

                    if (type != TYPE_KEYBOARD) {
                        continue;
                    }

                    long now = System.currentTimeMillis();

                    int buttonEventKey;

                    switch (code) {
                        case KEY_DOWN: {
                            if (value == KEY_EVENT_UP) {
                                if(isStratos3())
                                    buttonEventKey = buttonEvent.S3_KEY_MIDDLE_UP;
                                else
                                    buttonEventKey = buttonEvent.KEY_DOWN;
                                long delta = now - lastKeyDownKeyDown;
                                if ((delta > TRIGGER) && (delta < LONG_TRIGGER)) {
                                    buttonInterface.onKeyEvent(new buttonEvent(true, buttonEventKey));
                                } else {
                                    if (delta < TRIGGER) {
                                        buttonInterface.onKeyEvent(new buttonEvent(false, buttonEventKey));
                                    }
                                }
                            } else if (value == KEY_EVENT_PRESS) {
                                lastKeyDownKeyDown = now;
                            }
                            break;
                        }
                        case KEY_CENTER: {
                            if (value == KEY_EVENT_UP) {
                                if(isStratos3())
                                    buttonEventKey = buttonEvent.S3_KEY_UP;
                                else
                                    buttonEventKey = buttonEvent.KEY_CENTER;
                                long delta = now - lastKeyCenterKeyUp;
                                if ((delta > TRIGGER) && (delta < LONG_TRIGGER)) {
                                    buttonInterface.onKeyEvent(new buttonEvent(true, buttonEventKey));
                                } else {
                                    if (delta < TRIGGER) {
                                        buttonInterface.onKeyEvent(new buttonEvent(false, buttonEventKey));
                                    }
                                }
                            } else if (value == KEY_EVENT_PRESS) {
                                lastKeyCenterKeyUp = now;
                            }
                            break;
                        }
                        case KEY_UP: {
                            if (value == KEY_EVENT_UP) {
                                if(isStratos3())
                                    buttonEventKey = buttonEvent.S3_KEY_MIDDLE_DOWN;
                                else
                                    buttonEventKey = buttonEvent.KEY_UP;
                                long delta = now - lastKeyUpKeyUp;
                                if ((delta > TRIGGER) && (delta < LONG_TRIGGER)) {
                                    buttonInterface.onKeyEvent(new buttonEvent(true, buttonEventKey));
                                } else {
                                    if (delta < TRIGGER) {
                                        buttonInterface.onKeyEvent(new buttonEvent(false, buttonEventKey));
                                    }
                                }
                            } else if (value == KEY_EVENT_PRESS) {
                                lastKeyUpKeyUp = now;
                            }
                            break;
                        }
                        default: {
                            Logger.debug("Unsupported key: " + code);
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                Logger.error(e);
            }
        }

    }

}

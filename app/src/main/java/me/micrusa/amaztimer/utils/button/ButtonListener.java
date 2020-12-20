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

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;

import org.tinylog.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static me.micrusa.amaztimer.utils.devices.AmazfitUtils.isAmazfit;
import static me.micrusa.amaztimer.utils.devices.AmazfitUtils.isStratos3;

import static android.content.Context.POWER_SERVICE;

//Big thanks to AmazMod team for this way of getting button presses

public class ButtonListener {
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
    private Handler handler;
    private PowerManager.WakeLock wakeLock;
    private Thread thread;

    public void start(Context context, final ButtonInterface buttonInterface) {
        if(isListening() || !isAmazfit()) return;

        handler = new Handler(context.getMainLooper());
        powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AmazTimer:buttonListener");
        wakeLock.acquire();
        thread = new listenerThread(buttonInterface);
        thread.start();
    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
        if (wakeLock != null && wakeLock.isHeld())
            wakeLock.release();
    }

    public boolean isListening(){
        return thread != null;
    }

    private class listenerThread extends Thread{
        private String FILE_PATH;

        private long lastKeyDownKeyDown = 0;
        private long lastKeyCenterKeyUp = 0;
        private long lastKeyUpKeyUp = 0;

        private ButtonInterface buttonInterface;

        private listenerThread(ButtonInterface bInterface){
            if(isStratos3())
                FILE_PATH = "/dev/input/event1";
            else
                FILE_PATH = "/dev/input/event2";

            buttonInterface = bInterface;
        }

        public void run(){
            try {
                FileInputStream fileInputStream = new FileInputStream(new File(FILE_PATH));

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

                    check(code, value);
                }
            } catch (IOException e) {
                Logger.error(e);
            }
        }

        private void check(int code, int value){
            long now = System.currentTimeMillis();
            int buttonEventKey;
            switch (code) {
                case KEY_DOWN: {
                    if (value == KEY_EVENT_UP) {
                        if(isStratos3())
                            buttonEventKey = ButtonEvent.S3_KEY_MIDDLE_UP;
                        else
                            buttonEventKey = ButtonEvent.KEY_DOWN;
                        sendKeyEvent(lastKeyDownKeyDown, buttonEventKey);
                    } else if (value == KEY_EVENT_PRESS) {
                        lastKeyDownKeyDown = now;
                    }
                    break;
                }
                case KEY_CENTER: {
                    if (value == KEY_EVENT_UP) {
                        if(isStratos3())
                            buttonEventKey = ButtonEvent.S3_KEY_UP;
                        else
                            buttonEventKey = ButtonEvent.KEY_SELECT;
                        sendKeyEvent(lastKeyCenterKeyUp, buttonEventKey);
                    } else if (value == KEY_EVENT_PRESS) {
                        lastKeyCenterKeyUp = now;
                    }
                    break;
                }
                case KEY_UP: {
                    if (value == KEY_EVENT_UP) {
                        if(isStratos3())
                            buttonEventKey = ButtonEvent.S3_KEY_MIDDLE_DOWN;
                        else
                            buttonEventKey = ButtonEvent.KEY_UP;
                        sendKeyEvent(lastKeyUpKeyUp, buttonEventKey);
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

        private void sendKeyEvent(long lastKeyDown, int buttonEventKey){
            long delta = System.currentTimeMillis() - lastKeyDown;
            if ((delta > TRIGGER) && (delta < LONG_TRIGGER)) {
                handler.post(() -> buttonInterface.onKeyEvent(new ButtonEvent(true, buttonEventKey)));
            } else {
                if (delta < TRIGGER) {
                    handler.post(() -> buttonInterface.onKeyEvent(new ButtonEvent(false, buttonEventKey)));
                }
            }
        }

    }

}

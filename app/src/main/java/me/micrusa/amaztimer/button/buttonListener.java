package me.micrusa.amaztimer.button;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import static android.content.Context.POWER_SERVICE;

//Big thanks to AmazMod team for this class

public class buttonListener {
    private final String FILE_PATH = "/dev/input/event2";

    private final int TYPE_KEYBOARD = 1;

    public static final int KEY_DOWN = 64;
    public static final int KEY_CENTER = 116;
    public static final int KEY_UP = 63;

    private final int KEY_EVENT_UP = 0;
    private final int KEY_EVENT_PRESS = 1;

    private final int TRIGGER = 500;
    private final int LONG_TRIGGER = TRIGGER * 4;
    private final int LONG_TRIGGER_MAX = TRIGGER * 10;



    private boolean listening;

    ExecutorService executor;

    public void start(final buttonInterface buttonInterface) {
        FutureTask<Void> futureTask = new FutureTask<>(new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                long lastKeyDownKeyDown = 0;
                long lastKeyCenterKeyUp = 0;
                long lastKeyUpKeyUp = 0;

                File file = new File(FILE_PATH);

                listening = true;

                try {
                    FileInputStream fileInputStream = new FileInputStream(file);

                    while (true) {
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

                        switch (code) {
                            case KEY_DOWN: {
                                if (value == KEY_EVENT_UP) {
                                    long delta = now - lastKeyDownKeyDown;
                                    if ((delta > TRIGGER) && (delta < LONG_TRIGGER)) {
                                        buttonInterface.onKeyEvent(new buttonEvent(true, buttonEvent.KEY_DOWN));
                                    } else {
                                        if (delta < TRIGGER) {
                                            buttonInterface.onKeyEvent(new buttonEvent(false, buttonEvent.KEY_DOWN));
                                        }
                                    }
                                } else if (value == KEY_EVENT_PRESS) {
                                    lastKeyDownKeyDown = now;
                                }
                                break;
                            }
                            case KEY_CENTER: {
                                if (value == KEY_EVENT_UP) {
                                    long delta = now - lastKeyCenterKeyUp;
                                    if ((delta > TRIGGER) && (delta < LONG_TRIGGER)) {
                                        buttonInterface.onKeyEvent(new buttonEvent(true, buttonEvent.KEY_CENTER));
                                    } else {
                                        if (delta < TRIGGER) {
                                            buttonInterface.onKeyEvent(new buttonEvent(false, buttonEvent.KEY_CENTER));
                                        }
                                    }
                                } else if (value == KEY_EVENT_PRESS) {
                                    lastKeyCenterKeyUp = now;
                                }
                                break;
                            }
                            case KEY_UP: {
                                if (value == KEY_EVENT_UP) {
                                    long delta = now - lastKeyUpKeyUp;
                                    if ((delta > TRIGGER) && (delta < LONG_TRIGGER)) {
                                        buttonInterface.onKeyEvent(new buttonEvent(true, buttonEvent.KEY_UP));
                                    } else {
                                        if (delta < TRIGGER) {
                                            buttonInterface.onKeyEvent(new buttonEvent(false, buttonEvent.KEY_UP));
                                        }
                                    }
                                } else if (value == KEY_EVENT_PRESS) {
                                    lastKeyUpKeyUp = now;
                                }
                                break;
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    Log.e("AmazTimer", "/dev/input/event2 not found");
                }

                return null;
            }
        });

        executor = Executors.newFixedThreadPool(1);
        executor.execute(futureTask);
    }

    public void stop() {
        if (executor != null) {
            executor.shutdown();
            executor = null;
            listening = false;
        }
    }

    public boolean isListening() {
        return listening;
    }
}

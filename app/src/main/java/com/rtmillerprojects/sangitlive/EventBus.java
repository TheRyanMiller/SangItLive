package com.rtmillerprojects.sangitlive;

import android.os.Looper;

import com.squareup.otto.Bus;

import android.os.Handler;
import android.os.Looper;
import java.util.logging.LogRecord;

/**
 * Created by Ryan on 8/27/2016.
 */
public class EventBus {

    private static final Handler mainThread = new Handler(Looper.getMainLooper());
    private static EventBus mInstance;
    private final Bus mBus;

    private EventBus() {
        // Don't let this class get instantiated directly.
        mBus = new Bus();
    }

    // @DebugLog intentionally omitted
    private static EventBus getInstance() {
        if (mInstance == null) {
            mInstance = new EventBus();
        }
        return mInstance;
    }

    public static void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            getInstance().mBus.post(event);
        } else {
            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    post(event);
                }
            });
        }

    }

    public static void register(Object subscriber) {
        getInstance().mBus.register(subscriber);
    }

    public static void unregister(Object subscriber) {
        try {
            getInstance().mBus.unregister(subscriber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
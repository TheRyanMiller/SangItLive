package com.rtmillerprojects.sangitlive;

import android.os.Looper;

import com.squareup.otto.Bus;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Ryan on 8/27/2016.
 */
public class EventBus {

    private static EventBus mInstance;
    private static Bus mBus = new Bus();

    public static Bus getBus() {
        return mBus;
    }


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

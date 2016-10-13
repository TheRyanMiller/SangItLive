package com.rtmillerprojects.sangitlive.model.EventCalls;

/**
 * Created by Ryan on 10/10/2016.
 */
public class ReturnInternetStatus {
    public boolean connected;
    public String message;

    public ReturnInternetStatus(boolean connected, String message) {
        this.connected = connected;
        this.message = message;
    }
}

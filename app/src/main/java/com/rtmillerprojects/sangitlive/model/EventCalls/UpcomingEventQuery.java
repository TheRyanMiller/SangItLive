package com.rtmillerprojects.sangitlive.model.EventCalls;

import java.util.ArrayList;

/**
 * Created by Ryan on 8/27/2016.
 */
public class UpcomingEventQuery {

    private ArrayList<NameMbidPair> nameMbidPairs;
    private boolean localEvents;

    public boolean isFailedMbidAttempt() {
        return failedMbidAttempt;
    }

    public void setFailedMbidAttempt(boolean failedMbidAttempt) {
        this.failedMbidAttempt = failedMbidAttempt;
    }

    private boolean failedMbidAttempt;

    //Constructor
    public UpcomingEventQuery(ArrayList<NameMbidPair> nameMbidPairs, boolean localEvents, boolean failedMbidAttempt) {
        this.nameMbidPairs = nameMbidPairs;
        this.localEvents = localEvents;
        this.failedMbidAttempt = failedMbidAttempt;
    }

    public ArrayList<NameMbidPair> getNameMbidPairs() {
        return nameMbidPairs;
    }

    public void setNameMbidPairs(ArrayList<NameMbidPair> nameMbidPairs) {
        this.nameMbidPairs = nameMbidPairs;
    }

    public boolean isLocalEvents() {
        return localEvents;
    }

    public void setLocalEvents(boolean page) {
        this.localEvents = localEvents;
    }

}

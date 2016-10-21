package com.rtmillerprojects.sangitlive.model.EventCalls;

import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;

import java.util.ArrayList;

/**
 * Created by Ryan on 10/1/2016.
 */
public class EventManagerRequest {
    public boolean isFilteredByLocation;
    private ArrayList<NameMbidPair> nameMbidPairs;

    public boolean isForcedRefresh() {
        return forcedRefresh;
    }

    public void setForcedRefresh(boolean forcedRefresh) {
        this.forcedRefresh = forcedRefresh;
    }

    private boolean forcedRefresh;

    public boolean isFailedMbidAttempt() {
        return failedMbidAttempt;
    }

    public void setFailedMbidAttempt(boolean failedMbidAttempt) {
        this.failedMbidAttempt = failedMbidAttempt;
    }

    private boolean failedMbidAttempt;

    //Constructor
    public EventManagerRequest(ArrayList<NameMbidPair> nameMbidPairs, boolean failedMbidAttempt, boolean forcedRefresh, boolean isFilteredByLocation) {
        this.nameMbidPairs = nameMbidPairs;
        this.failedMbidAttempt = failedMbidAttempt;
        this.forcedRefresh = forcedRefresh;
        this.isFilteredByLocation = isFilteredByLocation;
    }

    public ArrayList<NameMbidPair> getNameMbidPairs() {
        return nameMbidPairs;
    }

    public void setNameMbidPairs(ArrayList<NameMbidPair> nameMbidPairs) {
        this.nameMbidPairs = nameMbidPairs;
    }

}

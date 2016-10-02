package com.rtmillerprojects.sangitlive.model.EventCalls;

import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;

import java.util.ArrayList;

/**
 * Created by Ryan on 10/1/2016.
 */
public class EventManagerRequest {
    private ArrayList<NameMbidPair> nameMbidPairs;
    private int page;

    public boolean isFailedMbidAttempt() {
        return failedMbidAttempt;
    }

    public void setFailedMbidAttempt(boolean failedMbidAttempt) {
        this.failedMbidAttempt = failedMbidAttempt;
    }

    private boolean failedMbidAttempt;

    //Constructor
    public EventManagerRequest(ArrayList<NameMbidPair> nameMbidPairs, int page, boolean failedMbidAttempt) {
        this.nameMbidPairs = nameMbidPairs;
        this.page = page;
        this.failedMbidAttempt = failedMbidAttempt;

    }

    public ArrayList<NameMbidPair> getNameMbidPairs() {
        return nameMbidPairs;
    }

    public void setNameMbidPairs(ArrayList<NameMbidPair> nameMbidPairs) {
        this.nameMbidPairs = nameMbidPairs;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}

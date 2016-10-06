package com.rtmillerprojects.sangitlive.model.EventCalls;

import com.rtmillerprojects.sangitlive.model.NotifyNewEventsForArtist;

import java.util.ArrayList;

/**
 * Created by Ryan on 10/4/2016.
 */
public class CompletedForceRefresh {
    public boolean isComplete;
    public boolean isError;
    public String errorMessage;
    public ArrayList<NotifyNewEventsForArtist> newEventsList;

    public ArrayList<NotifyNewEventsForArtist> getNewEventsList() {
        return newEventsList;
    }

    public void setNewEventsList(ArrayList<NotifyNewEventsForArtist> newEventsList) {
        this.newEventsList = newEventsList;
    }



    public CompletedForceRefresh(boolean isComplete,boolean isError) {
        this.isComplete = isComplete;
        this.isError = isError;
    }

}

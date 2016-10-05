package com.rtmillerprojects.sangitlive.model.EventCalls;

import java.util.ArrayList;

/**
 * Created by Ryan on 10/4/2016.
 */
public class CompletedForceRefresh {
    public boolean isComplete;
    public boolean isError;
    public String errorMessage;
    public ArrayList<NewEventsForArtist> newEventsList;

    public CompletedForceRefresh(boolean isComplete,boolean isError, ArrayList<NewEventsForArtist> newEventsList) {
        this.isComplete = isComplete;
        this.isError = isError;
        this.newEventsList = newEventsList;
    }

    public static class NewEventsForArtist {
        private String artistName;
        private String mbid;
        private int numOfNewShows;
        private int numOfNewShowsInLocation;

        public NewEventsForArtist(String artistName, String mbid, int numOfNewShows, int numOfNewShowsInLocation) {
            this.artistName = artistName;
            this.mbid = mbid;
            this.numOfNewShows = numOfNewShows;
            this.numOfNewShowsInLocation = numOfNewShowsInLocation;
        }
    }
}

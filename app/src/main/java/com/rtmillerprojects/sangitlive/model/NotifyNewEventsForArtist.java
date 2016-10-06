package com.rtmillerprojects.sangitlive.model;

/**
 * Created by Ryan on 10/5/2016.
 */

public class NotifyNewEventsForArtist {
    private String artistName;
    private String mbid;
    private int numOfNewShows;
    private int numOfNewShowsInLocation;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public int getNumOfNewShows() {
        return numOfNewShows;
    }

    public void setNumOfNewShows(int numOfNewShows) {
        this.numOfNewShows = numOfNewShows;
    }

    public int getNumOfNewShowsInLocation() {
        return numOfNewShowsInLocation;
    }

    public void setNumOfNewShowsInLocation(int numOfNewShowsInLocation) {
        this.numOfNewShowsInLocation = numOfNewShowsInLocation;
    }

    public NotifyNewEventsForArtist(String artistName, String mbid) {
        this.artistName = artistName;
        this.mbid = mbid;
    }

    public NotifyNewEventsForArtist(String artistName, String mbid, int numOfNewShowsTotal) {
        this.artistName = artistName;
        this.mbid = mbid;
        this.numOfNewShows = numOfNewShowsTotal;
    }

    //Empty constructor
    public NotifyNewEventsForArtist() {
    }
}
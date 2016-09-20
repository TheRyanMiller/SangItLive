package com.rtmillerprojects.sangitlive.model.EventCalls;

/**
 * Created by Ryan on 9/20/2016.
 */
public class LastFmArtistDetails {
    private String mbid;

    public LastFmArtistDetails(String mbid) {
        this.mbid = mbid;
    }

    public String getMbid() {

        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }


}

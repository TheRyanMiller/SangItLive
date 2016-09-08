package com.rtmillerprojects.sangitlive.model;

/**
 * Created by Ryan on 8/27/2016.
 */
public class PostArtistSearch {

    private String mbid;
    private String artistSearchString;

    //Constructor
    public PostArtistSearch(String mbid, String searchString) {
        this.mbid = mbid;
        this.artistSearchString = searchString;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getArtistSearchString() {
        return artistSearchString;
    }

    public void setArtistSearchString(String artistSearchString) {
        this.artistSearchString = artistSearchString;
    }

}

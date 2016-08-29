package com.rtmillerprojects.sangitlive;

/**
 * Created by Ryan on 8/27/2016.
 */
public class DoRestEvent {

    private String artistMbid;
    private String artistSearchString;
    private int page;

    //Constructor
    public DoRestEvent(String artistMbid, String artistSearchString, int page) {
        this.artistMbid = artistMbid;
        this.artistSearchString = artistSearchString;
        this.page = page;
    }

    public String getArtistMbid() {
        return artistMbid;
    }

    public void setArtistMbid(String artistMbid) {
        this.artistMbid = artistMbid;
    }

    public String getArtistSearchString() {
        return artistSearchString;
    }

    public void setArtistSearchString(String artistSearchString) {
        this.artistSearchString = artistSearchString;
    }
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}

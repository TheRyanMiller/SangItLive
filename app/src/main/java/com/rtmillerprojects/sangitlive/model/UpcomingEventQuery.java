package com.rtmillerprojects.sangitlive.model;

import java.util.ArrayList;

/**
 * Created by Ryan on 8/27/2016.
 */
public class UpcomingEventQuery {

    private ArrayList<String> artistMbids;
    private int page;

    //Constructor
    public UpcomingEventQuery(ArrayList<String> artistMbids, int page) {
        this.artistMbids = artistMbids;
        this.page = page;
    }

    public ArrayList<String> getArtistMbids() {
        return artistMbids;
    }

    public void setArtistMbid(ArrayList<String> artistMbid) {
        this.artistMbids = artistMbid;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}

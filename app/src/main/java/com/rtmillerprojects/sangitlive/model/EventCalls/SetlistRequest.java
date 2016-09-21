package com.rtmillerprojects.sangitlive.model.EventCalls;

/**
 * Created by Ryan on 9/20/2016.
 */
public class SetlistRequest {
    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public SetlistRequest(String mbid, int page) {
        this.mbid = mbid;
        this.page = page;
    }

    private String mbid;
    private int page;
}

package com.rtmillerprojects.sangitlive.model.EventCalls;

/**
 * Created by Ryan on 9/20/2016.
 */
public class SetlistRequest {

    private int page;
    public NameMbidPair nmp;


    public SetlistRequest(NameMbidPair nmp, int page) {
        this.nmp = nmp;
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public NameMbidPair getNmp() {
        return nmp;
    }

    public void setNmp(NameMbidPair nmp) {
        this.nmp = nmp;
    }


}

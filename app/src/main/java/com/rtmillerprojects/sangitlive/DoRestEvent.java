package com.rtmillerprojects.sangitlive;

/**
 * Created by Ryan on 8/27/2016.
 */
public class DoRestEvent {
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DoRestEvent(String url) {
        this.url = url;
    }

    String url;
}

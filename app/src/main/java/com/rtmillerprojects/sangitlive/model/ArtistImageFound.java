package com.rtmillerprojects.sangitlive.model;

/**
 * Created by Ryan on 9/7/2016.
 */
public class ArtistImageFound {
    private String url;
    private String mbid;

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArtistImageFound(String mbid, String url) {
        this.mbid = mbid;
        this.url = url;
    }
}

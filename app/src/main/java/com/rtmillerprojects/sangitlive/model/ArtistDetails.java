package com.rtmillerprojects.sangitlive.model;

import org.parceler.Parcel;

/**
 * Created by Ryan on 8/25/2016.
 */
@Parcel
public class ArtistDetails {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String mbid;
    private String sortName;
    private String url;
}

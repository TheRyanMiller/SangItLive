package com.rtmillerprojects.sangitlive.model;

import org.parceler.Parcel;

/**
 * Created by Ryan on 8/25/2016.
 */
@Parcel
public class ArtistDetails {
    String name;
    String mbid;
    String sortName;
    String url;
    String thumbUrl;

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

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }



}

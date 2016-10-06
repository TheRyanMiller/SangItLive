package com.rtmillerprojects.sangitlive.model;

import com.rtmillerprojects.sangitlive.model.lastfmartistsearch.Image;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Ryan on 8/25/2016.
 */
@Parcel
public class ArtistDetails{
    String name;
    String mbid;
    String sortName;
    String url;
    String thumbUrl;
    boolean isOnTour;
    String bio;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }



    public String getImageText() {
        return imageText;
    }

    public void setImageText(String imageText) {
        this.imageText = imageText;
    }

    public String imageText;

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public String imageSize;


    public boolean isOnTour() {
        return isOnTour;
    }

    public void setOnTour(boolean onTour) {
        isOnTour = onTour;
    }



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

package com.rtmillerprojects.sangitlive.model.EventCalls;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Ryan on 9/24/2016.
 */
public class NameMbidPair {
    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    private String artistName;
    private String mbid;

    public NameMbidPair(String artistName, String mbid) {
        this.artistName = artistName;
        this.mbid = mbid;
    }
}

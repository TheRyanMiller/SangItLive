
package com.rtmillerprojects.sangitlive.model.lastfmartistsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArtistLastFm {

    @SerializedName("artist")
    @Expose
    private Artist artist;

    /**
     * 
     * @return
     *     The artist
     */
    public Artist getArtist() {
        return artist;
    }

    /**
     * 
     * @param artist
     *     The artist
     */
    public void setArtist(Artist artist) {
        this.artist = artist;
    }

}

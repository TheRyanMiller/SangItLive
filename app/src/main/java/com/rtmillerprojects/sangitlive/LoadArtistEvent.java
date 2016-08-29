package com.rtmillerprojects.sangitlive;

/**
 * Created by Ryan on 8/28/2016.
 */
public class LoadArtistEvent {
    private ArtistResults artistResults;

    //Constructor
    public LoadArtistEvent(ArtistResults artistDetails){
        this.artistResults = artistDetails;
    }

    public void setArtistResults(ArtistResults artistResults) {
        this.artistResults = artistResults;
    }

    public ArtistResults getArtistResults() {
        return artistResults;
    }
}

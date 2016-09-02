package com.rtmillerprojects.sangitlive.model;

/**
 * Created by Ryan on 8/27/2016.
 */
public class LoadSetlistsEvent {
    private SetlistsByArtists setlists;

    //Constructor
    public LoadSetlistsEvent(SetlistsByArtists setlists) {
        this.setlists = setlists;
    }

    public SetlistsByArtists getSetlists() {
        return setlists;
    }
    public void setSetlists(SetlistsByArtists setlists) {
        this.setlists = setlists;
    }

}

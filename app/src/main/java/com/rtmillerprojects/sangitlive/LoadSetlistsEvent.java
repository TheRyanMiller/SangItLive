package com.rtmillerprojects.sangitlive;

/**
 * Created by Ryan on 8/27/2016.
 */
public class LoadSetlistsEvent {
    private SetlistsByArtists setlists;

    public LoadSetlistsEvent(SetlistsByArtists setlists) {
        this.setlists = setlists;
    }
}

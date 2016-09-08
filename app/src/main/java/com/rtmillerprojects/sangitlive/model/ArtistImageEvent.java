package com.rtmillerprojects.sangitlive.model;

import java.util.ArrayList;

/**
 * Created by Ryan on 9/7/2016.
 */
public class ArtistImageEvent {
    public ArrayList<String> mbids;
    public ArrayList<String> getMbids() {
        return mbids;
    }

    public void setMbids(ArrayList<String> mbids) {
        this.mbids = mbids;
    }

    public ArtistImageEvent(ArrayList<String> mbids) {
        this.mbids = mbids;
    }
}

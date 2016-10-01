package com.rtmillerprojects.sangitlive.model.EventCalls;

import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.model.musicbrainzaritstbrowse.Artist;

import java.util.ArrayList;

/**
 * Created by Ryan on 10/1/2016.
 */
public class MBBrowseList {
    public ArrayList<Artist> results;

    public MBBrowseList(ArrayList<Artist> results) {
        this.results = results;
    }
}

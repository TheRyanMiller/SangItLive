package com.rtmillerprojects.sangitlive.model.EventCalls;

import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.model.musicbrainzaritstbrowse.Artist;

import java.util.ArrayList;

/**
 * Created by Ryan on 10/1/2016.
 */
public class BITResultPackage {
    public ArrayList<BandsInTownEventResult> events;

    public BITResultPackage(ArrayList<BandsInTownEventResult> events) {
        this.events = events;
    }
}
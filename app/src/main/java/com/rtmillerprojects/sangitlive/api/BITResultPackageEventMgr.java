package com.rtmillerprojects.sangitlive.api;

import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.model.EventCalls.NameMbidPair;

import java.util.ArrayList;

/**
 * Created by Ryan on 10/1/2016.
 */
public class BITResultPackageEventMgr {
    public ArrayList<BandsInTownEventResult> events;
    public NameMbidPair pair;

    public BITResultPackageEventMgr(ArrayList<BandsInTownEventResult> events, NameMbidPair pair) {
        this.events = events;
        this.pair = pair;
    }
}

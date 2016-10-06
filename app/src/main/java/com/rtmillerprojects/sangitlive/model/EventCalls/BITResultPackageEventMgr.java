package com.rtmillerprojects.sangitlive.model.EventCalls;

import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.model.EventCalls.NameMbidPair;

import java.util.ArrayList;

/**
 * Created by Ryan on 10/1/2016.
 */
public class BITResultPackageEventMgr {
    public ArrayList<BandsInTownEventResult> events;
    public NameMbidPair pair;
    public boolean isForceRefresh;

    public BITResultPackageEventMgr(ArrayList<BandsInTownEventResult> events, NameMbidPair pair, boolean isForceRefresh) {
        this.events = events;
        this.pair = pair;
        this.isForceRefresh = isForceRefresh;
    }
}

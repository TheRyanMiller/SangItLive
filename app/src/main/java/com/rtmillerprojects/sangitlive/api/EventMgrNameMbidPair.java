package com.rtmillerprojects.sangitlive.api;

import com.rtmillerprojects.sangitlive.model.EventCalls.NameMbidPair;

/**
 * Created by Ryan on 10/1/2016.
 */
public class EventMgrNameMbidPair {
    public NameMbidPair pair;
    public boolean isFilteredByLocation;

    public EventMgrNameMbidPair(NameMbidPair pair, boolean isFilteredByLocation) {
        this.pair = pair;
        this.isFilteredByLocation = isFilteredByLocation;
    }
}

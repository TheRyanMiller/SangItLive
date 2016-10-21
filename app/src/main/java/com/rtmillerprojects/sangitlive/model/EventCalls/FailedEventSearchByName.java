package com.rtmillerprojects.sangitlive.model.EventCalls;

import android.provider.Settings;


/**
 * Created by Ryan on 10/20/2016.
 */

public class FailedEventSearchByName {
    public NameMbidPair pair;
    public boolean isLocationFiltered;

    public FailedEventSearchByName(NameMbidPair pair, boolean isLocationFiltered) {
        this.pair = pair;
        this.isLocationFiltered = isLocationFiltered;
    }

    public NameMbidPair getPair() {
        return pair;
    }

    public void setPair(NameMbidPair pair) {
        this.pair = pair;
    }

    public boolean isLocationFiltered() {
        return isLocationFiltered;
    }

    public void setLocationFiltered(boolean locationFiltered) {
        isLocationFiltered = locationFiltered;
    }


}

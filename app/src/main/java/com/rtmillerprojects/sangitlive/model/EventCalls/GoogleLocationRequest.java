package com.rtmillerprojects.sangitlive.model.EventCalls;

/**
 * Created by Ryan on 9/28/2016.
 */
public class GoogleLocationRequest {
    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public GoogleLocationRequest(String zip) {
        this.zip = zip;
    }

    private String zip;
}

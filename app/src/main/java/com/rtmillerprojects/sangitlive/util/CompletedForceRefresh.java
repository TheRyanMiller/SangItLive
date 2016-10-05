package com.rtmillerprojects.sangitlive.util;

/**
 * Created by Ryan on 10/4/2016.
 */
public class CompletedForceRefresh {
    public boolean isComplete;
    public boolean isError;
    public String errorMessage;

    public CompletedForceRefresh(boolean isComplete) {
        this.isComplete = isComplete;
    }
}

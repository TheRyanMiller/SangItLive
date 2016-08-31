package com.rtmillerprojects.sangitlive;

import retrofit2.Retrofit;

/**
 * Created by Ryan on 8/31/2016.
 */
public class ApiErrorEvent {

    int callNumber;
    Exception error;

    public ApiErrorEvent(Exception error, int callNumber) {
        this.callNumber = callNumber;
        this.error = error;
    }

    public Exception getError() {
        return error;
    }
    /*
    public int getHttpStatusCode() {

        if (!this.error.isNetworkError() && this.error.getResponse() != null) {
            return this.error.getResponse().getStatus();
        } else {
            return -1;
        }
    }

    public boolean isNetworkError() {
        return this.error.isNetworkError();
    }
    */
}

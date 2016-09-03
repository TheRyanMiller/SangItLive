
package com.rtmillerprojects.sangitlive.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LifeSpan {

    @SerializedName("begin")
    @Expose
    private String begin;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("ended")
    @Expose
    private Boolean ended;

    /**
     * 
     * @return
     *     The begin
     */
    public String getBegin() {
        return begin;
    }

    /**
     * 
     * @param begin
     *     The begin
     */
    public void setBegin(String begin) {
        this.begin = begin;
    }

    /**
     * 
     * @return
     *     The end
     */
    public String getEnd() {
        return end;
    }

    /**
     * 
     * @param end
     *     The end
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * 
     * @return
     *     The ended
     */
    public Boolean getEnded() {
        return ended;
    }

    /**
     * 
     * @param ended
     *     The ended
     */
    public void setEnded(Boolean ended) {
        this.ended = ended;
    }

}

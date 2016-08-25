package com.rtmillerprojects.sangitlive;

/**
 * Created by Ryan on 8/25/2016.
 */
public class SongPlayedInfo {
    private String songName;
    private String venue;
    private String city;
    private String state;
    private String date;
    private String tourName;

    public String getTourName() {
        return tourName;
    }

    public void setTourName (String tourName) {
        this.tourName = tourName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

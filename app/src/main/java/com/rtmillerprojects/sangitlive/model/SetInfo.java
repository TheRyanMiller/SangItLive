package com.rtmillerprojects.sangitlive.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Ryan on 8/25/2016.
 */
public class SetInfo implements Parcelable {
    private String songName;
    private String venue;
    private String city;
    private String state;
    private String date;
    private String tourName;
    private boolean wasPlayed;
    private ArrayList<String> songs;

    public void setWasPlayed(boolean wasPlayed) {
        this.wasPlayed = wasPlayed;
    }

    public void setSongs(ArrayList<String> songs) {
        this.songs = songs;
    }

    public boolean getWasPlayed() {

        return wasPlayed;
    }

    public ArrayList<String> getSongs() {
        return songs;
    }

    public SetInfo(){

    }
    protected SetInfo(Parcel in) {

        //songs = new ArrayList<>();
        ArrayList<String> songs = new ArrayList<>();
        in.readStringList(songs);
        this.songs = songs;
        venue = in.readString();
        city = in.readString();
        state = in.readString();
        date = in.readString();
        tourName = in.readString();
    }

    public static final Creator<SetInfo> CREATOR = new Creator<SetInfo>() {
        @Override
        public SetInfo createFromParcel(Parcel in) {
            return new SetInfo(in);
        }

        @Override
        public SetInfo[] newArray(int size) {
            return new SetInfo[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(songs);
        dest.writeString(venue);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(date);
        dest.writeString(tourName);
    }
}

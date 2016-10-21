package com.rtmillerprojects.sangitlive.api;

import com.rtmillerprojects.sangitlive.model.ArtistResults;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.model.SetlistsByArtists;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ryan on 8/28/2016.
 */
public interface ApiServiceBandsInTown {
    final static String BASEURL = "http://api.bandsintown.com/";
    final static String APPSETTINGS = "";
    final static String EVENTENDPOINT = "/artists/mbid_{mbid}/events";
    final static String ARTISTNAMEENDPOINT = "/artists/{artistName}/events";
    final static String SEARCHBYMBIDLOCATIONENDPOINT = "/artists/mbid_{mbid}/events/search";
    final static String SEARCHBYLOCATIONENDPOINT = "/artists/{artistName}/events/search";


    // Events By MBID
    @GET(EVENTENDPOINT)
    Call<ArrayList<BandsInTownEventResult>> searchEventsByMbid (@Path("mbid") String mbid, @Query("format") String format, @Query("api_version") String apiVersion, @Query("app_id") String appId);

    // Events By Artist Name
    @GET(ARTISTNAMEENDPOINT)
    Call<ArrayList<BandsInTownEventResult>> searchEventsByArtistName (@Path("artistName") String artistName, @Query("format") String format, @Query("api_version") String apiVersion, @Query("app_id") String appId);

    // Events By Location (artist name)
    @GET(SEARCHBYLOCATIONENDPOINT)
    Call<ArrayList<BandsInTownEventResult>> searchLocationalEventsByArtistName (@Path("artistName") String artistName, @Query("format") String format, @Query("location") String location, @Query("api_version") String apiVersion, @Query("app_id") String appId);

    // Events By Location (mbid)
    @GET(SEARCHBYMBIDLOCATIONENDPOINT)
    Call<ArrayList<BandsInTownEventResult>> searchLocationalEventsByMbid (@Path("mbid") String mbid, @Query("format") String format, @Query("location") String location, @Query("api_version") String apiVersion, @Query("app_id") String appId);



    /*
    EXAMPLE:
        All upcoming Metallica shows using music brainz id (artist name unknown):
        http://api.bandsintown.com/artists/mbid_65f4f0c5-ef9e-490c-aee3-909e7ae6b2ab/events?format=json&api_version=2.0&app_id=YOUR_APP_ID
     */
}

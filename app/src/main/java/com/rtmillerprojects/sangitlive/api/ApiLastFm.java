package com.rtmillerprojects.sangitlive.api;

import com.rtmillerprojects.sangitlive.model.lastfmartistsearch.ArtistLastFm;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ryan on 9/7/2016.
 */
public interface ApiLastFm {
    final static String BASEURL = "http://ws.audioscrobbler.com/";
    final static String APPSETTINGS = "/ws/2";
    final static String APIENDPOINT = "/2.0/";


    @GET(APIENDPOINT)
    Call<ArtistLastFm> searchArtist (@Query("method") String artistGetInfo, @Query("mbid") String mbidOrName, @Query("api_key") String apiKey, @Query("format") String format);


}

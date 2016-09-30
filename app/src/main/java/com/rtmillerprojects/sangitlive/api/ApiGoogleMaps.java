package com.rtmillerprojects.sangitlive.api;

import com.rtmillerprojects.sangitlive.model.GoogleLocation.LocationResults;
import com.rtmillerprojects.sangitlive.model.lastfmartistsearch.ArtistLastFm;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ryan on 9/7/2016.
 */
public interface ApiGoogleMaps {
    final static String BASEURL = "https://maps.googleapis.com/";
    final static String APIENDPOINT = "/maps/api/geocode/json";


    @GET(APIENDPOINT)
    Call<LocationResults> searchZip(@Query("address") String zip);


}

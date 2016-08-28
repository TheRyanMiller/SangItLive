package com.rtmillerprojects.sangitlive;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
/**
 * Created by Ryan on 8/28/2016.
 */
public interface ApiService {
    final static String BASEURL = "http://api.setlist.fm";
    final static String APPSETTINGS = "/rest/0.1/application_settings.json";
    final static String SETLISTSENDPOINT = "/rest/0.1/search/setlists.json";
    final static String ARTISTENDPOINT = "/rest/0.1/search/artists.json";

    // Setlists
    @GET(SETLISTSENDPOINT)
    Call<SetlistsByArtists> getSetlists(@Query("artistMbid") String artistKey, @Query("p") int page);
    //http://api.setlist.fm/rest/0.1/search/setlists.json?artistMbid=859d0860-d480-4efd-970c-c05d5f1776b8&p=1
}

package com.rtmillerprojects.sangitlive.api;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.model.ArtistImageEvent;
import com.rtmillerprojects.sangitlive.model.ArtistImageFound;
import com.rtmillerprojects.sangitlive.model.EventCalls.LastFmArtistDetails;
import com.rtmillerprojects.sangitlive.model.lastfmartistsearch.ArtistLastFm;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ryan on 9/3/2016.
 */
public class LastFmArtistService {
    private ArtistLastFm artistResults;
    private Context context;
    private String mbid;
    private boolean wasFound;
    private String imgUrl;
    private String placeholderUrl = "http://cliparts.co/cliparts/pc5/odK/pc5odKdxi.png";

    public LastFmArtistService(Application context){
        this.context = context;
        /* Some Examplesettings to use in future
            Look in NowPlayingApplication.java:
            --https://github.com/androidfu/Now-Playing/blob/master/app/src/main/java/com/androidfu/nowplaying/app/NowPlayingApplication.java
        mApplicationSettingsLocalStorageHandler = new AppSettingsLocalStorageHandler(context);
        isRetrofitLoggingEnabled = Boolean.valueOf(context.getString(R.string.retrofit_logging_enabled));
        appSettingsUrl = context.getString(R.string.application_settings_url);
        rottenTomatoesUrl = context.getString(R.string.movies_url);
        rottenTomatoesApiKey = context.getString(R.string.rotten_tomatoes_api_key); /// Look in res/values/secret.xml
        if (rottenTomatoesApiKey.equals("REPLACE WITH YOUR KEY")) {
            Toast.makeText(context, context.getString(R.string.error_replace_api_key), Toast.LENGTH_LONG).show();
        }
        */
    }

    @Subscribe
    public void queryForArtistInfo(LastFmArtistDetails ad){
        mbid = ad.getMbid();
        Gson gson = new GsonBuilder()
                //.registerTypeAdapterFactory(new ())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLastFm.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiLastFm artistApi = retrofit.create(ApiLastFm.class);

        Call<ArtistLastFm> call = artistApi.searchArtist("artist.getinfo",mbid,"da010c249a845cfee6f44aa935a7b2a0","json");
        call.enqueue(new Callback<ArtistLastFm>() {
            @Override
            public void onResponse(Call<ArtistLastFm> call, Response<ArtistLastFm> response) {
                artistResults = response.body();
                EventBus.post(artistResults);
            }
            @Override
            public void onFailure(Call<ArtistLastFm> call, Throwable t) {
                //EventBus.post(new APIErrorEvent(RetrofitError.unexpectedError(response.getUrl(), new HttpException("Empty Body")), event.getCallNumber()));
            }

        });
    }

    @Subscribe
    public void receiveQueryForArtistImage(ArtistImageEvent msg){
        ArrayList<String> mbids = msg.getMbids();
        Gson gson = new GsonBuilder()
                //.registerTypeAdapterFactory(new ())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiLastFm.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // prepare call in Retrofit 2.0
        //String radioheadKey = "a74b1b7f-71a5-4011-9441-d0b5e4122711";
        ApiLastFm artistApi = retrofit.create(ApiLastFm.class);
        for (int i = 0; i < mbids.size(); i++) {
            mbid = msg.getMbids().get(i);
            Call<ArtistLastFm> call = artistApi.searchArtist("artist.getinfo",mbid,"da010c249a845cfee6f44aa935a7b2a0","json");
            //asynchronous call
            call.enqueue(new Callback<ArtistLastFm>() {
                @Override
                public void onResponse(Call<ArtistLastFm> call, Response<ArtistLastFm> response) {
                    artistResults = response.body();
                    Log.d("RYAN TEST","ARTIST SEARCH RESPONSE SUCCESS");
                    EventBus.post(artistResults);
                }

                @Override
                public void onFailure(Call<ArtistLastFm> call, Throwable t) {
                    //EventBus.post(new APIErrorEvent(RetrofitError.unexpectedError(response.getUrl(), new HttpException("Empty Body")), event.getCallNumber()));
                }
            });
        }

    }

}

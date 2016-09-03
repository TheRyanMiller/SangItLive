package com.rtmillerprojects.sangitlive.api;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.model.UpcomingEventQuery;
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
public class ServiceUpcomingEvents {
    private ArrayList<BandsInTownEventResult> bandsInTownEvents;
    private Context context;
    private String mbid;

    public ServiceUpcomingEvents(Application context){
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
    public void receiveQueryForUpcomingEvents(UpcomingEventQuery event){
        Gson gson = new GsonBuilder()
                //.registerTypeAdapterFactory(new SetlistTypeAdapterFactory())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiServiceBandsInTown.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // prepare call in Retrofit 2.0
        //String radioheadKey = "a74b1b7f-71a5-4011-9441-d0b5e4122711";
        ApiServiceBandsInTown eventsApi = retrofit.create(ApiServiceBandsInTown.class);
        for (int i = 0; i < event.getArtistMbids().size(); i++) {
            mbid = event.getArtistMbids().get(i);
            Call<ArrayList<BandsInTownEventResult>> call = eventsApi.searchEvents(mbid,"json","2.0","ConcertCompanion");
            //asynchronous call
            call.enqueue(new Callback<ArrayList<BandsInTownEventResult>>() {
                @Override
                public void onResponse(Call<ArrayList<BandsInTownEventResult>> call, Response<ArrayList<BandsInTownEventResult>> response) {
                    bandsInTownEvents = response.body();
                    Log.d("RYAN TEST","EVENT SEARCH RESPONSE SUCCESS");
                    EventBus.post(bandsInTownEvents);
                }

                @Override
                public void onFailure(Call<ArrayList<BandsInTownEventResult>> call, Throwable t) {
                    //EventBus.post(new APIErrorEvent(RetrofitError.unexpectedError(response.getUrl(), new HttpException("Empty Body")), event.getCallNumber()));
                }
            });
        }

    }

}

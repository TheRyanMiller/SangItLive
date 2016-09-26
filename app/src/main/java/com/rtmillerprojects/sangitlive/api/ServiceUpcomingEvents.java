package com.rtmillerprojects.sangitlive.api;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.model.EventCalls.NameMbidPair;
import com.rtmillerprojects.sangitlive.model.EventCalls.UpcomingEventQuery;
import com.rtmillerprojects.sangitlive.util.DatabaseHelper;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private String artistName;
    private String mbid;
    private List<Long> favoritedShows;
    private NameMbidPair pair;
    private String failedArtistName;
    private String failedArtistMbid;
    private int responseCounter = 0;
    boolean failedFlag = false;
    ArrayList<String> knownFailures = new ArrayList<>();

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
    public void receiveQueryForUpcomingEvents(final UpcomingEventQuery event){
        responseCounter = 0;
        knownFailures = new ArrayList<>();
        Gson gson = new GsonBuilder()
                //.registerTypeAdapterFactory(new SetlistTypeAdapterFactory())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiServiceBandsInTown.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // prepare call in Retrofit 2.0
        //String radioheadKey = "a74b1b7f-71a5-4011-9441-d0b5e4122711";
        ApiServiceBandsInTown eventsApi = retrofit.create(ApiServiceBandsInTown.class);
        for (int i = 0; i < event.getNameMbidPairs().size(); i++) {
            mbid = event.getNameMbidPairs().get(i).getMbid();
            artistName = event.getNameMbidPairs().get(i).getArtistName();
            if(!event.isFailedMbidAttempt()) {
                Call<ArrayList<BandsInTownEventResult>> call = eventsApi.searchEventsByMbid(mbid, "json", "2.0", "ConcertCompanion");
                //asynchronous call
                call.enqueue(new Callback<ArrayList<BandsInTownEventResult>>() {
                    @Override
                    public void onResponse(Call<ArrayList<BandsInTownEventResult>> call, Response<ArrayList<BandsInTownEventResult>> response) {
                        String aName = event.getNameMbidPairs().get(responseCounter).getArtistName();
                        if (response.body() == null && response.errorBody() != null){
                            //Check against known failures
                            for(String a : knownFailures){
                                if(aName.equals(a)){
                                    failedFlag = true;
                                    break;
                                }
                                else failedFlag = false;
                            }
                            if (!failedFlag) {
                                knownFailures.add(aName);
                                failedFlag = false;
                                pair = new NameMbidPair(event.getNameMbidPairs().get(responseCounter).getArtistName(),event.getNameMbidPairs().get(responseCounter).getMbid());
                                Log.d("RYAN TEST", event.getNameMbidPairs().get(responseCounter).getArtistName() + " EVENT SEARCH BY MBID FAILED");
                                try {
                                    String errorMsg = response.errorBody().string();
                                    //Make New attempt using artist name
                                    if (errorMsg.contains("Unknown Artist")) {
                                        ArrayList<NameMbidPair> pairs = new ArrayList<NameMbidPair>();
                                        pairs.add(pair);
                                        EventBus.post(pair);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //DO ERROR HANDLING HERE
                            }
                        }
                        else if(response.body() != null){
                            bandsInTownEvents = response.body();
                            Log.d("RYAN TEST", artistName+" EVENT SEARCH BY MBID SUCCESS");
                            ArrayList<BandsInTownEventResult> scrubbedEventList = new ArrayList<>();
                            DatabaseHelper db = DatabaseHelper.getInstance(context);
                            favoritedShows = db.getFavoritedEventIds();
                            for (BandsInTownEventResult e : bandsInTownEvents) {
                                e.setAttending(false);
                                for (int i = 0; i < favoritedShows.size(); i++) {
                                    if (e.getId() == favoritedShows.get(i).intValue()) {
                                        e.setAttending(true);
                                        break;
                                    }
                                }
                                boolean a = e.isAttending();
                                scrubbedEventList.add(e);
                            }
                            EventBus.post(scrubbedEventList);
                        }
                        responseCounter++;
                    }

                    @Override
                    public void onFailure(Call<ArrayList<BandsInTownEventResult>> call, Throwable t) {
                        Log.d("RYAN TEST", artistName+" EVENT SEARCH BY MBID FAILED");
                        responseCounter++;
                        //Log.e(t.getMessage());
                    }
                });
            }

        }

    }

    @Subscribe
    public void newAttempt(NameMbidPair nmp){
        Gson gson = new GsonBuilder()
                //.registerTypeAdapterFactory(new SetlistTypeAdapterFactory())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiServiceBandsInTown.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ApiServiceBandsInTown eventsApi = retrofit.create(ApiServiceBandsInTown.class);

        artistName = nmp.getArtistName();
        Call<ArrayList<BandsInTownEventResult>> call = eventsApi.searchEventsByArtistName(nmp.getArtistName(), "json", "2.0", "ConcertCompanion");
        //asynchronous call
        call.enqueue(new Callback<ArrayList<BandsInTownEventResult>>() {
            @Override
            public void onResponse(Call<ArrayList<BandsInTownEventResult>> call, Response<ArrayList<BandsInTownEventResult>> response) {
                bandsInTownEvents = response.body();

                if (response.body() == null && response.errorBody() != null) {
                    Log.d("RYAN TEST", artistName+" EVENT SEARCH BY ARTISTNAME FAILED");
                    try {
                        String errorMsg = response.errorBody().string();
                        if (errorMsg.contains("Unknown Artist")) {
                            Log.d("RYAN TEST", artistName+" Results show that this is an unknown Artist AGAIN");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //DO ERROR HANDLING HERE
                }
                else{
                    Log.d("RYAN TEST", artistName+" EVENT SEARCH BY ARTISTNAME SUCCESS");
                }
                if (bandsInTownEvents != null) {
                    ArrayList<BandsInTownEventResult> scrubbedEventList = new ArrayList<>();
                    DatabaseHelper db = DatabaseHelper.getInstance(context);
                    favoritedShows = db.getFavoritedEventIds();
                    for (BandsInTownEventResult e : bandsInTownEvents) {
                        e.setAttending(false);
                        for (int i = 0; i < favoritedShows.size(); i++) {
                            if (e.getId() == favoritedShows.get(i).intValue()) {
                                e.setAttending(true);
                                break;
                            }
                        }
                        boolean a = e.isAttending();
                        scrubbedEventList.add(e);
                    }
                    EventBus.post(scrubbedEventList);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BandsInTownEventResult>> call, Throwable t) {
                Log.d("RYAN TEST", artistName+" EVENT SEARCH RESPONSE FAILED");
                //Log.e(t.getMessage());
            }
        });
    }

}

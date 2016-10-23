package com.rtmillerprojects.sangitlive.api;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.model.EventCalls.BITResultPackage;
import com.rtmillerprojects.sangitlive.model.EventCalls.BITResultPackageEventMgr;
import com.rtmillerprojects.sangitlive.model.EventCalls.EventManagerRequest;
import com.rtmillerprojects.sangitlive.model.EventCalls.FailedEventSearchByName;
import com.rtmillerprojects.sangitlive.model.EventCalls.NameMbidPair;
import com.rtmillerprojects.sangitlive.model.EventCalls.UpcomingEventQuery;
import com.rtmillerprojects.sangitlive.util.DatabaseHelper;
import com.rtmillerprojects.sangitlive.util.SharedPreferencesHelper;
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
    private EventMgrNameMbidPair eventMgrPair;
    private ArrayList<NameMbidPair> pairs;
    private String failedArtistName;
    private String failedArtistMbid;
    private int responseCounterAll = 0;
    private int responseCounterLocal = 0;
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
        responseCounterAll = 0;
        responseCounterLocal = 0;
        pairs = event.getNameMbidPairs();
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
                //Build the call based on
                if (event.isLocalEvents()){
                    String city = SharedPreferencesHelper.getStringPreference(context.getString(R.string.user_city),"");
                    String stateAbbr = SharedPreferencesHelper.getStringPreference(context.getString(R.string.user_location_state_abr),"");
                    call = eventsApi.searchLocationalEventsByMbid(mbid, "json", city+" , "+stateAbbr, "2.0", "ConcertCompanion");
                }
                //asynchronous call
                call.enqueue(new Callback<ArrayList<BandsInTownEventResult>>() {
                    @Override
                    public void onResponse(Call<ArrayList<BandsInTownEventResult>> call, Response<ArrayList<BandsInTownEventResult>> response) {
                        String aName;
                        if(event.isLocalEvents()){
                            aName = event.getNameMbidPairs().get(responseCounterLocal).getArtistName();
                        }
                        else{
                            aName = event.getNameMbidPairs().get(responseCounterAll).getArtistName();
                        }

                        String reqUrl = response.raw().request().url().toString();
                        String retMbid = reqUrl.substring(reqUrl.indexOf("mbid_")+5, reqUrl.indexOf("/events"));
                        NameMbidPair responsePair = new NameMbidPair(aName, retMbid);
                        if (response.body() == null && response.errorBody() != null){
                            //Check against known failures
                            try {
                                String errorMsg = response.errorBody().string();
                                if (errorMsg.contains("Unknown Artist")) {
                                    for(NameMbidPair p : pairs){
                                        if(p.getMbid().contains(retMbid)){
                                            pair = p;
                                            Log.d("RYAN TEST",  p.getArtistName()+ " EVENT SEARCH BY MBID FAILED");
                                            FailedEventSearchByName fesbn = new FailedEventSearchByName(pair, event.isLocalEvents());
                                            EventBus.post(fesbn);
                                            break;
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
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
                            if(event.isLocalEvents()){responseCounterLocal++;}
                            else{responseCounterAll++;}

                            EventBus.post(new BITResultPackage(scrubbedEventList,event.isLocalEvents()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<BandsInTownEventResult>> call, Throwable t) {
                        Log.d("RYAN TEST", artistName+" EVENT SEARCH BY MBID FAILED");
                        if(event.isLocalEvents()){responseCounterLocal++;}
                        else{responseCounterAll++;}
                        //Log.e(t.getMessage());
                    }
                });
            }

        }

    }

    @Subscribe
    public void newAttempt(final FailedEventSearchByName fesbn){
        Gson gson = new GsonBuilder()
                //.registerTypeAdapterFactory(new SetlistTypeAdapterFactory())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiServiceBandsInTown.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ApiServiceBandsInTown eventsApi = retrofit.create(ApiServiceBandsInTown.class);

        artistName = fesbn.getPair().getArtistName();
        final String artistMbid = fesbn.getPair().getMbid();
        Call<ArrayList<BandsInTownEventResult>> call = eventsApi.searchEventsByArtistName(fesbn.getPair().getArtistName(), "json", "2.0", "ConcertCompanion");
        //Build the call based on
        if (fesbn.isLocationFiltered()){
            String city = SharedPreferencesHelper.getStringPreference(context.getString(R.string.user_city),"");
            String stateAbbr = SharedPreferencesHelper.getStringPreference(context.getString(R.string.user_location_state_abr),"");
            call = eventsApi.searchLocationalEventsByArtistName(mbid, "json", city+" , "+stateAbbr, "2.0", "ConcertCompanion");
        }
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
                        if(e.getArtists().get(0).getMbid()==null){
                            e.getArtists().get(0).setMbid(artistMbid);
                        };
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
                    if(fesbn.isLocationFiltered()){responseCounterLocal++;}
                    else{responseCounterAll++;}
                    EventBus.post(new BITResultPackage(scrubbedEventList,fesbn.isLocationFiltered));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BandsInTownEventResult>> call, Throwable t) {
                Log.d("RYAN TEST", artistName+" EVENT SEARCH RESPONSE FAILED");
                if(fesbn.isLocationFiltered()){responseCounterLocal++;}
                else{responseCounterAll++;}
                //Log.e(t.getMessage());
            }
        });
    }


    //FOR EVENT MANAGER CALLS

    @Subscribe
    public void receiveEventManagerQuery(final EventManagerRequest event){
        responseCounterAll = 0;
        responseCounterLocal = 0;
        pairs = event.getNameMbidPairs();
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
                if (event.isFilteredByLocation){
                    String city = SharedPreferencesHelper.getStringPreference(context.getString(R.string.user_city),"");
                    String stateAbbr = SharedPreferencesHelper.getStringPreference(context.getString(R.string.user_location_state_abr),"");
                    call = eventsApi.searchLocationalEventsByMbid(mbid, "json", city+" , "+stateAbbr, "2.0", "ConcertCompanion");
                }
                //asynchronous call
                call.enqueue(new Callback<ArrayList<BandsInTownEventResult>>() {
                    @Override
                    public void onResponse(Call<ArrayList<BandsInTownEventResult>> call, Response<ArrayList<BandsInTownEventResult>> response) {
                        if (response.body() == null && response.errorBody() != null){
                            //Check against known failures
                            String reqUrl = response.raw().request().url().toString();
                            String retMbid = reqUrl.substring(reqUrl.indexOf("mbid_")+5, reqUrl.indexOf("/events"));
                            try {
                                String errorMsg = response.errorBody().string();
                                if (errorMsg.contains("Unknown Artist")) {
                                    for(NameMbidPair p : pairs){
                                        if(p.getMbid().contains(retMbid)){
                                            //TRY AGAIN VIA NEW SEARCH
                                            eventMgrPair = new EventMgrNameMbidPair(p,event.isForcedRefresh(),event.isFilteredByLocation);
                                            Log.d("RYAN TEST",  p.getArtistName()+ " EVENT SEARCH BY MBID FAILED");
                                            EventBus.post(eventMgrPair);
                                            break;
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else if(response.body() != null){
                            String reqUrl = response.raw().request().url().toString();
                            String retMbid = reqUrl.substring(reqUrl.indexOf("mbid_")+5, reqUrl.indexOf("/events"));
                            NameMbidPair n = new NameMbidPair(artistName,retMbid);
                            for(NameMbidPair p : pairs){
                                if(p.getMbid().contains(retMbid)){
                                    n = new NameMbidPair(p.getArtistName(),retMbid);
                                    break;
                                }
                            }
                            bandsInTownEvents = response.body();
                            Log.d("RYAN TEST", artistName+" EVENT SEARCH BY MBID SUCCESS");
                            ArrayList<BandsInTownEventResult> scrubbedEventList = new ArrayList<>();
                            DatabaseHelper db = DatabaseHelper.getInstance(context);
                            favoritedShows = db.getFavoritedEventIds();
                            for (BandsInTownEventResult e : bandsInTownEvents) {
                                //check if attending
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
                            if(event.isFilteredByLocation){responseCounterLocal++;}
                            else{responseCounterAll++;}
                            EventBus.post(new BITResultPackageEventMgr(scrubbedEventList,n,event.isForcedRefresh(),event.isFilteredByLocation));
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<BandsInTownEventResult>> call, Throwable t) {
                        Log.d("RYAN TEST", artistName+" EVENT SEARCH BY MBID FAILED");
                        if(event.isFilteredByLocation){responseCounterLocal++;}
                        else{responseCounterAll++;}
                        //Log.e(t.getMessage());
                    }
                });
            }

        }

    }

    @Subscribe
    public void newEventMgrAttempt(final EventMgrNameMbidPair emPair){
        final NameMbidPair nmp = emPair.pair;
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
        final String artistMbid = nmp.getMbid();
        Call<ArrayList<BandsInTownEventResult>> call = eventsApi.searchEventsByArtistName(nmp.getArtistName(), "json", "2.0", "ConcertCompanion");
        if (emPair.isFilteredByLocation){
            String city = SharedPreferencesHelper.getStringPreference(context.getString(R.string.user_city),"Charleston");
            String stateAbbr = SharedPreferencesHelper.getStringPreference(context.getString(R.string.user_location_state_abr),"SC");
            call = eventsApi.searchLocationalEventsByArtistName(artistName, "json", city+" , "+stateAbbr, "2.0", "ConcertCompanion");
        }
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
                            EventBus.post(new BITResultPackageEventMgr(null,nmp,emPair.isForceRefresh, eventMgrPair.isFilteredByLocation));
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
                        if(e.getArtists().get(0).getMbid()==null){
                            e.getArtists().get(0).setMbid(artistMbid);
                        }
                        for (int i = 0; i < favoritedShows.size(); i++) {
                            if (e.getId() == favoritedShows.get(i).intValue()) {
                                e.setAttending(true);
                                break;
                            }
                        }
                        boolean a = e.isAttending();
                        scrubbedEventList.add(e);
                    }
                    if(emPair.isFilteredByLocation){responseCounterLocal++;}
                    else{responseCounterAll++;}

                    EventBus.post(new BITResultPackageEventMgr(scrubbedEventList,nmp,emPair.isForceRefresh, eventMgrPair.isFilteredByLocation));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BandsInTownEventResult>> call, Throwable t) {
                Log.d("RYAN TEST", artistName+" EVENT SEARCH RESPONSE FAILED");
                //force refresh is set true
                EventBus.post(new BITResultPackageEventMgr(null,nmp, emPair.isForceRefresh,emPair.isFilteredByLocation));
                //Log.e(t.getMessage());
            }
        });
    }

}

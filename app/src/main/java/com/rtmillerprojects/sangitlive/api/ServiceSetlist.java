package com.rtmillerprojects.sangitlive.api;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rtmillerprojects.sangitlive.ArtistTypeAdapterFactory;
import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.model.ArtistResults;
import com.rtmillerprojects.sangitlive.model.EventCalls.SetlistRequest;
import com.rtmillerprojects.sangitlive.model.LoadArtistEvent;
import com.rtmillerprojects.sangitlive.model.LoadSetlistsEvent;
import com.rtmillerprojects.sangitlive.model.PostArtistSearch;
import com.rtmillerprojects.sangitlive.model.SetlistsByArtists;
import com.squareup.otto.Subscribe;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ryan on 8/27/2016.
 */
public class ServiceSetlist {
    private SetlistsByArtists setlistsByArtists;
    private ArtistResults artistResults;
    private Context context;
    private String mbid;
    private int page;

    public ServiceSetlist(Application context){
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
    public void getSetlists(SetlistRequest sr){
        mbid = sr.getMbid();
        page = sr.getPage();
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new SetlistTypeAdapterFactory())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiServiceSetlist.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // prepare call in Retrofit 2.0
        ApiServiceSetlist setlistApi = retrofit.create(ApiServiceSetlist.class);
        //String radioheadKey = "a74b1b7f-71a5-4011-9441-d0b5e4122711";
        Call<SetlistsByArtists> call = setlistApi.getSetlists(mbid, page);
        //asynchronous call
        call.enqueue(new Callback<SetlistsByArtists>() {
            @Override
            public void onResponse(Call<SetlistsByArtists> call, Response<SetlistsByArtists> response) {
                setlistsByArtists = response.body();
                Log.d("RYAN TEST","SETLIST RESPONSE SUCCESS");
                EventBus.post(new LoadSetlistsEvent(setlistsByArtists));
            }

            @Override
            public void onFailure(Call<SetlistsByArtists> call, Throwable t) {
                //EventBus.post(new APIErrorEvent(RetrofitError.unexpectedError(response.getUrl(), new HttpException("Empty Body")), event.getCallNumber()));
            }
        });
    }

    private void getArtists(String artistSearchString, int page){
        try {
            artistSearchString = URLEncoder.encode(artistSearchString,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ArtistTypeAdapterFactory())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiServiceSetlist.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();



        ApiServiceSetlist setlistApi = retrofit.create(ApiServiceSetlist.class);
        Call<ArtistResults> call = setlistApi.getArtists(artistSearchString, page);
        //asynchronous call
        call.enqueue(new Callback<ArtistResults>() {
            @Override
            public void onResponse(Call<ArtistResults> call, Response<ArtistResults> response) {
                artistResults = response.body();
                Log.d("RYAN TEST","ARTISTS RESPONSE SUCCESS");
                EventBus.post(new LoadArtistEvent(artistResults));
            }

            @Override
            public void onFailure(Call<ArtistResults> call, Throwable t) {
                Log.d("RYAN TEST","ARTISTS RESPONSE FAILED");
                //EventBus.post(new APIError)
                //EventBus.post(new APIErrorEvent(RetrofitError.unexpectedError(response.getUrl(), new HttpException("Empty Body")), event.getCallNumber()));
            }
        });
    }
}

/* FROM FILE
public SetlistsByArtists getJsonFromFile(String mbid, int page) {

        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapterFactory(new SetlistTypeAdapterFactory());
        Gson gson = gsonBuilder.create();
        InputStream is = this.getResources().openRawResource(R.raw.samplesetlistjson);
        Reader reader = new InputStreamReader(is);
        SetlistsByArtists localSetlistsByArtists = gson.fromJson(reader, SetlistsByArtists.class);

        return localSetlistsByArtists;
    }
 */
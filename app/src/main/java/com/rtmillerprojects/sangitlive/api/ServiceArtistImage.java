package com.rtmillerprojects.sangitlive.api;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.model.ArtistImageEvent;
import com.rtmillerprojects.sangitlive.model.ArtistImageFound;
import com.rtmillerprojects.sangitlive.model.musicbrainzartistresourcemodel.MusicBrainzArtistResourceResult;
import com.rtmillerprojects.sangitlive.model.musicbrainzartistresourcemodel.Relation;
import com.squareup.otto.Subscribe;

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
public class ServiceArtistImage {
    private MusicBrainzArtistResourceResult resourceReults;
    private Context context;
    private String mbid;
    private boolean wasFound;
    private String imgUrl;
    private List<Long> favoritedShows;
    private String placeholderUrl = "http://cliparts.co/cliparts/pc5/odK/pc5odKdxi.png";

    public ServiceArtistImage(Application context){
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
    public void receiveQueryForArtistImage(ArtistImageEvent msg){
        ArrayList<String> mbids = msg.getMbids();
        Gson gson = new GsonBuilder()
                //.registerTypeAdapterFactory(new ())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiServiceMusicBrainz.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // prepare call in Retrofit 2.0
        //String radioheadKey = "a74b1b7f-71a5-4011-9441-d0b5e4122711";
        ApiServiceMusicBrainz artistApi = retrofit.create(ApiServiceMusicBrainz.class);
        for (int i = 0; i < msg.getMbids().size(); i++) {
            mbid = msg.getMbids().get(i);
            Call<MusicBrainzArtistResourceResult> call = artistApi.searchArtistImage(mbid,"json","url-rels");
            //asynchronous call
            call.enqueue(new Callback<MusicBrainzArtistResourceResult>() {
                @Override
                public void onResponse(Call<MusicBrainzArtistResourceResult> call, Response<MusicBrainzArtistResourceResult> response) {
                    resourceReults = response.body();
                    Log.d("RYAN TEST","EVENT SEARCH RESPONSE SUCCESS");
                    if(resourceReults!=null) {
                        wasFound = false;
                        for(Relation r : resourceReults.getRelations()){
                            if(r.getType().equals("image")){
                                imgUrl = r.getUrl().getResource();
                                wasFound = true;
                                break;
                            };
                        }
                        if(wasFound==false){imgUrl = placeholderUrl;}
                    }
                    else{
                        imgUrl = placeholderUrl;
                    }


                    EventBus.post(new ArtistImageFound(mbid, imgUrl));
                }

                @Override
                public void onFailure(Call<MusicBrainzArtistResourceResult> call, Throwable t) {
                    //EventBus.post(new APIErrorEvent(RetrofitError.unexpectedError(response.getUrl(), new HttpException("Empty Body")), event.getCallNumber()));
                }
            });
        }

    }

}

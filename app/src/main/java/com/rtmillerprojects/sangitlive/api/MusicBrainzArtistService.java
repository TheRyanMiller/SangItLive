package com.rtmillerprojects.sangitlive.api;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.model.ArtistImageEvent;
import com.rtmillerprojects.sangitlive.model.MusicBrainzArtist;
import com.rtmillerprojects.sangitlive.model.PostArtistSearch;
import com.rtmillerprojects.sangitlive.model.lastfmartistsearch.ArtistLastFm;
import com.rtmillerprojects.sangitlive.model.musicbrainzaritstbrowse.Artist;
import com.rtmillerprojects.sangitlive.model.musicbrainzaritstbrowse.ArtistBrowseResults;
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
public class MusicBrainzArtistService {
    private ArtistBrowseResults artistBrowseResults;
    private ArrayList<Artist> artistList = new ArrayList<>();
    private Context context;
    private String mbid;
    private boolean wasFound;
    private String imgUrl;
    private String placeholderUrl = "http://cliparts.co/cliparts/pc5/odK/pc5odKdxi.png";

    public MusicBrainzArtistService(Application context){
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
    public void receiveQueryForArtistSearch(PostArtistSearch msg){
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
        Call<ArtistBrowseResults> call = artistApi.browseArtists(msg.getArtistSearchString(),"json");

        //asynchronous call
        call.enqueue(new Callback<ArtistBrowseResults>() {
            @Override
            public void onResponse(Call<ArtistBrowseResults> call, Response<ArtistBrowseResults> response) {
                artistBrowseResults = response.body();
                Log.d("RYAN TEST","ARTIST SEARCH RESPONSE SUCCESS");
                artistList = new ArrayList<Artist>();
                if(artistBrowseResults!=null) {
                    artistList.addAll(artistBrowseResults.getArtists());
                    EventBus.post(artistList);
                }
            }

            @Override
            public void onFailure(Call<ArtistBrowseResults> call, Throwable t) {
                //EventBus.post(new APIErrorEvent(RetrofitError.unexpectedError(response.getUrl(), new HttpException("Empty Body")), event.getCallNumber()));
            }
        });


    }

}

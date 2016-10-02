package com.rtmillerprojects.sangitlive.api;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.model.EventCalls.GoogleLocationRequest;
import com.rtmillerprojects.sangitlive.model.GoogleLocation.LocationResults;
import com.squareup.otto.Subscribe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ryan on 9/28/2016.
 */
public class ServiceLocationGoogle {

    private LocationResults locationResults;
    private Context context;
    private String zip;
    private boolean wasFound;

    public ServiceLocationGoogle(Application context){
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
    public void queryForLocation(GoogleLocationRequest req){
        zip = req.getZip();
        Gson gson = new GsonBuilder()
                //.registerTypeAdapterFactory(new ())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiGoogleMaps.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiGoogleMaps mapsApi = retrofit.create(ApiGoogleMaps.class);

        Call<LocationResults> call = mapsApi.searchZip(zip);
        call.enqueue(new Callback<LocationResults>() {
            @Override
            public void onResponse(Call<LocationResults> call, Response<LocationResults> response) {
                locationResults = response.body();
                EventBus.post(locationResults);
            }
            @Override
            public void onFailure(Call<LocationResults> call, Throwable t) {
                //EventBus.post(new APIErrorEvent(RetrofitError.unexpectedError(response.getUrl(), new HttpException("Empty Body")), event.getCallNumber()));
            }

        });
    }
}

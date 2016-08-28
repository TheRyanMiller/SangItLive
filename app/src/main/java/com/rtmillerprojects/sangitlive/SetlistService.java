package com.rtmillerprojects.sangitlive;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ryan on 8/27/2016.
 */
public class SetlistService {
    private Bus mBus;
    int page;
    String urlQuery;
    private SetlistsByArtists setlistsByArtists;
    StringBuilder result = new StringBuilder();
    private HttpURLConnection urlConnection = null;

    public SetlistService(String urlQuery, Bus bus) {
        this.urlQuery = urlQuery;
        mBus = getBus();
    }


    public void makeRestCall() {

        //Load setlists api call


    }

    @Subscribe
    public void getJsonFromRest(final DoRestEvent event) {
        /**
         * x_api_key is stored in a strings resource file named secret.xml and
         * located in res/values, but secret.xml has been added to .gitignore so my key doesn't
         * end up in source control.
         *
         * simply create your own res/values/secret.xml file with your own Rotten Tomatoes API
         * key and the app will run as expected.
         */
//SETLIST FM URL http://api.setlist.fm/rest/0.1/artist/69d9c5ba-7bba-4cb7-ab32-8ccc48ad4f97/setlists.json
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // prepare call in Retrofit 2.0
        ApiService setlistApi = retrofit.create(ApiService.class);
        String radioheadKey = "859d0860-d480-4efd-970c-c05d5f1776b8";
        int page = 1;
        Call<SetlistsByArtists> call = setlistApi.getSetlists(radioheadKey, page);
        //asynchronous call
        call.enqueue(new Callback<SetlistsByArtists>() {
            @Override
            public void onResponse(Call<SetlistsByArtists> call, Response<SetlistsByArtists> response) {
                setlistsByArtists = response.body();
                Log.d("RYAN TEST","RESPONSE SUCCESS");
                getBus().post(new LoadSetlistsEvent(setlistsByArtists));
            }

            @Override
            public void onFailure(Call<SetlistsByArtists> call, Throwable t) {
                //EventBus.post(new APIErrorEvent(RetrofitError.unexpectedError(response.getUrl(), new HttpException("Empty Body")), event.getCallNumber()));
            }
        });

    }

    /* OLD VERSION OF REST REQUEST BELOW. DOES NOT INCLUDE RETROFIT
    */
    /*
        try {
            String urlString = event.getUrl();
            URL urlToRequest = new URL(urlString);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
            }

            // create JSON object from content
            InputStream stream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null && result.toString() != "") {
            String resultString = result.toString();
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .registerTypeAdapterFactory(new ItemTypeAdapterFactory());
            Gson gson = gsonBuilder.create();
            setlistsByArtists = gson.fromJson(resultString, SetlistsByArtists.class);
        } else {
            setlistsByArtists = null;
        }
        mBus.post(new LoadSetlistsEvent(setlistsByArtists));
    */

    private Bus getBus(){
        return EventBus.getBus();
    }


}

package com.rtmillerprojects.sangitlive;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ryan on 8/25/2016.
 */
public class ArtistSearchActivity extends AppCompatActivity {

    EditText searchString;
    Button btnSearch;
    ArtistsAdapter artistAdapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_search);

        searchString = (EditText) findViewById(R.id.artist_string);
        btnSearch = (Button) findViewById(R.id.btn_search);
        recyclerView = (RecyclerView) findViewById(R.id.artist_results);
        final Context context = this;

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtistResults aResults = requestWebService(searchString.getText().toString());
                String a = "";
                ArrayList<ArtistDetails> adList = new ArrayList<>();
                for(ArtistResults.ArtistsBean.ArtistBean ab : aResults.getArtists().getArtist()){
                    ArtistDetails ad = new ArtistDetails();
                    ad.setMbid(ab.getMbid());
                    ad.setName(ab.getName());
                    ad.setSortName(ab.getSortName());
                    ad.setUrl(ab.getUrl());
                    adList.add(ad);
                }
                artistAdapter = new ArtistsAdapter(adList, context);
                //layoutManager = new LinearLayoutManager(this);
                //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setAdapter(artistAdapter);
                //recyclerView.setLayoutManager(layoutManager);
            }
        });

    }

    public static ArtistResults requestWebService(String artistString) {
        String urlQuery = "http://api.setlist.fm/rest/0.1/search/artists.json?artistName=" + artistString;
        disableConnectionReuseIfNecessary();
        ArtistResults artistResults;
        StringBuilder result = new StringBuilder();

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(urlQuery);
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

            //return gson.fromJson(result.toString(),Response.class);
        } catch (MalformedURLException e) {
            // URL is invalid
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
        } catch (IOException e) {
            // could not read response body
            // (could not create input stream)
        }
        //catch (GsonEx e) {
        // response body is no valid JSON string
        //}
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        if(result!=null){
            GsonBuilder gsonBuilder = new GsonBuilder();
                    //.registerTypeAdapterFactory(new ItemTypeAdapterFactory());
            Gson gson = gsonBuilder.create();
            String resultString = result.toString();
            artistResults = gson.fromJson(new JsonParser().parse(resultString).getAsJsonObject(), ArtistResults.class);
            return artistResults;
        }
        else{
            return null;
        }
    }

    /**
     * required in order to prevent issues in earlier Android version.
     */
    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK)
                < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }
}

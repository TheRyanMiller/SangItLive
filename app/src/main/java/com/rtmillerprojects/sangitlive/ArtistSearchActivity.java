package com.rtmillerprojects.sangitlive;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
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
        searchString.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchString = String.valueOf(s);
                if(!TextUtils.isEmpty(searchString) && searchString.substring(searchString.length()-1).equals(" ")) {
                    artistSearch();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Call submit button

            }
        });
        btnSearch = (Button) findViewById(R.id.btn_search);
        recyclerView = (RecyclerView) findViewById(R.id.artist_results);
        final Context context = this;

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                artistSearch();
            }
        });

    }

    private void artistSearch() {
        ArtistResults aResults = requestWebService(searchString.getText().toString());
        if(aResults==null){
            //do something if null
            artistAdapter = new ArtistsAdapter(null, this);
            recyclerView.setAdapter(artistAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        else {
            ArrayList<ArtistDetails> adList = new ArrayList<>();
            for (ArtistResults.ArtistsBean.ArtistBean ab : aResults.getArtists().getArtist()) {
                ArtistDetails ad = new ArtistDetails();
                ad.setMbid(ab.getMbid());
                ad.setName(ab.getName());
                ad.setSortName(ab.getSortName());
                ad.setUrl(ab.getUrl());
                adList.add(ad);
            }
            artistAdapter = new ArtistsAdapter(adList, this);
            recyclerView.setAdapter(artistAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    public static ArtistResults requestWebService(String artistString) {

        disableConnectionReuseIfNecessary();
        ArtistResults artistResults;
        StringBuilder result = new StringBuilder();

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            String urlQuery = "http://api.setlist.fm/rest/0.1/search/artists.json?artistName=" + URLEncoder.encode(artistString, "UTF-8");
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
        if(result!=null && result.toString()!=""){
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

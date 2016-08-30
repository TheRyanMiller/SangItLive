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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

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
    Bus mBus;
    SetlistService setlistService;

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
                    getArtists(searchString);
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
                getArtists(searchString.getText().toString());
            }
        });

    }

    @Subscribe
    public void receiveArtistResults(LoadArtistEvent loadArtistEvent) {
        Toast.makeText(this,"ARTIST IS RETURNED",Toast.LENGTH_SHORT).show();
        ArtistResults aResults = loadArtistEvent.getArtistResults();
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

    private void getArtists(String artistSearchString){
        EventBus.post(new DoRestEvent(null, artistSearchString, 1));
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.unregister(this);
    }
}

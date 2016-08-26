package com.rtmillerprojects.sangitlive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView responseText;
    private TextView titleText;
    private Button searchButton;
    private SetAdapter siAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private EditText songString;
    private TextView resultsMsg;
    SetlistsByArtists responseObj;
    private Button btnChooseArtist;
    SetlistsByArtists setlistsByArtists;
    String url = "http://api.setlist.fm/rest/0.1/artist/69d9c5ba-7bba-4cb7-ab32-8ccc48ad4f97/setlists.json";
    Gson gson;
    String mbid;
    AsyncHttpClient client;
    ListView listView;
    private static final int CONNECTION_TIMEOUT = 150000;
    private static final int DATARETRIEVAL_TIMEOUT = 150000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String artistName = intent.getStringExtra("artistName");

        mbid = intent.getStringExtra("mbid");
        if(mbid!=null){
            setlistsByArtists = getJson(mbid);
        }

        recyclerView = (RecyclerView) findViewById(R.id.songlist);
        btnChooseArtist = (Button) findViewById(R.id.btn_choose_artist);
        resultsMsg = (TextView) findViewById(R.id.results_detail);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        songString = (EditText) findViewById(R.id.songSearch);
        searchButton = (Button) findViewById(R.id.searchButton);
        final Context context = this;
        if(artistName!=null) {
            btnChooseArtist.setText(artistName);
        }
        else{
            btnChooseArtist.setText("Choose an artist");
        }
        btnChooseArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchArtist();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //requestWebService(url);
                if(setlistsByArtists!=null) {
                    doSearch(songString.getText().toString(), setlistsByArtists);
                }
                else{
                    Toast.makeText(context,"No results found for this artist",Toast.LENGTH_SHORT).show();
                }

                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }

        });

        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory());
        Gson gson = gsonBuilder.create();
    }

    public SetlistsByArtists getJson(String mbid){
        InputStream is = this.getResources().openRawResource(R.raw.samplesetlistjson);
        Reader reader1 = new InputStreamReader(is);

        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try {
            // create connection

            String urlQuery = "http://api.setlist.fm/rest/0.1/search/setlists.json?artistMbid=" + URLEncoder.encode(mbid, "UTF-8");
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
            String resultString = result.toString();
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .registerTypeAdapterFactory(new ItemTypeAdapterFactory());
            Gson gson = gsonBuilder.create();
            SetlistsByArtists setlistsByArtists = gson.fromJson(resultString, SetlistsByArtists.class);

            return  setlistsByArtists;

        }
        else{
            return null;
        }

    }


    public void doSearch(String songString, SetlistsByArtists setlistsByArtists) {
        String songName = songString;

        ArrayList<SetInfo> setInfo = new ArrayList<>();

        SetlistsByArtists.SetlistsBean.SetlistBean.SetsBean.SetBean set;
        //SetlistsByArtists.SetlistsBean.SetlistBean.SetsBean.SetBean.SongBean song;
        String song;
        ArrayList<String> songs = new ArrayList<>();
        int instanceCounter = 0;
        int totalSetsCounted = 0;
        for (SetlistsByArtists.SetlistsBean.SetlistBean sl : setlistsByArtists.getSetlists().getSetlist()) {
            //PUT SETS OUT HERE1!!!
            SetInfo si = new SetInfo();
            si.setCity(sl.getVenue().getCity().getName()+", "+sl.getVenue().getCity().getCountry().getName());
            si.setDate(sl.getEventDate());
            si.setVenue(sl.getVenue().getName());
            si.setTourName(sl.getTour());
            si.setWasPlayed(false);
            for (int i = 0; i < sl.getSets().getSet().size(); i++) {
                set = sl.getSets().getSet().get(i);
                for (int k = 0; k < set.getSong().size(); k++) {
                    song = set.getSong().get(k).getName().toLowerCase();
                    songs.add(set.getSong().get(k).getName());
                    if (songName.toLowerCase().matches(song)) {
                        instanceCounter++;
                        si.setWasPlayed(true);
                        int abc = 1;
                    }
                    //Need to add song to set songs array
                }
            }
            si.setSongs(songs);
            songs.clear();
            setInfo.add(si);
            totalSetsCounted++;
            //NEED TO ADD SI
        }

        resultsMsg.setText("This song matched "+instanceCounter+" of "+totalSetsCounted+" sets searched");

        siAdapter = new SetAdapter(setInfo, this);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(siAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void searchArtist(){
        Intent intent = new Intent(this, ArtistSearchActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        startActivity(intent);
        //startActivityForResult(intent,ARTIST_SEARCH_CODE);
    }
}

